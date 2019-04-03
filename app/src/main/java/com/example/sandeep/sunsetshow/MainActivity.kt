package com.example.sandeep.sunsetshow

import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.zip.CheckedInputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    protected fun GetSunset(view:View){
        var city=cityText.text.toString()
        val url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%2Cny%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)
    }
    inner class MyAsyncTask: AsyncTask<String, String, String>() {
        override fun onPreExecute(){
            //Before execute
        }
        override fun doInBackground(vararg p0: String?): String {
                //after execute
            try {
                    var url=URL(p0[0])
                    val urlConnect=url.openConnection() as HttpURLConnection
                    urlConnect.connectTimeout=7000
                    var inString=convertTostring(urlConnect.inputStream)
                    publishProgress(inString)
            }catch (ex:Exception){}
            //Cannot access in UI
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json=JSONObject(values[0])
                val query = json.getJSONObject("query")
                val results=query.getJSONObject("results")
                val channel=results.getJSONObject("channel")
                val astronomy=channel.getJSONObject("astronomy")
                var sunrise=astronomy.getString("sunrise")
                showText.setText("Sunrise is at $sunrise")
            }catch(ex:Exception){}
        }

        override fun onPostExecute(result: String?) {
            //sfter excute
        }

    }
    fun convertTostring(inputStream: InputStream):String{
        val bufferReader=BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString:String=""
        try{
             do{
                        line=bufferReader.readLine()
                        if (line!= null){
                            allString +=line
                        }
            }while (line!=null)
            inputStream.close()

        }catch(ex:Exception){}
        return allString
    }

}
