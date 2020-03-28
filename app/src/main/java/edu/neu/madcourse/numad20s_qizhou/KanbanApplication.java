package edu.neu.madcourse.numad20s_qizhou;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import edu.neu.madcourse.numad20s_qizhou.managers.TaskManager;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class KanbanApplication extends Application {

    private Activity mActivity;

    private static KanbanApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        TaskManager.getInstance().initialize();

        registerActivityLifecycleCallbacks(activityCallback);
    }


    ActivityLifecycleCallbacks activityCallback =  new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mActivity = activity;
            Log.d(TAG, "onActivityResumed:" + activity.getLocalClassName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            mActivity = null;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            //TaskManager.getInstance().saveTask(instance);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    public static KanbanApplication getInstance() {
        return instance;
    }

}
