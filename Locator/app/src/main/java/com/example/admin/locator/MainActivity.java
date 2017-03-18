package com.example.admin.locator;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    static EditText uname,pwd;
    static ProgressBar progressBar;
    Button login_button;
    TextView login_status;
    Intent in;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    public static final String loggedIn = "flagKey";
    public  static boolean flag=false;

    SharedPreferences sharedpreferences;
    String store_uname,store_pwd;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Entry","Entered onCreate in Activity 1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname=(EditText)findViewById(R.id.userName);
        pwd=(EditText)findViewById(R.id.password);
        login_button=(Button)findViewById(R.id.LOGIN_BUTTON);
        login_status=(TextView)findViewById(R.id.login_status);
        //-------
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-------- this is to hide the keyboard after pressing login button
                pwd.clearFocus();
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                //--------
                Log.i("login button","onClick");
                store_uname  = uname.getText().toString();
                store_pwd  = pwd.getText().toString();

                new LoginVerify(MainActivity.this, login_status).execute(store_uname, store_pwd);
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                Log.i("MainActivity","LoginVerify flag:"+LoginVerify.flag);
/*
                    if (LoginVerify.flag.equals("true")) {
                        Log.i("LoginVerify", "true");
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(Name, store_uname);
                        editor.putString(Pass, store_pwd);
                        editor.putBoolean(loggedIn, true);
                        editor.commit();

                        in = new Intent(MainActivity.this, second_main_homepage.class);
                        startActivity(in);
                    } else {
                        Log.i("LoginVerify", "false");
                        login_status.setText("Invalid username/password");
                        Toast.makeText(getApplicationContext(), "Enter valid username/password", Toast.LENGTH_SHORT).show();
                        uname.setText("");
                        pwd.setText("");

                    }

*/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = sharedpreferences.getBoolean(loggedIn,false);

        //If we will get true
        if(flag){
            //We will start the Profile Activity
            Intent intent = new Intent(MainActivity.this, second_main_homepage.class);
            //-------
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            //finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
