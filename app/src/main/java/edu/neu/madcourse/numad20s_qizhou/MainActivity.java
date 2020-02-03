package edu.neu.madcourse.numad20s_qizhou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myListView = findViewById(R.id.myListView);
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
