package sg.edu.np.mad.socrata;

import java.time.LocalDateTime;

public class StudySession {
    // study time in seconds
    private double studyTime;

    private LocalDateTime studyStartDateTime = LocalDateTime.now();

    public StudySession() {

    }

    public StudySession(double studyTime) {
        this.studyTime = studyTime;
    }

    public double getStudyTime() {
        return studyTime;
    }

    public LocalDateTime getStudyStartDateTime() {
        return studyStartDateTime;
    }
}
