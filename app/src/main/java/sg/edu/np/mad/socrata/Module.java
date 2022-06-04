package sg.edu.np.mad.socrata;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Module {

    private String moduleName;

    private ArrayList<Homework> homeworkArrayList = new ArrayList<>();

    private float studyTime = 0f;

    private @ColorInt int color;

    public Module() {

    }

    public Module(String moduleName, @ColorRes int colorID, Context context) {
        this.setColor(colorID, context);
        this.setModuleName(moduleName);
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ArrayList<Homework> getHomeworkArrayList() {
        return homeworkArrayList;
    }

    public void setHomeworkArrayList(ArrayList<Homework> homeworkArrayList) {
        this.homeworkArrayList = homeworkArrayList;
    }

    public float getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(float studyTime) {
        this.studyTime = studyTime;
    }

    public @ColorInt int getColor() {
        return color;
    }

    public void setColor(@ColorRes int colorID, Context context) {
        this.color = ContextCompat.getColor(context, colorID);
    }
}
