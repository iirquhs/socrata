package sg.edu.np.mad.socrata;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class LocalStorage {
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();

    public LocalStorage(Activity activity) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public User getUser() {
        String userString = sharedPreferences.getString("user", null);

        if (userString == null) {
            Log.e("user", "user is null", new Throwable());
        }

        return gson.fromJson(userString, User.class);
    }

    public void setUser(User user) {
        Log.d("TAG", gson.toJson(user) + "");
        sharedPreferences.edit().putString("user", gson.toJson(user)).apply();
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

}
