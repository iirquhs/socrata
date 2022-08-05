package sg.edu.np.mad.socrata;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class AlarmManagerHelper {

    AlarmManager alarmManager;

    Context context;

    public AlarmManagerHelper(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public ZonedDateTime setMotivationalSettingAlarm(MotivationalQuoteSetting motivationalQuoteSetting) {
        Intent serviceIntent = new Intent(context.getApplicationContext(), MotivationBackgroundReceiver.class);

        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context, 1, serviceIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 1, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        ZonedDateTime currentLocalDateTime = ZonedDateTime.now();

        LocalTime localTime = motivationalQuoteSetting.getTimeOfDay();

        ZonedDateTime nextZonedDateTime = ZonedDateTime.of(currentLocalDateTime.getYear(),
                currentLocalDateTime.getMonth().getValue(),
                currentLocalDateTime.getDayOfMonth(), localTime.getHour(), localTime.getMinute(), 0, 0, ZoneId.systemDefault());

        int multiplier = motivationalQuoteSetting.getMultiplier();

        long interval = multiplier;

        switch (motivationalQuoteSetting.getFrequency()) {
            case "Hour":
                nextZonedDateTime = nextZonedDateTime.withHour(currentLocalDateTime.getHour()+multiplier);
                interval *= AlarmManager.INTERVAL_HOUR;
                break;
            case "Minute":
                nextZonedDateTime = nextZonedDateTime.withHour(currentLocalDateTime.getHour());
                nextZonedDateTime = nextZonedDateTime.withMinute(currentLocalDateTime.getMinute()+multiplier);
                interval *= AlarmManager.INTERVAL_FIFTEEN_MINUTES/15;
                break;
            case "Week":
                nextZonedDateTime = nextZonedDateTime.plusWeeks(multiplier);
                nextZonedDateTime = nextZonedDateTime.with(DayOfWeek.MONDAY);
                interval *= AlarmManager.INTERVAL_DAY*7;
                break;
            case "Month":
                nextZonedDateTime = nextZonedDateTime.plusMonths(multiplier);
                nextZonedDateTime = nextZonedDateTime.withDayOfMonth(1);
                interval *= AlarmManager.INTERVAL_DAY*30;
                break;
            case "Year":
                nextZonedDateTime = nextZonedDateTime.plusYears(multiplier);
                nextZonedDateTime = nextZonedDateTime.withDayOfYear(1);
                interval *= AlarmManager.INTERVAL_DAY*365;
                break;
            default:
                if (LocalTime.now().isAfter(localTime)) {
                    nextZonedDateTime = nextZonedDateTime.plusDays(multiplier);
                }
                interval *= AlarmManager.INTERVAL_DAY;
                break;
        }

        if (motivationalQuoteSetting.isNotificationOn()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextZonedDateTime.toInstant().toEpochMilli()
                    , interval, pendingIntent);
            return nextZonedDateTime;
        }

        alarmManager.cancel(pendingIntent);

        return null;
    }

    public PendingIntent setHomeworkReminderAlarm(Homework homework, HomeworkReminder homeworkReminder) {
        Intent reminderIntent = new Intent(context.getApplicationContext(), ReminderReceiver.class);

        int notificationID = new Random().nextInt(Integer.MAX_VALUE);

        reminderIntent.putExtra("notificationID", notificationID);

        long minutesBeforeDueDate = homeworkReminder.getTimeBeforeDueDate();
        String dateFrequency = homeworkReminder.getDateFrequency();

        reminderIntent.putExtra("homeworkName", homework.getHomeworkName());

        reminderIntent.putExtra("reminder", minutesBeforeDueDate + " " + dateFrequency);

        switch (dateFrequency) {
            case "Minute":
                break;
            case "Hour":
                minutesBeforeDueDate *= 60;
                break;
            case "Day":
                minutesBeforeDueDate *= 60 * 24;
                break;
            case "Week":
                minutesBeforeDueDate *= 60 * 24 * 7;
                break;
            case "Month":
                minutesBeforeDueDate *= 60 * 24 * 30;
                break;
            case "Year":
                minutesBeforeDueDate *= 60 * 24 * 365;
                break;
        }

        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                    notificationID, reminderIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                    notificationID, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis() + (homework.CalculateSecondsLeftBeforeDueDate() - Duration.ofMinutes(1).getSeconds() * minutesBeforeDueDate) * 1000),
                ZoneId.systemDefault());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                zonedDateTime.toInstant().toEpochMilli(),
                pendingIntent);

        Log.d("TAG", zonedDateTime.toString());

        return pendingIntent;
    }

    public void cancelReminderNotification(Homework homework, Map<Integer, ArrayList<PendingIntent>> homeworkNotificationMap) {

        ArrayList<PendingIntent> notificationReminderIntent = homeworkNotificationMap.get(homework.getHomeworkId());

        if (notificationReminderIntent == null) { return; }

        for (PendingIntent pendingIntent : notificationReminderIntent) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
