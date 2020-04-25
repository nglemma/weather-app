package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    EditText cityeditText;

    TextView resulttextview, resulttextview2;

    public void check(View view)
    {
        String cityname;
        cityname = cityeditText.getText().toString();
        if(cityname.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Textfield can't be empty",Toast.LENGTH_LONG).show();
            cityname="lagos,Nigeria";
        }
        String uurl="http://api.openweathermap.org/data/2.5/weather?q="+cityname+"&APPID=a28acae38322dc74695578ca270fc41a";
        DownloadTask task = new DownloadTask();
        task.execute(uurl);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityeditText.getWindowToken(),0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityeditText = findViewById(R.id.cityeditText);
        resulttextview = findViewById(R.id.resulttextview);
        resulttextview2 = findViewById(R.id.resulttextview2);


    }
    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try
            {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        public void onPostExecute(String s)
        {
            super.onPostExecute(s);
            //Log.i("message",s);
            try
            {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);

                for (int i=0; i < arr.length(); i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    resulttextview.setText(jsonPart.getString("main"));
                    resulttextview2.setText(jsonPart.getString("description"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    }
