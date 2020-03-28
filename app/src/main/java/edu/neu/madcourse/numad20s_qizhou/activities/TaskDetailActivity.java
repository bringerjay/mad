package edu.neu.madcourse.numad20s_qizhou.activities;
import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import edu.neu.madcourse.numad20s_qizhou.KanbanApplication;
import edu.neu.madcourse.numad20s_qizhou.R;
import edu.neu.madcourse.numad20s_qizhou.managers.TaskManager;
import edu.neu.madcourse.numad20s_qizhou.model.Card;
import edu.neu.madcourse.numad20s_qizhou.utils.BitmapTools;
import java.util.Calendar;
import java.util.UUID;
import edu.neu.madcourse.numad20s_qizhou.model.Task;
import static edu.neu.madcourse.numad20s_qizhou.R.drawable.rounded_background;
import static edu.neu.madcourse.numad20s_qizhou.fragments.TasksFragment.EXISTING_ID;

public class TaskDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Spinner taskStatusDropdownBar, taskPriorityDropdownBar;
    private ImageView photo, closeTask, saveTask, taskDateTimePicker, editTask, displayImage;
    private TextView taskTitle, taskDescription, taskDate, taskTime;
    private Task itemTask = null;
    private int year, month, day, hour, minute;
    private String timeSelected;
    private StringBuilder dateSelected;
    private LinearLayout spinnerStatusLayout, spinnerPriorityLayout;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 123;
    private String selectedImagePath = null;
    private String existingTaskId = null;
    private Boolean isUserClickedExistingTask = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);

        if (getIntent().getExtras() != null) {
            existingTaskId = getIntent().getExtras().getString(EXISTING_ID, null);
            isUserClickedExistingTask = getIntent().getExtras().getBoolean("isUserClickedExistingTask", false);
        }

        initViews();
        initListeners();
        initSpinner();

        if (existingTaskId != null) {
            showExistingTask();
            enableViews(false);
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case SELECT_FILE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, SELECT_FILE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                displayImage.setImageBitmap(bmp);

            } else if (requestCode == SELECT_FILE) {

                //data.getData return the content URI for the selected Image
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //Gets the String value in the column
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                selectedImagePath = imgDecodableString;
                displayImage.setImageBitmap(BitmapTools.decodeSampledBitmapFromPath(selectedImagePath, 100, 100));
            }
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

    //  endregion


    //
    // region helpers
    //

    private void enableViews(boolean enable) {

        if (enable) {

            taskTitle.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {

                fieldEditMode(taskTitle, true);
                fieldEditMode(taskDescription, true);
                fieldEditMode(taskStatusDropdownBar, true);
                fieldEditMode(taskPriorityDropdownBar, true);
                fieldEditMode(photo, true);
                fieldEditMode(taskDateTimePicker, true);

                editTask.setVisibility(View.GONE);
                saveTask.setVisibility(View.VISIBLE);
            }

        } else {

            fieldEditMode(taskTitle, false);
            fieldEditMode(taskDescription, false);
            fieldEditMode(taskStatusDropdownBar, false);
            fieldEditMode(taskPriorityDropdownBar, false);
            fieldEditMode(photo, false);
            fieldEditMode(taskDateTimePicker, false);

            editTask.setVisibility(View.VISIBLE);
            saveTask.setVisibility(View.GONE);
        }
    }

    public void showExistingTask() {

        if (existingTaskId == null) return;

        itemTask = TaskManager.getInstance().getTaskWithId(existingTaskId);

        if (itemTask == null) return;

        dateSelected = itemTask.taskDate;
        timeSelected = itemTask.taskTime;
        selectedImagePath = itemTask.taskImagePath;

        taskTitle.setText(itemTask.taskTitle);
        taskDescription.setText(itemTask.taskDescription);
        taskDate.setText(dateSelected);
        taskTime.setText(timeSelected);
        taskPriorityDropdownBar.setSelection(TaskManager.getInstance().getPriorityNumber(itemTask.cardPriority));
        taskStatusDropdownBar.setSelection(TaskManager.getInstance().getStatusNumber(itemTask.cardStatus));
        displayImage.setImageBitmap(BitmapTools.decodeSampledBitmapFromPath(selectedImagePath, 100, 100));
    }

    //Todo complex
    public void fieldEditMode(View id, Boolean isEditMode) {
        if (id == taskTitle || id == taskDescription) {
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
        } else if (id == taskStatusDropdownBar || id == taskPriorityDropdownBar) {
            id.setEnabled(isEditMode);

            if (!isEditMode) {
                id.setBackgroundResource(R.color.white);
                id.setBackgroundResource(rounded_background);
            } else {
                spinnerStatusLayout.setBackgroundResource(R.color.verylightGray);
                spinnerPriorityLayout.setBackgroundResource(R.color.verylightGray);
                id.setBackgroundResource(R.color.verylightGray);
                id.setBackgroundResource(rounded_background);

            }
        } else if (id == photo || id == taskDateTimePicker) {
            id.setEnabled(isEditMode);
        }
    }

    private void SelectImage() {

        final CharSequence[] items = {/*"Camera", */"Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetailActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    if (ActivityCompat.checkSelfPermission(TaskDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(TaskDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_FILE);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, SELECT_FILE);
                    }

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }

            }
        });
        builder.show();
    }


    void initViews(){
        photo = findViewById(R.id.task_image_bar);
        taskTitle = findViewById(R.id.task_title_view);
        taskDescription = findViewById(R.id.task_description_view);
        taskDateTimePicker = findViewById(R.id.date_time_bar);
        taskStatusDropdownBar = (Spinner) findViewById(R.id.task_status_dropdown);
        taskPriorityDropdownBar = (Spinner) findViewById(R.id.task_priority_dropdown);
        saveTask = findViewById(R.id.save_task_bar);
        closeTask = findViewById(R.id.close_task_bar);
        editTask = findViewById(R.id.edit_task_bar);
        taskDate = findViewById(R.id.task_date_view);
        taskTime = findViewById(R.id.task_time_view);
        displayImage = (ImageView) findViewById(R.id.selected_image);
        spinnerStatusLayout = (LinearLayout) findViewById(R.id.spinner_status);
        spinnerPriorityLayout = (LinearLayout) findViewById(R.id.spinner_priotity);
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

                // TODO date and others
                String title = taskTitle.getText().toString();
                String details = taskDescription.getText().toString();
                String date = taskDate.getText().toString();
                String time = taskTime.getText().toString();
                Card.CardPriority priority = (Card.CardPriority) taskPriorityDropdownBar.getSelectedItem();
                Card.CardStatus status = (Card.CardStatus) taskStatusDropdownBar.getSelectedItem();

                if ((title.isEmpty())) {
                        Toast.makeText(TaskDetailActivity.this, "Please enter the task title at least!", Toast.LENGTH_SHORT).show();
                        return;
                }

                if (existingTaskId != null) {

                    itemTask.taskTitle = title;
                    itemTask.taskDescription = details;
                    itemTask.cardPriority = priority;
                    itemTask.cardStatus = status;
                    itemTask.taskDate = dateSelected;
                    itemTask.taskTime = timeSelected;
                    itemTask.taskImagePath = selectedImagePath;
                    TaskManager.getInstance().replaceExistingTaskItem(itemTask.taskId, itemTask);

                } else {

                    String id = UUID.randomUUID().toString(); // Must be called only once
                    itemTask = new Task(id, title, details, dateSelected, timeSelected, priority, status, selectedImagePath);
                    TaskManager.getInstance().addTaskItem(itemTask);
                }

                TaskManager.getInstance().saveTask(KanbanApplication.getInstance());
                Toast.makeText(TaskDetailActivity.this, "Task saved!"/* + itemTask.taskId*/, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
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
                        dateSelected = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day);
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

                //todo
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

    //drop down list to ask user where to add the task(todo , inprogress, completed)
    public void initSpinner() {
        ArrayAdapter<Card.CardStatus> adapterCardStatus = new ArrayAdapter<Card.CardStatus>(TaskDetailActivity.this,
                android.R.layout.simple_spinner_item, Card.CardStatus.values());

        adapterCardStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskStatusDropdownBar.setAdapter(adapterCardStatus);
        taskStatusDropdownBar.setOnItemSelectedListener(this);

        ArrayAdapter<Card.CardPriority> adapterCardPriority = new ArrayAdapter<Card.CardPriority>(TaskDetailActivity.this,
                android.R.layout.simple_spinner_item, Card.CardPriority.values());

        adapterCardPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskPriorityDropdownBar.setAdapter(adapterCardPriority);
        taskPriorityDropdownBar.setOnItemSelectedListener(this);
    }
}
