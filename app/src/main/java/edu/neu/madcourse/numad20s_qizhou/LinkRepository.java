package edu.neu.madcourse.numad20s_qizhou;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LinkRepository {
    private LinkDAO linkDAO;
    private LiveData<List<Link>> allLinks;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    LinkRepository(Application application) {
        LinkRoomDatabase db = LinkRoomDatabase.getDatabase(application);
        linkDAO = db.linkDAO();
        allLinks = linkDAO.getAllLinks();
    }
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Link>> getAllLinks() {
        return allLinks;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Link link) {
        LinkRoomDatabase.databaseWriteExecutor.execute(() -> {
            linkDAO.insert(link);
        });
    }
}
