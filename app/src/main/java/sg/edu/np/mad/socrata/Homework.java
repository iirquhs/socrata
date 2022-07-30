package sg.edu.np.mad.socrata;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Homework {
    private String homeworkName;
    private boolean isCompleted = false;

    private String dueDateTimeString;

    private String moduleName;


    private ArrayList<Note> noteArrayList = new ArrayList<>();

    private int homeworkId;


    public Homework() {

    }

    public Homework(String homeworkName, String dueDateTimeString, String moduleRef) {
        Random random = new Random();
        this.setHomeworkName(homeworkName);
        this.setDueDateTimeString(dueDateTimeString);
        this.setModuleRef(moduleRef);
        this.setHomeworkId(random.nextInt(Integer.MAX_VALUE));
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
     * @return
     */
    public long CalculateSecondsLeftBeforeDueDate() {

        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime dueDateTime = this.ConvertDueDateTime(dueDateTimeString);

        Duration duration = Duration.between(currentTime, dueDateTime);

        return duration.getSeconds();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleRef(String moduleName) {
        this.setModuleName(moduleName);
    }


    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
