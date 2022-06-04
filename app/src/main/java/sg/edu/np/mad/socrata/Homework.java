package sg.edu.np.mad.socrata;

public class Homework {

    private String homeworkName;
    private String status = "In Progress";

    private float timeSpent = 0f;

    private Module module;

    public Homework() {

    }

    public Homework(String homeworkName, Module module) {
        this.setHomeworkName(homeworkName);
        this.setModule(module);
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(float timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
