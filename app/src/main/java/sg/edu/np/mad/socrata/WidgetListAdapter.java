package sg.edu.np.mad.socrata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;


import java.util.ArrayList;
import java.util.List;

public class WidgetListAdapter implements RemoteViewsService.RemoteViewsFactory{
    //put here 1st to update list

    LocalStorage localStorage;
    Integer moduleName = R.id.widgetModule;
    Integer homework = R.id.widgetHomework;
    ArrayList<Homework> List = new ArrayList<>();
    ArrayList<Module> mList = new ArrayList<>();
    Context mContext;
    private int appWidgetId;

    public WidgetListAdapter(Context context, Intent intent){
        mContext = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        initData();
        Log.d("ASd", List.toString());
    }

    @Override
    public void onDataSetChanged() {
        List.clear();
        initData();
        Log.d("refresh", List.toString());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.collection_widget);
        Module module = mList.get(ModuleUtils.findModule(mList, List.get(position).getModuleName()));
        int colour = module.getColor();
        view.setTextViewText(moduleName, List.get(position).getModuleName());
        view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour1);
        view.setTextViewText(homework, List.get(position).getHomeworkName());
        switch ( Integer.toHexString(colour).toUpperCase()){
            case "FF00DDFF":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour1);
                break;
            case "FF0099CC":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour2);
                break;
            case "FF669900":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour3);
                break;
            case "FFFF8800":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour4);
                break;
            case "FF99cc00":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour5);
                break;
            case "FFFFBB33":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour6);
                break;
            case "FFAA66CC":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour7);
                break;
            case "FFFF4444":
                view.setInt(R.id.item_frame, "setBackgroundResource", R.drawable.widget_colour8);
                break;
        }

        Bundle extras = new Bundle();
        extras.putInt(NewAppWidget.EXTRA_ITEM,
                position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        view.setOnClickFillInIntent(R.id.item_frame, fillInIntent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private void initData() {
        localStorage = new LocalStorage(mContext);
        mList = localStorage.getModuleArrayList();
        List = HomeworkUtils.splitByIsCompletedHomeworkArrayList(mList)[0];
    }

}
