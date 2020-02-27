package edu.neu.madcourse.numad20s_qizhou;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Link.class}, version = 1, exportSchema = false)
public abstract class LinkRoomDatabase extends RoomDatabase {
    public abstract LinkDAO linkDAO();
    private static volatile LinkRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static LinkRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LinkRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LinkRoomDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
