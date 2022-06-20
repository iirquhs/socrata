package sg.edu.np.mad.socrata;

import androidx.annotation.ColorInt;

import java.util.ArrayList;

public class Module {

    //TODO ADD GETTER AND SETTOR FOR ARRAY LIST SO THAT THE FIREBASE HomeworkCREATE can UPDATE AND CAN PASS THE WHOLE MODULE OBJECT

    private String moduleName;
    private String targetGrade;

    private ArrayList<Homework> homeworkArrayList = new ArrayList<>();
    private ArrayList<StudySession> studySessionArrayList = new ArrayList<>();

    private float totalStudyTime;

    private int targetHoursPerWeek;

    private @ColorInt int color;

    public Module() {

    }

    public Module(String moduleName, String targetGrade, int targetHoursPerWeek , @ColorInt int color) {
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

    public void addHomework(Homework homework) {
        this.homeworkArrayList.add(homework);
    }

    public double getTotalStudyTime() {
        double sum = 0f;
        for (StudySession studySession : studySessionArrayList) {
            sum += studySession.getStudyTime();
        }
        return sum;
    }

    public @ColorInt int getColor() {
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
