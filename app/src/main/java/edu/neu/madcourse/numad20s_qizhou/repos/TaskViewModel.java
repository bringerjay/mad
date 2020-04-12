package edu.neu.madcourse.numad20s_qizhou.repos;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.neu.madcourse.numad20s_qizhou.model.Task;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> incomingTasks;
    private LiveData<List<Task>> inprogressTasks;
    private LiveData<List<Task>> completedTasks;


    public TaskViewModel (Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        if (allTasks == null) {
            allTasks = taskRepository.getAllTasks();
        }
        return allTasks; }

    public LiveData<List<Task>> getIncomingTasks() {
        if (incomingTasks == null) {
            incomingTasks = taskRepository.getIncomingTasks();
        }
        return incomingTasks; }

    public LiveData<List<Task>> getInprogressTasks() {
        if (inprogressTasks == null) {
            inprogressTasks = taskRepository.getInprogressTasks();
        }
        return inprogressTasks;
    }

    public LiveData<List<Task>> getCompletedTasks() {
        if (completedTasks == null) {
            completedTasks = taskRepository.getCompletedTasks();
        }
        return completedTasks;
    }

    public void insert(Task task) { taskRepository.insert(task); }

    public void deleteAll() {
        taskRepository.deleteAll();
    }

    public void deleteTaskById(Integer id) {
        taskRepository.deleteTaskById(id);
    }

    public Task getTaskById(Integer id) throws ExecutionException, InterruptedException {
        return taskRepository.getTaskById(id);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }


}
