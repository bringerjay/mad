package edu.neu.madcourse.numad20s_qizhou;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "link_table")
public class Link {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "link")
    private String link;

    public Link(@NonNull String link) {this.link = link;}

    public String getLink(){return this.link;}
}
