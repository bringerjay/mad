package edu.neu.madcourse.numad20s_qizhou.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @NonNull
    public String title;
    public String description;
    public String date;
    public String time;
    public String cardPriority;
    public String cardStatus;
    public String location;
    public String member;


    public Task(@NonNull String title, String description, String date, String time, String cardPriority, String cardStatus, String location, String member) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.cardPriority = cardPriority;
        this.cardStatus = cardStatus;
        this.location = location;
        this.member = member;
    }
}
