package edu.neu.madcourse.numad20s_qizhou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Intent triggered");
        String time = "Alarm is on. It is ";
        Date currentTime = Calendar.getInstance().getTime();
        time = time + currentTime.toString() + " now.";
        System.out.println(time);
        Toast.makeText(context,time,Toast.LENGTH_LONG).show();
    }
}
