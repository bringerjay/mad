package edu.neu.madcourse.numad20s_qizhou.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.neu.madcourse.numad20s_qizhou.model.Card;

@Dao
public interface CardDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Card card);

    @Query("DELETE FROM card")
    void deleteAll();

    @Query("SELECT * from card ")
    LiveData<List<Card>> getAllCards();

    @Query("SELECT * FROM card WHERE id = :id")
    Card getCardById(Integer id);

    @Query("DELETE FROM card WHERE id = :id")
    void deleteById(Integer id);

    @Update
    void update(Card card);

    @Query("SELECT * FROM card WHERE taskId = :id")
    Card getCardByTaskId(Integer id);
}
