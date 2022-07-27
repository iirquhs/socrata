package sg.edu.np.mad.socrata;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    public static final String ACTION_AUTO_UPDATE = "AUTO_UPDATE";
    public static final String ADD = "add";
    public static final String EXTRA_ITEM = "1";
    public static final String REFRESH = "refresh";
    public static final String EXTRA_ITEM_POSITION = "extraItemPosition";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        switch (intent.getAction()) {
            case EXTRA_ITEM_POSITION:
                //change your activity name
                context.startActivity(new Intent(context,
                        MainActivity.class).putExtra("homework", "homework")
                        .setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case ADD:
                //change your activity name
                context.startActivity(new Intent(context,
                        HomeworkCreateActivity.class)
                        .setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case ACTION_AUTO_UPDATE:
                //int i = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
                //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                int i = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.notifyAppWidgetViewDataChanged(i,R.id.widget_list);
                Toast.makeText(context, "refreshed", Toast.LENGTH_SHORT).show();
                break;

            //refresh button
            /*case REFRESH:
                int i = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.notifyAppWidgetViewDataChanged(i, R.id.widget_list);
                Toast.makeText(context, "refreshed", Toast.LENGTH_SHORT).show();
                break;*/
        }

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Here we setup the intent which points to the StackViewService which will
        // provide the views for this collection.
        Intent add = new Intent(context, NewAppWidget.class);
        add.setAction(NewAppWidget.ADD);
        add.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent addPendingIntent = PendingIntent.getBroadcast(context, 0 ,add,PendingIntent.FLAG_IMMUTABLE);

        //refresh button
        Intent refresh = new Intent(context, NewAppWidget.class);
        refresh.setAction(NewAppWidget.ACTION_AUTO_UPDATE);
        refresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0 ,refresh,PendingIntent.FLAG_IMMUTABLE);
        refresh.setData(Uri.parse(refresh.toUri(Intent.URI_INTENT_SCHEME)));

        Intent intent = new Intent(context, WidgetListAdapter.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent toastIntent = new Intent(context, NewAppWidget.class);
        toastIntent.setAction(NewAppWidget.EXTRA_ITEM_POSITION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_IMMUTABLE);

        //Instantiate the RemoteViews object//
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setPendingIntentTemplate(R.id.widget_list, toastPendingIntent);
        views.setOnClickPendingIntent(R.id.refreshButton, refreshPendingIntent);
        views.setOnClickPendingIntent(R.id.addButton, addPendingIntent);
        setRemoteAdapter(context, views);
        //Request that the AppWidgetManager updates the application widget//
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        // start alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
        Log.d("timer", "started: ");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        // stop alarm only if all widgets have been disabled
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(),getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        if (appWidgetIds.length == 0) {
            // stop alarm
            AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
            appWidgetAlarm.stopAlarm();
        }
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, WidgetService.class));
    }
}