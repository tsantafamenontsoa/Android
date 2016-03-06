package com.example.exo2;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class MainActivity extends Activity {


    Button login, register;
    EditText email, password;
    int success,responseCode;
    StringBuffer response;
    RegisterAsyncTask RegisterData;
    LoginAsyncTask LoginData;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle("Login & Register");
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);





        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LoginData = new LoginAsyncTask();
                LoginData.execute();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RegisterData = new RegisterAsyncTask();
                RegisterData.execute();

            }
        });



    }


    public class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {
        String url = "http://recruiting.api.fitle.com/user/create";
        String mail = email.getText().toString();
        String pwd = password.getText().toString();


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

                String urlParameters = "email="+ mail + "&password=\"" + pwd +"\"";
                Log.i("ParametreRegister :", urlParameters);
                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();


                responseCode = con.getResponseCode();
                Log.i("Valeur :", ""+responseCode);

                if(responseCode == 201) {
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




            } catch (java.io.IOException e) {
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
            if (responseCode == 201) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivityForResult(intent, 100);
                Toast.makeText(getApplicationContext(), "Your account has been created successfully",
                        Toast.LENGTH_LONG).show();

                finish();

            }
            else{
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.toString());
                    jsonObj = new JSONObject(response.toString());
                    Toast.makeText(getApplicationContext(), jsonObj.getString("error"),
                            Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), response,
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this,
                        MainActivity.class);
                startActivityForResult(intent, 100);
                finish();
            }
            //Log.i("Valeur :", response.toString());


        }

    }

    public class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
        String url = "http://recruiting.api.fitle.com/user/token";
        String mail = email.getText().toString();
        String pwd = password.getText().toString();

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
                // optional default is GET
                con.setRequestMethod("POST");

                String urlParameters = "email="+ mail + "&password=\"" + pwd +"\"";
                con.setDoOutput(true);


                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();


                responseCode = con.getResponseCode();
                Log.i("Parametres :", urlParameters);
                Log.i("Valeur :", ""+responseCode);

                if(responseCode == 200) {

                    Authenticator.setDefault(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            PasswordAuthentication pa = new PasswordAuthentication(token, "".toCharArray());
                            return pa;
                        }
                    });

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




            } catch (java.io.IOException e) {
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
            if (responseCode == 200) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.toString());
                    jsonObj = new JSONObject(response.toString());
                    token = jsonObj.getString("token");
                    Log.i("Token :", token);
                    Intent intent = new Intent(MainActivity.this, GetMessage.class);
                    intent.putExtra("Token",token);
                    startActivityForResult(intent, 100);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.toString());
                    jsonObj = new JSONObject(response.toString());

                    Toast.makeText(getApplicationContext(),jsonObj.getString("error") ,
                            Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),response.toString() ,
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this,
                        MainActivity.class);
                startActivityForResult(intent, 100);
                finish();
            }
            //Log.i("Valeur :", response.toString());

        }

    }

}




