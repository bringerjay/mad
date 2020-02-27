package edu.neu.madcourse.numad20s_qizhou;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AddLinkActivity extends AppCompatActivity {
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView myListView;
    private LinkViewModel linkViewModel;
    public static final int NEW_LINK_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPLY = "com.example.android.linklistsql.REPLY";
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_link);
        linkViewModel = new ViewModelProvider(this).get(LinkViewModel.class);
        myListView = findViewById(R.id.myListView);
        linkViewModel.getAllLinks().observe(this, new Observer<List<Link>>() {
            @Override
            public void onChanged(@Nullable final List<Link> links) {
                System.out.println(links.size());
                ArrayList<String> copy = new ArrayList<>(links.size());
                if (links.size() <= 1){
                    return;
                }else {
                    listItems.add(links.get(links.size()-1).getLink());
                }
            }
        });
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String url = (String) adapter.getItemAtPosition(position);
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
        String linkUrl = editText.getText().toString();
        Link link = new Link(linkUrl);
        linkViewModel.insert(link);
        adapter.notifyDataSetChanged();
        editText.setText(null);
    }
}
