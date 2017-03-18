package com.example.admin.locator;

/**
 * Created by ADMIN on 13-03-2017.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.util.Log;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
//----
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class LoginVerify extends AsyncTask<String,String,String> {
    private Context context;
    private TextView login_status;
    public static String flag="";
    //---------
    SharedPreferences sharedpreferences;

    Intent in;
    String uname,pwd;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    public static final String loggedIn = "flagKey";
    //----

    public LoginVerify(Context context,TextView statusField) {
        this.context = context;
        this.login_status = statusField;
    }

    protected void onPreExecute(){
        Log.i("LoginVerify","PreExecute");
        this.login_status.setText("Connecting server..");
        MainActivity.progressBar.setProgress(30);
    }


    @Override
    protected String doInBackground(String... arg0) {
        Log.i("LoginVerify","doInBG");
        try{
            String username = (String)arg0[0];
            //--------
            uname=username;
            String password = (String)arg0[1];
            //----
            pwd=password;
            String link="http://192.168.0.6:80/loginSession.php";
            //String link="http://10.0.2.2/searchSession.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            Log.i("Login Verify","Got data: "+data);
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();
            Log.i("Login Verify","s2s");
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            Log.i("Login Verify","Read server response");
            return (sb.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            Log.i("Login Verify","Exception:"+e.getMessage());
            return (new String("Exception: " + e.getMessage()));

        }
    }
    @Override
    protected void onPostExecute(String result){
        MainActivity.progressBar.setProgress(60);
        Log.i("LoginVerify","PostExecute");
        //this.statusField.setText("Connected to server..");
        flag=result;
        //call verify func
        //---
        if(flag.equals("true")){
            MainActivity.progressBar.setProgress(90);
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            Log.i("TEST VALUE NAME:","NAME: "+uname);
            editor.putString(Name, uname );
            editor.putString(Pass, pwd);
            editor.putBoolean(loggedIn, true);
            MainActivity.progressBar.setProgress(100);
            editor.commit();
            MainActivity.progressBar.setVisibility(View.GONE);
            in = new Intent(context, second_main_homepage.class);
            //-----------to clear all activities on top of stack
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(in);
        } else {
            MainActivity.progressBar.setProgress(80);
            this.login_status.setText("Invalid username/password");
            MainActivity.uname.setText("");
            MainActivity.pwd.setText("");
            MainActivity.progressBar.setProgress(100);
            Toast.makeText(context, "Enter valid username/password", Toast.LENGTH_SHORT).show();
            MainActivity.progressBar.setVisibility(View.GONE);
        }
    }
}
