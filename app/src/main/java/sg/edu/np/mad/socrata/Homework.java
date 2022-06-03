package sg.edu.np.mad.socrata;

public class Homework {

    private String homeworkName;
    private String status = "In Progress";

    private float timeSpent = 0f;

    public Homework() {

    }

    public Homework(String homeworkName) {
        this.setHomeworkName(homeworkName);
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
}
