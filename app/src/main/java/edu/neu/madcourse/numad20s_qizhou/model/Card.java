package edu.neu.madcourse.numad20s_qizhou.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(tableName = "card",
        foreignKeys = {
        @ForeignKey(entity = FamilyMember.class,
        parentColumns = "name",
        childColumns = "familyMember",
        onDelete = SET_NULL),
        @ForeignKey(entity = Task.class,
        parentColumns = "id",
        childColumns = "taskId",
        onDelete = SET_NULL)
        })
public class Card {

 @PrimaryKey(autoGenerate = true)
 public int id;
 public String familyMember;
 public Integer taskId;
 public Boolean assignedStatus;
 public String cardPriority;
 public String cardStatus;


 public Card(String familyMember, Integer taskId, String cardPriority,
             String cardStatus, Boolean assignedStatus)
 {
     this.familyMember = familyMember;
     this.taskId = taskId;
     this.cardPriority = cardPriority;
     this.cardStatus = cardStatus;
     this.assignedStatus = assignedStatus;
 }
}

