package sg.edu.np.mad.socrata;

import java.util.ArrayList;

public class Module {

    private String moduleName;

    private ArrayList<Homework> homeworkArrayList = new ArrayList<>();

    private float studyTime = 0f;

    public Module() {

    }

    public Module(String moduleName) {
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
}
