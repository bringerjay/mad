package edu.neu.madcourse.numad20s_qizhou.repos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.neu.madcourse.numad20s_qizhou.model.FamilyMember;

@Dao
public interface MemberDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FamilyMember member);

    @Query("DELETE FROM members")
    void deleteAll();

    @Query("SELECT * from members")
    LiveData<List<FamilyMember>> getAllMembers();

    @Query("SELECT * FROM members WHERE name = :name")
    FamilyMember getMemberByName(String name);

    @Query("DELETE FROM members WHERE name = :name")
    void deleteByName(String name);

}
