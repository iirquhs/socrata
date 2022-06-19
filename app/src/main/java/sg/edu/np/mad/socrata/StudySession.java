package sg.edu.np.mad.socrata;

import java.time.LocalDateTime;

public class StudySession {
    // study time in seconds
    private long studyTime;

    private LocalDateTime studyStartDateTime = LocalDateTime.now();

    public StudySession() {

    }

    public StudySession(long studyTime) {
        this.studyTime = studyTime;
    }

    public long getStudyTime() {
        return studyTime;
    }

    public LocalDateTime getStudyStartDateTime() {
        return studyStartDateTime;
    }
}
