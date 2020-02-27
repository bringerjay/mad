package edu.neu.madcourse.numad20s_qizhou;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LinkDAO {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Link link);

    @Query("DELETE FROM link_table")
    void deleteAll();

    @Query("SELECT * from link_table ")
    LiveData<List<Link>> getAllLinks();

}
