package sg.edu.np.mad.socrata;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class Homework{

    private String homeworkName;
    private String status = "In Progress";

    private float timeSpent = 0f;

    private String dueDateTimeString;

    // The path for the specific module
    private String moduleRef;

    public Homework() {

    }

    public Homework(String homeworkName, String dueDateTimeString, String moduleRef) {
        this.setHomeworkName(homeworkName);
        this.setDueDateTimeString(dueDateTimeString);
        this.setModuleRef(moduleRef);
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

    public String getDueDateTimeString() {
        return dueDateTimeString;
    }

    public LocalDateTime ConvertDueDateTime(String dueDateTimeString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive().appendPattern("MMM dd yyyy HH:mm")
                .toFormatter(Locale.ENGLISH);

        LocalDateTime dueDateTime = LocalDateTime.parse(dueDateTimeString, formatter);
        return dueDateTime;
    }

    public long CalculateSecondsLeftBeforeDueDate(LocalDateTime dueDateTime) {

        LocalDateTime currentTime = LocalDateTime.now();

        Duration duration = Duration.between(currentTime, dueDateTime);

        return duration.getSeconds();
    }

    //PUT IN MMM dd yyyy HH:mm format eg JUN 12 2022
    public void setDueDateTimeString(String dueDateTimeString) {
        this.dueDateTimeString = dueDateTimeString;
    }

    public String getModuleRef() {
        return moduleRef;
    }

    public void setModuleRef(String moduleRef) {
        this.moduleRef = moduleRef;
    }
}
