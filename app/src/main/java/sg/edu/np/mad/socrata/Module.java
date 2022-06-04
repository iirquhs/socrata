package sg.edu.np.mad.socrata;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Module {

    private String moduleName;
    private String targetGrade;

    private ArrayList<Homework> homeworkArrayList = new ArrayList<>();
    private ArrayList<StudySession> studySessionArrayList = new ArrayList<>();

    private float totalStudyTime;

    private int targetHoursPerWeek;

    private @ColorInt int color;

    public Module() {

    }

    public Module(String moduleName, String targetGrade, int targetHoursPerWeek , @ColorRes int colorID, Context context) {
        this.setColor(colorID, context);
        this.setModuleName(moduleName);
        this.setTargetGrade(targetGrade);
        this.setTargetHoursPerWeek(targetHoursPerWeek);
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

    public void addHomeworkArrayList(Homework homework) {
        this.homeworkArrayList.add(homework);
    }

    public float getTotalStudyTime() {
        float sum = 0f;
        for (StudySession studySession : studySessionArrayList) {
            sum += studySession.getStudyTime();
        }
        return sum;
    }

    public @ColorInt int getColor() {
        return color;
    }

    public void setColor(@ColorRes int colorID, Context context) {
        this.color = ContextCompat.getColor(context, colorID);
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(String targetGrade) {
        this.targetGrade = targetGrade;
    }

    public ArrayList<StudySession> getStudySessionArrayList() {
        return studySessionArrayList;
    }

    public void setStudySessionArrayList(StudySession studySession) {
        this.studySessionArrayList.add(studySession);
    }

    public int getTargetHoursPerWeek() {
        return targetHoursPerWeek;
    }

    public void setTargetHoursPerWeek(int targetHoursPerWeek) {
        this.targetHoursPerWeek = targetHoursPerWeek;
    }
}
