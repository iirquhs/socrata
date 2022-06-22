package sg.edu.np.mad.socrata;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class StudySession {
    // study time in seconds
    private double studyTime;

    private final String studyStartDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"));

    public StudySession() {

    }

    public StudySession(double studyTime) {
        this.studyTime = studyTime;
    }

    public double getStudyTime() {
        return studyTime;
    }

    public String getStudyStartDateTime() {
        return studyStartDateTime;
    }

    public LocalDateTime ConvertDueDateTime(String dueDateTimeString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive().appendPattern("MMM dd yyyy HH:mm")
                .toFormatter(Locale.ENGLISH);

        return LocalDateTime.parse(dueDateTimeString, formatter);
    }
}
