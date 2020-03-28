package edu.neu.madcourse.numad20s_qizhou.activities;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import edu.neu.madcourse.numad20s_qizhou.R;
import edu.neu.madcourse.numad20s_qizhou.managers.*;
import edu.neu.madcourse.numad20s_qizhou.model.*;
import edu.neu.madcourse.numad20s_qizhou.fragments.*;
import static edu.neu.madcourse.numad20s_qizhou.R.layout.activity_main;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private TextView helloNote;
    private TextView taskSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helloNote = findViewById(R.id.inner_welcome_title);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        taskSum = (TextView) headerView.findViewById(R.id.total_tasks);
        FloatingActionButton fab = findViewById(R.id.add_task_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_tasks);
        setNavItem(R.id.nav_tasks);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                taskSum.setText(String.valueOf(TaskManager.getInstance().tasksList.size()));
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();



    }

    @Override
    protected void onResume() {
        super.onResume();
        String message = String.format ("You have %d tasks!", TaskManager.getInstance().tasksList.size());
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

        //Other stuff in OnCreate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {
            showDeleteAllWarningDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();
        setNavItem(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //  endregion


    //
    // region helpers
    //

    @SuppressLint("ResourceAsColor")
    private void setNavItem(int id) {

        switch (id) {
            case R.id.nav_tasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        TasksFragment.newInstance(false)).commit();
                helloNote.setText(getString(R.string.welcome_title_tasks));
                break;

            case R.id.low_priority_task:
                fragmentInterfaceListener.onPriorityButtonClicked(Card.CardPriority.LOW);
                helloNote.setText(getString(R.string.welcome_title_low));
                break;

            case R.id.med_priority_task:
                fragmentInterfaceListener.onPriorityButtonClicked(Card.CardPriority.MEDIUM);
                helloNote.setText(getString(R.string.welcome_title_medium));
                break;

            case R.id.high_priority_task:
                fragmentInterfaceListener.onPriorityButtonClicked(Card.CardPriority.HIGH);
                helloNote.setText(getString(R.string.welcome_title_high));
                break;

            case R.id.nav_kanban:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        TasksFragment.newInstance(true)).commit();
                helloNote.setText(getString(R.string.welcome_title_board));
                break;

            case R.id.todo:
                fragmentInterfaceListener.onStatusButtonClicked(Card.CardStatus.TODO);
                helloNote.setText(getString(R.string.welcome_title_todo));
                break;

            case R.id.inprogress:
                fragmentInterfaceListener.onStatusButtonClicked(Card.CardStatus.INPROGRESS);
                helloNote.setText(getString(R.string.welcome_title_inprogress));
                break;

            case R.id.completed:
                fragmentInterfaceListener.onStatusButtonClicked(Card.CardStatus.COMPLETED);
                helloNote.setText(getString(R.string.welcome_title_completed));
                break;
        }
    }


    private void showDeleteAllWarningDialog() {

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Are you sure you want to delete all your tasks?");
        build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaskManager.getInstance().removeAllTasks();
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

    public OnMainViewsClickListener fragmentInterfaceListener;

    public interface OnMainViewsClickListener {

        void onDeleteClicked();

        void onPriorityButtonClicked(Card.CardPriority cardPriority);

        void onStatusButtonClicked(Card.CardStatus cardStatus);
    }

}

