package edu.neu.madcourse.numad20s_qizhou.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import edu.neu.madcourse.numad20s_qizhou.R;
import edu.neu.madcourse.numad20s_qizhou.model.Task;
import edu.neu.madcourse.numad20s_qizhou.repos.CardViewModel;
import edu.neu.madcourse.numad20s_qizhou.repos.MemberViewModel;
import edu.neu.madcourse.numad20s_qizhou.repos.TaskViewModel;

import static edu.neu.madcourse.numad20s_qizhou.R.drawable.rounded_background;
import static edu.neu.madcourse.numad20s_qizhou.fragments.TasksFragment.EXISTING_ID;

public class TaskDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Spinner taskStatusDropdownBar, taskPriorityDropdownBar, taskAssignmentDropdownBar;
    private ImageView closeTask, saveTask, taskDateTimePicker, editTask;
    private TextView taskTitle, taskDescription, taskDate, taskTime, taskLocation;
    private Task itemTask = null;
    private int year, month, day, hour, minute;
    private String timeSelected;
    private String dateSelected;
    private LinearLayout spinnerStatusLayout, spinnerPriorityLayout, spinnerAssignmentLayout;
    private Integer existingTaskId = null;
    private Boolean isUserClickedExistingTask = false;
    private CardViewModel cardViewModel;
    private TaskViewModel taskViewModel;
    private MemberViewModel memberViewModel;

    public TaskDetailActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        if (getIntent().getExtras() != null) {
            existingTaskId = getIntent().getExtras().getInt(EXISTING_ID);
            isUserClickedExistingTask = getIntent().getExtras().getBoolean("isUserClickedExistingTask", false);
        }
        initViews();
        initListeners();
        initSpinner();

        if (existingTaskId != null) {
            try {
                showExistingTask();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            enableViews(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected //todo
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the third item gets selected
                break;
            case 3:
                // Whatever you want to happen when the third item gets selected
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        timeSelected = String.format("%02d:%02d %s", hour, minute, hourOfDay < 12 ? "am" : "pm");
        taskTime.setText(timeSelected);
    }

    private void enableViews(boolean enable) {
        if (enable) {
            taskTitle.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                fieldEditMode(taskTitle, true);
                fieldEditMode(taskLocation, true);
                fieldEditMode(taskDescription, true);
                fieldEditMode(taskStatusDropdownBar, true);
                fieldEditMode(taskPriorityDropdownBar, true);
                fieldEditMode(taskAssignmentDropdownBar, true);
                fieldEditMode(taskDateTimePicker, true);
                editTask.setVisibility(View.GONE);
                saveTask.setVisibility(View.VISIBLE);
            }
        } else {
            fieldEditMode(taskTitle, false);
            fieldEditMode(taskLocation, false);
            fieldEditMode(taskDescription, false);
            fieldEditMode(taskStatusDropdownBar, false);
            fieldEditMode(taskPriorityDropdownBar, false);
            fieldEditMode(taskAssignmentDropdownBar, false);
            fieldEditMode(taskDateTimePicker, false);
            editTask.setVisibility(View.VISIBLE);
            saveTask.setVisibility(View.GONE);
        }
    }

    public void showExistingTask() throws ExecutionException, InterruptedException {
        if (existingTaskId == null) return;
        itemTask = taskViewModel.getTaskById(existingTaskId);
        if (itemTask == null) return;
        dateSelected = itemTask.date;
        timeSelected = itemTask.time;
        taskTitle.setText(itemTask.title);
        taskDescription.setText(itemTask.description);
        taskDate.setText(dateSelected);
        taskTime.setText(timeSelected);
        taskLocation.setText(itemTask.location);
        taskPriorityDropdownBar.setSelection(getPriorityNumber(itemTask.cardPriority));
        taskStatusDropdownBar.setSelection(getStatusNumber(itemTask.cardStatus));
        taskAssignmentDropdownBar.setSelection(getUserNumber(itemTask.member));
    }

    public int getPriorityNumber(String cardPriority) {
        int itemSelected = 0;
        if(cardPriority.equals("LOW"))
            itemSelected = 0;
        else if(cardPriority.equals("MEDIUM"))
            itemSelected = 1;
        else if(cardPriority.equals("HIGH"))
            itemSelected = 2;
        return itemSelected;
    }

    public int getStatusNumber(String cardStatus) {
        int itemSelected = 0;
        if(cardStatus.equals("INCOMING"))
            itemSelected = 0;
        else if(cardStatus.equals("INPROGRESS"))
            itemSelected = 1;
        else if(cardStatus.equals("COMPLETED"))
            itemSelected = 2;
        return itemSelected;
    }


    public int getUserNumber(String user) {
        int itemSelected = 0;
        if(user.equals("Nobody"))
            itemSelected = 0;
        else if(user.equals("Dad"))
            itemSelected = 1;
        else if(user.equals("Mom"))
            itemSelected = 2;
        else if(user.equals("Kid"))
            itemSelected = 3;
        return itemSelected;
    }

    public void fieldEditMode(View id, Boolean isEditMode) {
        if (id == taskTitle || id == taskDescription || id == taskLocation) {
            id.setFocusable(isEditMode);
            id.setEnabled(isEditMode);
            id.setClickable(isEditMode);
            id.setFocusableInTouchMode(isEditMode);
            id.requestFocus();
            if (!isEditMode) {
                id.setBackgroundResource(R.color.white);
                id.setBackgroundResource(rounded_background);
            } else {
                id.setBackgroundResource(R.color.verylightGray);
                id.setBackgroundResource(rounded_background);
            }
        } else if (id == taskStatusDropdownBar || id == taskPriorityDropdownBar ||
        id == taskAssignmentDropdownBar) {
            id.setEnabled(isEditMode);
            if (!isEditMode) {
                id.setBackgroundResource(R.color.white);
                id.setBackgroundResource(rounded_background);
            } else {
                spinnerStatusLayout.setBackgroundResource(R.color.lightPurple);
                spinnerPriorityLayout.setBackgroundResource(R.color.lightPurple);
                spinnerAssignmentLayout.setBackgroundResource(R.color.lightPurple);
                id.setBackgroundResource(R.color.verylightGray);
            }
        } else if (id == taskDateTimePicker) {
            id.setEnabled(isEditMode);
        }
    }


    void initViews(){
        taskTitle = findViewById(R.id.task_title_view);
        taskDescription = findViewById(R.id.task_description_view);
        taskDateTimePicker = findViewById(R.id.date_time_bar);
        taskStatusDropdownBar = (Spinner) findViewById(R.id.task_status_dropdown);
         taskPriorityDropdownBar = (Spinner) findViewById(R.id.task_priority_dropdown);
        taskAssignmentDropdownBar = (Spinner) findViewById(R.id.task_assignment_dropdown);
        saveTask = findViewById(R.id.save_task_bar);
        closeTask = findViewById(R.id.close_task_bar);
        editTask = findViewById(R.id.edit_task_bar);
        taskDate = findViewById(R.id.task_date_view);
        taskTime = findViewById(R.id.task_time_view);
        taskLocation = findViewById(R.id.location_input);
        spinnerStatusLayout = (LinearLayout) findViewById(R.id.spinner_status);
        spinnerPriorityLayout = (LinearLayout) findViewById(R.id.spinner_priotity);
        spinnerAssignmentLayout = (LinearLayout) findViewById(R.id.spinner_assignment);
    }

    void initListeners(){
        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableViews(true);
            }
        });
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString();
                String details = taskDescription.getText().toString();
                String date = taskDate.getText().toString();
                String time = taskTime.getText().toString();
                String location = taskLocation.getText().toString();
                String taskLocationData = "TBD";
                if(location!=null){
                    taskLocationData = location;
                }
                String priority = (String) taskPriorityDropdownBar.getSelectedItem();
                String status = (String) taskStatusDropdownBar.getSelectedItem();
                String user = (String) taskAssignmentDropdownBar.getSelectedItem();
                if ((title.isEmpty())) {
                        Toast.makeText(TaskDetailActivity.this, "Please enter the task title at least!", Toast.LENGTH_SHORT).show();
                        return;
                }
                if (existingTaskId != null) {
                    itemTask.title = title;
                    itemTask.description = details;
                    itemTask.cardPriority = priority;
                    itemTask.cardStatus = status;
                    itemTask.date = dateSelected;
                    itemTask.time = timeSelected;
                    itemTask.member = user;
                    itemTask.location = taskLocationData;
                    taskViewModel.updateTask(itemTask);
                } else {
                    itemTask = new Task(title,details,dateSelected,timeSelected,priority,status,taskLocationData,user);
                    taskViewModel.insert(itemTask);
                }
                Toast.makeText(TaskDetailActivity.this, "Task saved!"/* + itemTask.id*/, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        taskDateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (year == 0 || month == 0 || day == 0) {
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(TaskDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        dateSelected = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).toString();
                        taskDate.setText(dateSelected);
                        Calendar calendar = Calendar.getInstance();
                        hour = calendar.get(Calendar.HOUR_OF_DAY);
                        minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetailActivity.this, TaskDetailActivity.this,
                                hour, minute, DateFormat.is24HourFormat(TaskDetailActivity.this));
                        timePickerDialog.show();
                    }
                }, year, month, day);

                mDatePicker.getDatePicker();
                mDatePicker.show();
            }
        });

        closeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString();
                String description = taskDescription.getText().toString();
                if ((title.isEmpty()) & (description.isEmpty())) {
                    finish();
                } else if (isUserClickedExistingTask) {
                    finish();
                } else {
                    AlertDialog.Builder build = new AlertDialog.Builder(TaskDetailActivity.this);
                    build.setTitle("Discard data entered?");
                    build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
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
            }
        });

    }

    public void initSpinner() {
        ArrayList<String> cardStatusArray = new ArrayList<>();
        ArrayList<String> cardPriorityArray = new ArrayList<>();
        ArrayList<String> taskAssignmentArray = new ArrayList<>();
        cardStatusArray.add("INCOMING");
        cardStatusArray.add("INPROGRESS");
        cardStatusArray.add("COMPLETED");
        cardPriorityArray.add("HIGH");
        cardPriorityArray.add("MEDIUM");
        cardPriorityArray.add("LOW");
        taskAssignmentArray.add("Anyone");
        taskAssignmentArray.add("Dad");
        taskAssignmentArray.add("Mom");
        taskAssignmentArray.add("Kid");
        ArrayAdapter<String> adapterCardStatus = new ArrayAdapter<String>(TaskDetailActivity.this,
                android.R.layout.simple_spinner_item, cardStatusArray);
        adapterCardStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskStatusDropdownBar.setAdapter(adapterCardStatus);
        taskStatusDropdownBar.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapterCardPriority = new ArrayAdapter<String>(TaskDetailActivity.this,
                android.R.layout.simple_spinner_item, cardPriorityArray);
        adapterCardPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterCardAssignment = new ArrayAdapter<String>(TaskDetailActivity.this,
                android.R.layout.simple_spinner_item, taskAssignmentArray);
        adapterCardPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskPriorityDropdownBar.setAdapter(adapterCardPriority);
        taskPriorityDropdownBar.setOnItemSelectedListener(this);
        taskAssignmentDropdownBar.setAdapter(adapterCardAssignment);
        taskAssignmentDropdownBar.setOnItemSelectedListener(this);
    }
}
