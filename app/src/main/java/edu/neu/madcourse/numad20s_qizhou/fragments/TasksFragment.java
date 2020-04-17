package edu.neu.madcourse.numad20s_qizhou.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.numad20s_qizhou.R;
import edu.neu.madcourse.numad20s_qizhou.activities.MainActivity;
import edu.neu.madcourse.numad20s_qizhou.activities.TaskDetailActivity;
import edu.neu.madcourse.numad20s_qizhou.model.Card;
import edu.neu.madcourse.numad20s_qizhou.model.Task;
import edu.neu.madcourse.numad20s_qizhou.repos.CardViewModel;
import edu.neu.madcourse.numad20s_qizhou.repos.TaskViewModel;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static androidx.core.content.PermissionChecker.checkSelfPermission;


public class TasksFragment extends Fragment implements MainActivity.OnMainViewsClickListener {

    private RecyclerView tasksRecyclerView;
    private Context taskFragmentContext;
    private List<Task> itemList;
    private List<Task> allTaskList;
    private List<Task> incomingTaskList;
    private List<Task> inprogressTaskList;
    private List<Task> completedTaskList;
    public static final String EXISTING_ID = "existingTaskId";
    public static final String IS_EXISTING_TASK = "isUserClickedExistingTask";
    private boolean isKanbanPage = false;
    private TaskViewModel taskViewModel;
    private CardViewModel cardViewModel;
    private String statusFilterSelected;
    LocationManager lm;
    Location location;
    LocationListener locationListener;
    double longitude;
    double latitude;
    String latestLocation;
    MainActivity mainActivity;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public TasksFragment(TaskViewModel taskViewModel, CardViewModel cardViewModel, LocationManager lm, MainActivity mainActivity) {
        this.taskViewModel = taskViewModel;
        this.cardViewModel = cardViewModel;
        this.itemList = new ArrayList<>();
        this.allTaskList = new ArrayList<>();
        this.incomingTaskList = new ArrayList<>();
        this.inprogressTaskList = new ArrayList<>();
        this.completedTaskList = new ArrayList<>();
        this.statusFilterSelected = "ALL";
        this.lm = lm;
        this.mainActivity = mainActivity;
        requestPermissions();
        this.locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                String stringLogi = String.format("%.2f", longitude);
                String stringLati = String.format("%.2f", latitude);
                latestLocation ="location\n" + stringLogi + "," + stringLati;
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
    }

