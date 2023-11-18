package com.practice.myweather

import android.app.DownloadManager.Request
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    lateinit var btnSub: Button
    lateinit var etCity: EditText
    lateinit var etCountry: EditText
    lateinit var tvRes: TextView

    val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="
    val apiKey = "a73271f4624e70c8da6a8ffaa11a0da2"

    var df = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSub = findViewById(R.id.submit)
        etCountry = findViewById(R.id.country)
        etCity = findViewById(R.id.city)
        tvRes = findViewById(R.id.tvRes)

        btnSub.setOnClickListener {
            var tempUrl = ""
            var city = etCity.text.toString()
            var country = etCountry.text.toString()

            if (city.equals("")){
                tvRes.text = "City cannot be Empty"
            } else {
                if (!country.equals("")) {
                    tempUrl = apiUrl + city + ',' + country + "&appid=" + apiKey
                } else {
                    tempUrl = apiUrl + city + "&appid=" + apiKey
                }
                val queue = Volley.newRequestQueue(this)

                val strReq = StringRequest(com.android.volley.Request.Method.POST, tempUrl,
                    { response ->
                        var output = ""
                        try{
                            var jsonResp = JSONObject(response)
                            var array = jsonResp.getJSONArray("weather")
                            var jsonweather = array.getJSONObject(0)
                            var jsondesc = jsonweather.getString("main")
                            var jsonObjMain = jsonResp.getJSONObject("main")
                            var temp = jsonObjMain.getDouble("temp") - 273.15
                            var feels = jsonObjMain.getDouble("feels_like") - 273.15
                            var humidity = jsonObjMain.getInt("humidity")
                            var jsonSys = jsonResp.getJSONObject("sys")
                            var ctry_name = jsonSys.getString("country")
                            var city_name = jsonResp.getString("name")
                            var pressure = jsonObjMain.getString("pressure")

                            tvRes.setTextColor(Color.rgb(38,77,153))
                            output += "Current Weather in "+city_name+","+ctry_name+" is: "+jsondesc+"\nwith a temperature of " +df.format(temp)+" Degree Celsius.\nBut it will feel like "+df.format(feels)+" Degree Celsius.\nThe Humidity is "+humidity+"%\nThe Pressure in hPA: "+pressure

                            tvRes.setText(output)
                        }catch (e : JSONException){
                            e.printStackTrace()
                        }






                    },
                    { Toast.makeText(applicationContext, "Something Wrong", Toast.LENGTH_LONG).show() })

                queue.add(strReq)

            }
        }

    }

}

