package sg.edu.np.mad.socrata;

public class HomeworkReminder {
    private long timeBeforeDueDate;
    private String dateFrequency;

    public HomeworkReminder(long timeBeforeDueDate, String dateFrequency) {
        setTimeBeforeDueDate(timeBeforeDueDate);
        setDateFrequency(dateFrequency);
    }

    public void setTimeBeforeDueDate(long timeBeforeDueDate) {
        this.timeBeforeDueDate = timeBeforeDueDate;
    }

    public void setDateFrequency(String dateFrequency) {
        this.dateFrequency = dateFrequency;
    }

    public String makeText() {
        return getTimeBeforeDueDate() + " " + getDateFrequency() + " before";
    }

    public long getTimeBeforeDueDate() {
        return timeBeforeDueDate;
    }

    public String getDateFrequency() {
        return dateFrequency;
    }
}