    private void initDatabase() {
        Task initTask1 = new Task("Create tasks", "Try creating some tasks",
                "2020-4-22", "19:23 pm", "HIGH", "INCOMING", latestLocation, "Dad");
        Task initTask2 = new Task("Plan your stuff", "Try add more details",
                "2020-4-22", "19:23 pm", "HIGH", "INCOMING", latestLocation, "Mom");
        Task initTask3 = new Task("Try out the app", "Play with the app all you like",
                "2020-4-22", "19:23 pm", "HIGH", "INPROGRESS", latestLocation, "Kid");
        Task initTask4 = new Task("Find some bugs", "Lets treasure hunt the bugs",
                "2020-4-22", "19:23 pm", "MEDIUM", "INPROGRESS", latestLocation, "Mom");
        Task initTask5 = new Task("Test out the functions", "Play with some functions enabled by the app.",
                "2020-4-22", "19:23 pm", "MEDIUM", "INPROGRESS", latestLocation, "Kid");
        Task initTask6 = new Task("Install the app", "Build the app on your device",
                "2020-4-22", "19:23 pm", "LOW", "COMPLETED", latestLocation, "Mom");
        Task initTask7 = new Task("Open the app", "Get in the app to take a look",
                "2020-4-22", "19:23 pm", "LOW", "COMPLETED", latestLocation, "Dad");
        taskViewModel.insert(initTask1);
        taskViewModel.insert(initTask2);
        taskViewModel.insert(initTask3);
        taskViewModel.insert(initTask4);
        taskViewModel.insert(initTask5);
        taskViewModel.insert(initTask6);
        taskViewModel.insert(initTask7);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static TasksFragment newInstance(boolean isKanbanPage, TaskViewModel taskViewModel,
                                            CardViewModel cardViewModel, LocationManager lm, MainActivity mainActivity) {
        TasksFragment myFragment = new TasksFragment(taskViewModel, cardViewModel, lm, mainActivity);
        Bundle args = new Bundle();
        args.putBoolean("KANBAN", isKanbanPage);
        myFragment.setArguments(args);
        return myFragment;
    }

    private void notifyUpdates(String status) {
        if (status.equals(statusFilterSelected)) {
            System.out.println("notifying " + status);
            switch (status) {
                case "ALL":
                    this.itemList = this.allTaskList;
                    break;
                case "INCOMING":
                    this.itemList = this.incomingTaskList;
                    break;
                case "INPROGRESS":
                    this.itemList = this.inprogressTaskList;
                    break;
                case "COMPLETED":
                    this.itemList = this.completedTaskList;
                    break;
            }
            tasksRecyclerView.getAdapter().notifyDataSetChanged();
        }
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
                //initDatabase();
            }
            return;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        taskFragmentContext = context;
        ((MainActivity) getActivity()).fragmentInterfaceListener = this;
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isKanbanPage = getArguments() != null && getArguments().getBoolean("KANBAN", false);
        if (checkSelfPermission(mainActivity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude_init = location.getLongitude();
        double latitude_init = location.getLatitude();
        String stringLogi = String.format("%.2f", longitude_init);
        String stringLati = String.format("%.2f", latitude_init);
        latestLocation ="location\n" + stringLogi + "," + stringLati;
        //initDatabase();
        System.out.println("location" + latestLocation);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions() {
        System.out.println("Requesting permission");
        ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1111);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_fragment, container, false);
        tasksRecyclerView = view.findViewById(R.id.tasksList);
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                System.out.println("updating" + tasks.size());
                System.out.println(allTaskList.size());
                if(tasks.size()<1){
                    allTaskList = new ArrayList<>();
                }else {
                    System.out.println("refreshing");
                    ArrayList<Task> taskListCopy = new ArrayList<>();
                    taskListCopy.addAll(tasks);
                    allTaskList = taskListCopy;
                }
                System.out.println(allTaskList.size());
                notifyUpdates("ALL");
            }
        });
        taskViewModel.getIncomingTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                System.out.println("updating incoming" + tasks.size());
                System.out.println(incomingTaskList.size());
                if(tasks.size()<1){
                    incomingTaskList = new ArrayList<>();
                }else {
                    System.out.println("refreshing incoming");
                    ArrayList<Task> taskListCopy = new ArrayList<>();
                    taskListCopy.addAll(tasks);
                    incomingTaskList = taskListCopy;
                }
                System.out.println(incomingTaskList.size());
                notifyUpdates("INCOMING");
            }
        });
        taskViewModel.getInprogressTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                System.out.println("updating inprogress " + tasks.size());
                System.out.println(inprogressTaskList.size());
                if(tasks.size()<1){
                    inprogressTaskList = new ArrayList<>();
                }else {
                    System.out.println("refreshing inprogress");
                    ArrayList<Task> taskListCopy = new ArrayList<>();
                    taskListCopy.addAll(tasks);
                    inprogressTaskList = taskListCopy;
                }
                System.out.println(inprogressTaskList.size());
                notifyUpdates("INPROGRESS");
            }
        });
        taskViewModel.getCompletedTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                System.out.println("updating completed" + tasks.size());
                System.out.println(completedTaskList.size());
                if(tasks.size()<1){
                    completedTaskList = new ArrayList<>();
                }else {
                    System.out.println("refreshing completed");
                    ArrayList<Task> taskListCopy = new ArrayList<>();
                    taskListCopy.addAll(tasks);
                    completedTaskList = taskListCopy;
                }
                System.out.println(completedTaskList.size());
                notifyUpdates("COMPLETED");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView();
        tasksRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resume list....");
        System.out.println(itemList.size());
        tasksRecyclerView.getAdapter().notifyDataSetChanged();
        System.out.println(itemList.size());
    }

    @Override
    public void onDeleteClicked() {
        System.out.println("Deleted clicked.");
        tasksRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onPriorityButtonClicked(String cardPriority) {

    }

    @Override
    public void onNavigationButtonClicked(String cardStatus) {
        this.statusFilterSelected = cardStatus;
        System.out.println("Navigation button clicked." + cardStatus);
        notifyUpdates(cardStatus);
    }


    private void initializeRecyclerView() {
        // Create an instance of SectionedRecyclerViewAdapter
        final SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        // Add your Sections
        sectionAdapter.addSection(new MySection());
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(taskFragmentContext, LinearLayoutManager.VERTICAL, false));
        tasksRecyclerView.setAdapter(sectionAdapter);
    }

    class MySection extends StatelessSection {

        public MySection() {
            // call constructor with layout resources for this Section header and items
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.tasks_item)
                    .build());
        }

        @Override
        public int getContentItemsTotal() {
            return itemList.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom instance of ViewHolder for the items of this section
            return new MyItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

            // bind your view here
            try {
                itemHolder.hydrate(itemList.get(position));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskTitle;
        private final TextView taskDescription;
        private final TextView taskDate;
        private final TextView taskTime;
        private final TextView taskLocation;
        private final TextView taskMember;
        private final TextView taskCommute;


        private View view = null;

        public MyItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            taskTitle = itemView.findViewById(R.id.title);
            taskDescription = itemView.findViewById(R.id.description);
            taskDate = (TextView) itemView.findViewById(R.id.task_item_date);
            taskTime = (TextView) itemView.findViewById(R.id.task_item_time);
            taskLocation = (TextView) itemView.findViewById(R.id.location);
            taskMember = (TextView) itemView.findViewById(R.id.user);
            taskCommute = (TextView) itemView.findViewById(R.id.commute_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(taskFragmentContext, TaskDetailActivity.class);
                    intent.putExtra(EXISTING_ID, itemList.get(getAdapterPosition()).id);
                    intent.putExtra(IS_EXISTING_TASK, true);
                    startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteWarningDialog(getAdapterPosition());
                    return false;
                }
            });

        }

        public void hydrate(Task task) throws ExecutionException, InterruptedException {
            taskTitle.setText(task.title);
            if (task.description==null){
                taskDescription.setText("The task was quick created.");
            }else {
                taskDescription.setText(task.description);
            }
            taskDate.setText(task.date);
            taskTime.setText(task.time);
            System.out.println(task.date);
            System.out.println(task.time);
            taskLocation.setText(task.location);
            String member = "Anyone";
            if (task != null){
                member = task.member.toUpperCase();
            }
                taskMember.setText(member);
            Location destination = new Location("");
            if (location!=null){
            double minLog = location.getLongitude()-0.1;
            double maxLog = location.getLongitude()+0.1;
            double minLat = location.getLatitude()-0.1;
            double maxLat = location.getLatitude()+0.1;
            Random r = new Random();
            double randomLat = minLat + r.nextFloat() * (maxLat - minLat);
            double randomLog = minLog + r.nextFloat() * (maxLog - minLog);
            destination.setLatitude(randomLat);
            destination.setLongitude(randomLog);
            Float distance = location.distanceTo(destination);
            String commuteTime = String.format("%.2f", distance/64373.8*60);
            String mins = "Commute Time:\n" + commuteTime + " mins";
            taskCommute.setText(mins);}
            if(view!=null && isKanbanPage){
                switch (task.cardStatus) {
                    case "INCOMING": view.setBackground(getResources().getDrawable(R.drawable.incoming_background));
                        break;
                    case "INPROGRESS": view.setBackground(getResources().getDrawable(R.drawable.inprogress_background));
                        break;
                    case "COMPLETED": view.setBackground(getResources().getDrawable(R.drawable.completed_background));
                        break;
                }
            }
        }
    }

    private void showDeleteWarningDialog(final int adapterPosition) {
        if ( isVisible() ) {

            AlertDialog.Builder build= new AlertDialog.Builder(taskFragmentContext);
            build.setTitle("Are you sure you want to delete this task?");
            build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    taskViewModel.deleteTaskById(itemList.get(adapterPosition).id);
                    tasksRecyclerView.getAdapter().notifyDataSetChanged();
                }
            });
            build.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog= build.create();
            dialog.show();
        }
    }
}
