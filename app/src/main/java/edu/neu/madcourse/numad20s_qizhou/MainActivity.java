package edu.neu.madcourse.numad20s_qizhou;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.text.util.Linkify;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView myListView;
    EditText editText;
    TextView findPrime;
    Boolean primeState = false;
    Integer primeNumber = 2;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    TextView setAlarm;
    PowerConnectionReceiver powerConnectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myListView = findViewById(R.id.myListView);
        findPrime = findViewById(R.id.findPrime);
        setAlarm = findViewById(R.id.timer);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String url = (String)adapter.getItemAtPosition(position);
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                listItems);
        myListView.setAdapter(adapter);
        editText = (EditText) findViewById(R.id.textInputEditText);
        powerConnectionReceiver = new PowerConnectionReceiver();
        IntentFilter filter = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("android.intent.action.ACTION_BATTERY_CHANGED");
        this.registerReceiver(powerConnectionReceiver, filter);
        FloatingActionButton fab = findViewById(R.id.add_to_list_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addListItem();
                Snackbar.make(view, "Item is added to the list."
                        , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    private void addListItem() {
        String link = editText.getText().toString();
        listItems.add(link);
        adapter.notifyDataSetChanged();
        editText.setText(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/**
    public void showInfo(View view){
        TextView textView = findViewById(R.id.textView);
        textView.setText("Qi Zhou \n zhou.qi@husky.neu.edu");
        Button aboutButton = findViewById(R.id.aboutButton);
        Button backButton = findViewById(R.id.backButton1);
        aboutButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void back1(View view){
        TextView textView = findViewById(R.id.textView);
        textView.setText("www.google.com");
        Button aboutButton = findViewById(R.id.aboutButton);
        Button backButton = findViewById(R.id.backButton1);
        aboutButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
    }
 **/
}
