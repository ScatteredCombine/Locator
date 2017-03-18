package com.example.admin.locator;

/**
 * Created by ADMIN on 13-03-2017.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.util.Log;

import android.os.Handler;

public class SrvrSvc extends Service {
    int rssi,level,percentage;
    String name,addr,pc,link,data,username;
    final Handler handler = new Handler();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
    }

    Runnable outer = new Object() {
        final Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                new ConnectSrv().execute("send");
                Log.d("Handlers", "Called on main thread");
                handler.postDelayed(runnableCode, 10000);
            }
        };
    }.runnableCode;

    @Override
    public void onStart(Intent intent, int startid) {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifi != null && wifi.isWifiEnabled()) {
            //The code
            rssi = wifi.getConnectionInfo().getRssi(); //rssi ante signal strength in decibels
            name = wifi.getConnectionInfo().getSSID(); //ssid ante connect ayina network peru
            addr = wifi.getConnectionInfo().getBSSID(); //idi emo mac address
            level = WifiManager.calculateSignalLevel(rssi, 100);  //to convert rssi on a scale of 1 to 100
            percentage = (int) ((level / 100.0) * 100);
            pc=Integer.toString(percentage);
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            username=sharedpreferences.getString(MainActivity.Name,"nameKey");
        }
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        //----------------------------------
        handler.post(outer);
        //----------------------------------
        //new ConnectSrv().execute("send");
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        handler.removeCallbacks(outer);
    }

    private class ConnectSrv extends AsyncTask<String,String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("OnPreExecute()", "");
        }

        @Override
        protected String doInBackground(String... arg0) {
            Log.i("doInBackground()", "");
            try {
                Log.i("*IN TRY BLOCK*", "TRY");
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if(wifi != null && wifi.isWifiEnabled()) {
                    //The code
                    rssi = wifi.getConnectionInfo().getRssi(); //rssi ante signal strength in decibels
                    name = wifi.getConnectionInfo().getSSID(); //ssid ante connect ayina network peru
                    addr = wifi.getConnectionInfo().getBSSID(); //idi emo mac address
                    level = WifiManager.calculateSignalLevel(rssi, 100);  //to convert rssi on a scale of 1 to 100
                    percentage = (int) ((level / 100.0) * 100);
                    pc=Integer.toString(percentage);
                    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    username=sharedpreferences.getString(MainActivity.Name,"nameKey");
                }
                link = "http://192.168.0.6:80/locator_s2s.php";
                data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                        URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("mac", "UTF-8") + "=" +
                        URLEncoder.encode(addr, "UTF-8");
                data += "&" + URLEncoder.encode("percentage", "UTF-8") + "=" +
                        URLEncoder.encode(pc, "UTF-8");
                Log.i("DATA",""+data);
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                Log.i("Conn","URL Conn open");
                conn.setDoOutput(true);
                Log.i("OP","Set Conn as OP");
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                Log.i("OSR","Got OP stream");
                wr.write(data);
                wr.flush();
                Log.i("Write","writing data..");
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                return (sb.toString());
            }
            catch(Exception e){
                Log.e("Exception:",""+e.getMessage());
                e.printStackTrace();
                return (new String("Exception: " + e.getMessage()));
            }
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            Log.i("OnPostExecute()", ""+result);
        }

    }
}
