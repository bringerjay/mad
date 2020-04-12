package edu.neu.madcourse.numad20s_qizhou.model;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "members")
public class FamilyMember {

    @PrimaryKey
    @NonNull
    public String name;


    public FamilyMember(String name)
    {
        this.name = name;
    }
}
