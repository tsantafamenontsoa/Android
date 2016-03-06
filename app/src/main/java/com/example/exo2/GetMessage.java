package com.example.exo2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by Tsanta on 26/02/2016.
 */
public class GetMessage extends Activity {

    MessageAsyncTask messageData;
    String token,message;
    int responseCode;
    StringBuffer response;
    TextView messageTextView;
    Button bouton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_message);


        Intent intent = getIntent();
        token = intent.getStringExtra("Token");
        Log.i("Tokenmessage :", token);
        messageTextView = (TextView) findViewById(R.id.textView3);

        messageData = new MessageAsyncTask();
        messageData.execute();

        bouton = (Button) findViewById(R.id.button2);
        bouton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Authenticator.setDefault(null);
                Intent intent = new Intent(GetMessage.this,MainActivity.class);
                startActivityForResult(intent, 100);
                finish();
            }
        });
    }
    public class MessageAsyncTask extends AsyncTask<Void, Void, Void> {
        String url = "http://recruiting.api.fitle.com/secret";

        protected void onPreExecute() {
            Log.i("add", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");

            URL obj = null;
            try {
                obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");

                responseCode = con.getResponseCode();
                Log.i("Valeurmessage :", ""+responseCode);

                if(responseCode == 200) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                }
                else{

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getErrorStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                }




            } catch (IOException e) {
                e.printStackTrace();

            }
            Log.i("add", " end doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("add", "onPostExecute");
            super.onPostExecute(result);
            //print result
            if (responseCode == 200){
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.toString());
                    jsonObj = new JSONObject(response.toString());
                    message = jsonObj.getString("message");
                    Log.i("Token :", token);
                    messageTextView.setText(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                messageTextView.setText(response.toString());
            }

        }

    }

}