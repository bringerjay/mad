package edu.neu.madcourse.numad20s_qizhou.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.neu.madcourse.numad20s_qizhou.model.Task;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("DELETE FROM tasks")
    void deleteAll();

    @Query("SELECT * from tasks ")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getTaskById(Integer id);

    @Query("DELETE FROM tasks WHERE id = :id")
    void deleteById(Integer id);

    @Update
    void update(Task task);

    @Query("SELECT * FROM tasks WHERE cardStatus = :status")
    LiveData<List<Task>> getTasksByStatus(String status);

}
