package com.example.admin.locator;

/*AUTHOR: Nitin Mamidala
****SCATTERED COMBINE****   */

//Importing all needed packages
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;

//Creating MainActivity class
public class MainActivity extends AppCompatActivity {   //DO NOT REMOVE APPCOMPATACTIVITY AS EXTENSION
    //Declaring all components used for login screen
    static EditText uname,pwd;
    static ProgressBar progressBar;
    Button login_button;
    TextView login_status;
    //Declaring variables for SharedPreferences
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    //public static final String Pass = "passKey";
    public static final String loggedIn = "flagKey";
    public  static boolean flag=false;
    //Declaring req. variables
    SharedPreferences sharedpreferences;
    String store_uname,store_pwd;
    boolean doubleBackToExitPressedOnce = false;    //Check flag for double-back-exit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Entry in MainActivity","Entered onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialising view components
        uname=(EditText)findViewById(R.id.userName);
        pwd=(EditText)findViewById(R.id.password);
        login_button=(Button)findViewById(R.id.LOGIN_BUTTON);
        login_status=(TextView)findViewById(R.id.login_status);
        login_status.setGravity(Gravity.CENTER);    //Aligning status text to center
        progressBar=(ProgressBar)findViewById(R.id.progressBar);    //Progress bar feature added in v1.1
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);  //Initialising sharedpreferences

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Main Activity","LoginButton onClick");
                //-------- this is to hide the keyboard after pressing login button
                pwd.clearFocus();
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                //--------
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE); //Show progress bar
                progressBar.setProgress(5);
                store_uname  = uname.getText().toString();  //Getting user input
                store_pwd  = pwd.getText().toString();
                //Calling AsyncTask to provide network connection
                Log.i("Main Activity","Calling AsyncTask..");
                new LoginVerify(MainActivity.this, login_status).execute(store_uname, store_pwd);

                    /*  LOGIC FOR LOGIN VERIFY SHIFTED TO ASYNCTASK TO OVERCOME SEVERAL TEST CASE FAILURES V1.1
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
        Log.i("Main Activity","OnResume Method");
        flag = sharedpreferences.getBoolean(loggedIn,false);
        //If we will get true
        if(flag){
            //We will start the Profile Activity
            Log.i("Main Activity","User logged in already so calling homepage");
            Intent intent = new Intent(MainActivity.this, second_main_homepage.class);
            //-------clearing all activities on top of stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("Main Activity","onBackPressed");
        if (doubleBackToExitPressedOnce) {      //code to handle double-back-exit
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
        Log.i("Main Activity","onDestroy");
    }
}
