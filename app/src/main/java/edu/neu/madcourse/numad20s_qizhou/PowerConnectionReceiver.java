package edu.neu.madcourse.numad20s_qizhou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Charging state changed.");
        System.out.println(intent.getAction());
        String msg;
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
           msg = "Charging!";
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")){
            msg = "Not Charging!";
            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
    }
}
