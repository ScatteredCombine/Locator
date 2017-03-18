package com.example.admin.locator;

import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class second_main_homepage extends AppCompatActivity {

    String name="";
    TextView welcome_msg;
    Intent in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("HomePage","In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main_homepage);
        welcome_msg=(TextView)findViewById(R.id.WELCOME_USER);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        name=sharedpreferences.getString(MainActivity.Name,"nameKey");
        welcome_msg.setText("Welcome "+name + " !");
    }

    public void start_service(View view){
        Log.i("homepage","starting service..");
        startService(new Intent(this, SrvrSvc.class));
    }

    public void stop_service(View view){
        Log.i("homepage","stopping service..");
        stopService(new Intent(this, SrvrSvc.class));
    }

    public  void logout(View view){
        Log.i("homepage","logging out");
        LoginVerify.flag="false";
        Log.i("HomePage","Login Verify flag:"+LoginVerify.flag);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(getApplicationContext(),"Logged out successfully!", Toast.LENGTH_SHORT).show();
        finish();
        in = new Intent(second_main_homepage.this, MainActivity.class);
        //-------------to clear all activities on top of stack
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press HOME button to exit", Toast.LENGTH_SHORT).show();
    }

}
