package edu.neu.madcourse.numad20s_qizhou.repos;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.neu.madcourse.numad20s_qizhou.model.Task;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> incomingTasks;
    private LiveData<List<Task>> inprogressTasks;
    private LiveData<List<Task>> completedTasks;


    TaskRepository(Application application) {
        KanBanDatabase db = KanBanDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
        incomingTasks = taskDao.getTasksByStatus("INCOMING");
        inprogressTasks = taskDao.getTasksByStatus("INPROGRESS") ;
        completedTasks = taskDao.getTasksByStatus("COMPLETED");
    }

    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    void insert(Task task) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task);
        });
    }


    public Task getTaskById(Integer id) throws ExecutionException, InterruptedException {
        Callable<Task> callable = new Callable<Task>() {
            @Override
            public Task call() throws Exception {
                return taskDao.getTaskById(id);
            }
        };
        Future<Task> future = Executors.newSingleThreadExecutor().submit(callable);
        Task card = null;
        try{ card = future.get();} catch (ExecutionException e) {
            e.printStackTrace();
            throw new ExecutionException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InterruptedException();
        }
        return card;
    }

    public void deleteAll() {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteAll();
        });
    }

    public void deleteTaskById(Integer id) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteById(id);
        });
    }

    public void updateTask(Task task) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.update(task);
        });
    }

    public LiveData<List<Task>> getIncomingTasks() {
        return incomingTasks;
    }

    public LiveData<List<Task>> getInprogressTasks() {
        return inprogressTasks;
    }

    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }
}
