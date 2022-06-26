package sg.edu.np.mad.socrata;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class StudySession {
    private final String studyStartDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"));
    // study time in seconds
    private double studyTime;

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

    //Convert date string eg. JUN 12 2004 23:59 into localdatetime
    public LocalDateTime ConvertDueDateTime(String dueDateTimeString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive().appendPattern("MMM dd yyyy HH:mm")
                .toFormatter(Locale.ENGLISH);

        return LocalDateTime.parse(dueDateTimeString, formatter);
    }
}
