package sg.edu.np.mad.socrata;

import androidx.annotation.ColorInt;

import java.util.ArrayList;

public class Module {

    private String moduleName;
    private String targetGrade;

    private ArrayList<Homework> homeworkArrayList = new ArrayList<>();
    private ArrayList<StudySession> studySessionArrayList = new ArrayList<>();

    private int targetHoursPerWeek;

    private @ColorInt
    int color;

    public Module() {

    }

    public Module(String moduleName, String targetGrade, int targetHoursPerWeek, @ColorInt int color) {
        this.setColor(color);
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

    public void setHomeworkArrayList(ArrayList<Homework> homeworkArrayList) {
        this.homeworkArrayList = homeworkArrayList;
    }

    public void addHomework(Homework homework) {
        this.homeworkArrayList.add(homework);
    }

    public @ColorInt
    int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
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

    public void setStudySessionArrayList(ArrayList<StudySession> studySessionArrayList) {
        this.studySessionArrayList = studySessionArrayList;
    }

    public void addStudySession(StudySession studySession) {
        this.studySessionArrayList.add(studySession);
    }

    public int getTargetHoursPerWeek() {
        return targetHoursPerWeek;
    }

    public void setTargetHoursPerWeek(int targetHoursPerWeek) {
        this.targetHoursPerWeek = targetHoursPerWeek;
    }
}
