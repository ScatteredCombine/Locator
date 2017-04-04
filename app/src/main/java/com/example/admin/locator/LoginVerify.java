package com.example.admin.locator;

/*AUTHOR: Nitin Mamidala
****SCATTERED COMBINE****   */

//Importing all needed packages

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class LoginVerify extends AsyncTask<String,String,String> {
    //Declaring req. variables
    private Context context;
    private TextView login_status;
    public static String flag="";
    SharedPreferences sharedpreferences;
    Intent in;
    String uname,pwd;
    //Declaring variables for sharedpreferences xml mapping
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    public static final String loggedIn = "flagKey";

    public LoginVerify(Context context,TextView statusField) {
        Log.i("Login Verify","Constructor called..");
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
            String username = (String)arg0[0];  //Reading Parameters from MainActivity
            uname=username;
            String password = (String)arg0[1];
            pwd=password;
            //Link to reach the PHP page
            String link="http://randomid12321.000webhostapp.com/loginSession.php";
            //Encoding data to be sent to server
            String data  = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            Log.i("Login Verify","Got data: "+data);
            //Starting network connection
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            Log.i("Login Verify","Sent data to server");
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Reading Server Response
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
        flag=result;
        //Acting on server response
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
