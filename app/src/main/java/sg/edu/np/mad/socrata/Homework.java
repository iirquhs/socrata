package sg.edu.np.mad.socrata;

import java.time.LocalDateTime;

public class Homework{

    private String homeworkName;
    private String status = "In Progress";

    private float timeSpent = 0f;

    private LocalDateTime dueDateTime;

    private Module module;

    private boolean hasDoneHomework;

    public Homework() {

    }

    public Homework(String homeworkName, LocalDateTime dueDateTime, Module module) {
        this.setHomeworkName(homeworkName);
        this.setModule(module);
        this.setDueDateTime(dueDateTime);
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

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public boolean isHasDoneHomework() {
        return hasDoneHomework;
    }

    public void setHasDoneHomework(boolean hasDoneHomework) {
        this.hasDoneHomework = hasDoneHomework;
    }
}
