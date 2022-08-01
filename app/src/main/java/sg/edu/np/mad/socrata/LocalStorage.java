package sg.edu.np.mad.socrata;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalStorage {
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();

    final String homeworkNotificationMapKey = "homeworkNotificationMap";
    final String motivationalQuoteSettingKey = "motivationalQuoteSetting";
    final String userKey = "user";

    public LocalStorage(Activity activity) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }
    public LocalStorage(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public User getUser() {
        String userString = sharedPreferences.getString(userKey, null);

        if (userString == null) {
            Log.e(userKey, "user is null", new Throwable());
        }

        return gson.fromJson(userString, User.class);
    }

    public void setUser(User user) {
        sharedPreferences.edit().putString(userKey, gson.toJson(user)).apply();
    }

    public void setModuleArrayList(ArrayList<Module> moduleArrayList) {
        User user = getUser();
        user.setModuleArrayList(moduleArrayList);
        setUser(user);
    }

    public ArrayList<Module> getModuleArrayList() {
        User user = getUser();
        return user.getModuleArrayList();
    }

    public void setHomeworkArrayList(ArrayList<Homework> homeworkArrayList, String moduleName) {
        ArrayList<Module> moduleArrayList = getModuleArrayList();

        Module module = moduleArrayList.get(ModuleUtils.findModule(moduleArrayList, moduleName));
        module.setHomeworkArrayList(homeworkArrayList);

        setModuleArrayList(moduleArrayList);
    }


    public void setNoteArrayList(ArrayList<Note> noteArrayList, ArrayList<Homework> homeworkArrayList,
                                 String homeworkName, String moduleName) {
        Homework homework = homeworkArrayList.get(HomeworkUtils.findHomework(homeworkArrayList, homeworkName));
        homework.setNoteArrayList(noteArrayList);

        setHomeworkArrayList(homeworkArrayList, moduleName);
    }

    public void setMotivationalQuoteSetting(MotivationalQuoteSetting motivationalQuoteSetting) {
        sharedPreferences.edit().putString(motivationalQuoteSettingKey, gson.toJson(motivationalQuoteSetting)).apply();
    }

    public MotivationalQuoteSetting getMotivationalQuoteSetting() {
        String motivationalQuoteSettingString = sharedPreferences.getString(motivationalQuoteSettingKey, null);

        if (motivationalQuoteSettingString == null) {
            return null;
        }

        return gson.fromJson(motivationalQuoteSettingString, MotivationalQuoteSetting.class);

    }

    public void addHomeworkNotificationPendingIntents(Homework homework, ArrayList<PendingIntent> notificationPendingIntents) {

        if (sharedPreferences.getString(homeworkNotificationMapKey, null) == null) {

            sharedPreferences.edit().putString(homeworkNotificationMapKey, gson.toJson(new HashMap<>())).apply();
        }

        Type type = new TypeToken<HashMap<Integer, ArrayList<PendingIntent>>>(){}.getType();
        Map<Integer, ArrayList<PendingIntent>>  homeworkNotificationMap =
                gson.fromJson(sharedPreferences.getString(homeworkNotificationMapKey, null), type);

        homeworkNotificationMap.put(homework.getHomeworkId(), notificationPendingIntents);
    }

    public Map<Integer, ArrayList<PendingIntent>>  getHomeworkNotificationMap() {
        Type type = new TypeToken<HashMap<Integer, ArrayList<PendingIntent>>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(homeworkNotificationMapKey, null), type);
    }

    public void removeHomeworkNotification(Homework homework) {
        Type type = new TypeToken<HashMap<Integer, ArrayList<Long>>>(){}.getType();
        Map<Integer, ArrayList<Long>>  homeworkNotificationMap =
                gson.fromJson(sharedPreferences.getString(homeworkNotificationMapKey, null), type);

        homeworkNotificationMap.remove(homework.getHomeworkId());
    }

}
