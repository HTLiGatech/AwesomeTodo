package com.jiuzhang.guojing.awesometodo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jiuzhang.guojing.awesometodo.AlarmReceiver;
import com.jiuzhang.guojing.awesometodo.TodoEditActivity;
import com.jiuzhang.guojing.awesometodo.models.Todo;

public class AlarmUtils {

    public static void setAlarm(@NonNull Context context, @NonNull Todo todo) {
        AlarmManager alarmMgr;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(TodoEditActivity.KEY_TODO, todo);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                                                               0,
                                                               intent,
                                                               PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, // will wake up the device
                     todo.remindDate.getTime(),
                     alarmIntent);
    }
}
