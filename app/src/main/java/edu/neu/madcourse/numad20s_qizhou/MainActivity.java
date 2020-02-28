package edu.neu.madcourse.numad20s_qizhou;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.SystemClock;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    TextView findPrime;
    TextView showLocation;
    Boolean primeState = false;
    Integer primeNumber = 2;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    TextView setAlarm;
    PowerConnectionReceiver powerConnectionReceiver;
    double longitude;
    double latitude;
    LocationManager lm;
    Location location;
    LocationListener locationListener;
    public static final int NEW_LINK_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPLY = "com.example.android.linklistsql.REPLY";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findPrime = findViewById(R.id.findPrime);
        setAlarm = findViewById(R.id.timer);
        powerConnectionReceiver = new PowerConnectionReceiver();
        IntentFilter filter = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("android.intent.action.ACTION_BATTERY_CHANGED");
        this.registerReceiver(powerConnectionReceiver, filter);
        FloatingActionButton fab = findViewById(R.id.add_link_action);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddLinkActivity.class);
                startActivityForResult(intent, NEW_LINK_ACTIVITY_REQUEST_CODE);
            }
        });
        showLocation = findViewById(R.id.location);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                String newLocation = "Latitude : " + latitude + "\nLongitude : " + longitude;
                showLocation.setText(newLocation);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        requestPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions() {
        System.out.println("Requesting permission");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1111);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        System.out.println("Requesting responded");
        if (requestCode == 1111) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude_init = location.getLongitude();
                double latitude_init = location.getLatitude();
                String newLocation_init = "Latitude : " + latitude_init + "\nLongitude : " + longitude_init;
                System.out.println(newLocation_init);
                showLocation.setText(newLocation_init);
            }
            return;
        }
    }


    public void start_find_primes(View view) {
    StartTask runner = new StartTask();
    runner.execute();
    }

    public void stop_find_primes(View view) {
        StopTask runner = new StopTask();
        runner.execute();
    }

    public void start_timer(View view) {
        System.out.println("clicked");
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this,0,myIntent,0);
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(),1000,
                alarmIntent);
        System.out.println("Click processed");
        setAlarm.setText("The time watch is on.");
    }

    public void stop_timer(View view) {
        alarmMgr.cancel(alarmIntent);
        setAlarm.setText("The time watch is off.");
    }

    private class StartTask extends AsyncTask<String, String, String> {

        private String newMsg;

        @Override
        protected String doInBackground(String... params) {
            while (primeState){
                newMsg = validatePrime(primeNumber);
                publishProgress(newMsg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                primeNumber = primeNumber + 1;
            }
            return newMsg;
        }


        @Override
        protected void onPostExecute(String result) {
            String stop = "Finding prime stopped at : " + primeNumber.toString();
            findPrime.setText(stop);
        }


        @Override
        protected void onPreExecute() {
            primeState = true;
        }


        @Override
        protected void onProgressUpdate(String... text) {
            findPrime.setText(text[0]);
        }
    }


    private class StopTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPreExecute() {
            primeState = false;
        }

    }

    private String validatePrime(Integer n){
        String newMsg = "Checking the number : " + n.toString();
        for (int i = 2; i < n; i++) {
                if (n % i == 0)
                    newMsg = newMsg + ". It is a prime.";
                return newMsg;
            }
            newMsg = newMsg + ". It is a prime.";
        findPrime.setText(newMsg);
        return newMsg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
