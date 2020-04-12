package edu.neu.madcourse.numad20s_qizhou.activities;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import edu.neu.madcourse.numad20s_qizhou.R;
import edu.neu.madcourse.numad20s_qizhou.fragments.TasksFragment;
import edu.neu.madcourse.numad20s_qizhou.model.Task;
import edu.neu.madcourse.numad20s_qizhou.repos.CardViewModel;
import edu.neu.madcourse.numad20s_qizhou.repos.MemberViewModel;
import edu.neu.madcourse.numad20s_qizhou.repos.TaskViewModel;
import static edu.neu.madcourse.numad20s_qizhou.R.layout.activity_main;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {
    private CardViewModel cardViewModel;
    private TaskViewModel taskViewModel;
    private MemberViewModel memberViewModel;
    private ArrayList<Task> taskList;
    private BottomNavigationView bottomNavigation;
    public OnMainViewsClickListener fragmentInterfaceListener;
    LocationManager lm;
    private SensorManager sensorManager;
    private Sensor temp;
    private Sensor humid;
    String ambientTemp;
    String humidity;
    private TextView weather;
    private String weatherText;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Main page created");
        setContentView(activity_main);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        taskList = new ArrayList<>();
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                System.out.println("Main act observer found data size changed");
                if(tasks.size()<1){
                    taskList.clear();
                }else {
                    ArrayList<Task> taskListCopy = new ArrayList<>();
                    taskListCopy.addAll(tasks);
                    taskList = taskListCopy;
                    }
                makeSnackBar();
                }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        FloatingActionButton fab = findViewById(R.id.add_task_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.
                        class);
                startActivity(intent);
            }
        });
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                TasksFragment.newInstance(true,taskViewModel,cardViewModel,lm,this)).commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_incoming:
                                fragmentInterfaceListener.onNavigationButtonClicked("INCOMING");
                                break;
                            case R.id.navigation_inprogress:
                                fragmentInterfaceListener.onNavigationButtonClicked("INPROGRESS");
                                break;
                            case R.id.navigation_completed:
                                fragmentInterfaceListener.onNavigationButtonClicked("COMPLETED");
                                break;
                            case R.id.navigation_all:
                                fragmentInterfaceListener.onNavigationButtonClicked("ALL");
                                break;
                        }
                        return true;
                    }
                });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humid = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        String msg = "not supported by the device";
        if (this.humidity == null){
            this.humidity = msg;
        }
        if (this.ambientTemp == null){
            this.ambientTemp = msg;
        }
        weatherText = "Humidity: " + this.humidity + " ; Temperature: " + this.ambientTemp;
        weather = findViewById(R.id.temp_humid);
        weather.setText(weatherText);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions() {
        System.out.println("Requesting permission");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1111);
    }

    protected void makeSnackBar() {
        Integer size = taskList.size();
        String message = String.format ("You have %d tasks!", size);
        System.out.println(message);
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
        Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_regular);
        TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById( com.google.android.material.R.id.snackbar_action );
        TextView snackbarTextView = (TextView) snackbar.getView().findViewById( com.google.android.material.R.id.snackbar_text );
        snackbarActionTextView.setTextSize( 16 );
        snackbarTextView.setTextSize( 16 );
        snackbarActionTextView.setTypeface(typeface);
        snackbarTextView.setTypeface(typeface);
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("main act resumed");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {
            showDeleteAllWarningDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showDeleteAllWarningDialog() {

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Are you sure you want to delete all your tasks?");
        build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taskViewModel.deleteAll();
                fragmentInterfaceListener.onDeleteClicked();
            }
        });
        build.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = build.create();
        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onSensorChanged(SensorEvent event) {
        String msg = "not supported by the device";
        String type = event.sensor.getStringType();
        if (type.equals(Sensor.STRING_TYPE_AMBIENT_TEMPERATURE)){
        this.ambientTemp = String.format("%.1f", event.values[0]);
        }else {
            this.humidity = String.format("%.1f", event.values[0]);
       }
        if (this.humidity == null){
            this.humidity = msg;
        }
        if (this.ambientTemp == null){
            this.ambientTemp = msg;
        }
        weatherText = "   Humidity: " + this.humidity + " ; Temperature: " + this.ambientTemp;
        weather.setText(weatherText);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public interface OnMainViewsClickListener {

        void onDeleteClicked();

        void onPriorityButtonClicked(String cardPriority);

        void onNavigationButtonClicked(String cardStatus);
    }

}

