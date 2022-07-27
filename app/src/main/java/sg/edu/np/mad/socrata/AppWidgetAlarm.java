package sg.edu.np.mad.socrata;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AppWidgetAlarm {
    private final int ALARM_ID = 0;
    private final int INTERVAL_MILLIS = 10000;

    private Context mContext;

    public AppWidgetAlarm(Context context)
    {
        mContext = context;

    }


    public void startAlarm()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS);

        Intent alarmIntent=new Intent(mContext, NewAppWidget.class);
        alarmIntent.setAction(NewAppWidget.ACTION_AUTO_UPDATE);
        //alarmIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.EXTRA_APPWIDGET_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_MILLIS, pendingIntent);
    }


    public void stopAlarm()
    {
        Intent alarmIntent = new Intent(NewAppWidget.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
