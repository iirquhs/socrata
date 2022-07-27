package sg.edu.np.mad.socrata;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;

public class Homework {

    private String homeworkName;
    private boolean isCompleted = false;

    private String dueDateTimeString;

    private String moduleName;

    private ArrayList<Note> noteArrayList = new ArrayList<>();

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

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean status) {
        this.isCompleted = status;
    }

    public String getDueDateTimeString() {
        return dueDateTimeString;
    }

    public ArrayList<Note> getNoteArrayList() {return noteArrayList; }

    public void setNoteArrayList(ArrayList<Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    public void addNote(Note note) {
        this.noteArrayList.add(note);
    }


    //PUT IN MMM dd yyyy HH:mm format eg JUN 12 2022
    public void setDueDateTimeString(String dueDateTimeString) {
        this.dueDateTimeString = dueDateTimeString;
    }

    /**
     * Convert dueDateTimeString into LocalDateTime
     * @param dueDateTimeString
     * @return
     */
    public LocalDateTime ConvertDueDateTime(String dueDateTimeString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive().appendPattern("MMM dd yyyy HH:mm")
                .toFormatter(Locale.ENGLISH);

        LocalDateTime dueDateTime = LocalDateTime.parse(dueDateTimeString, formatter);
        return dueDateTime;
    }

    /**
     * Calculate the time between the current time and the due date time and return the seconds left
     * @param dueDateTime
     * @return
     */
    public long CalculateSecondsLeftBeforeDueDate(LocalDateTime dueDateTime) {

        LocalDateTime currentTime = LocalDateTime.now();

        Duration duration = Duration.between(currentTime, dueDateTime);

        return duration.getSeconds();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleRef(String moduleName) {
        this.moduleName = moduleName;
    }
}
