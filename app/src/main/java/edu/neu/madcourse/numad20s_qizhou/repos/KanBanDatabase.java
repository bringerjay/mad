package edu.neu.madcourse.numad20s_qizhou.repos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.neu.madcourse.numad20s_qizhou.model.Card;
import edu.neu.madcourse.numad20s_qizhou.model.FamilyMember;
import edu.neu.madcourse.numad20s_qizhou.model.Task;

@Database(entities = {Card.class, FamilyMember.class, Task.class}, version = 2, exportSchema = false)
public abstract class KanBanDatabase extends RoomDatabase {
    public abstract CardDAO cardDAO();
    public abstract MemberDAO memberDAO();
    public abstract TaskDao taskDao();
    private static volatile KanBanDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static KanBanDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (KanBanDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            KanBanDatabase.class, "kanban_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}