package sg.edu.np.mad.socrata;

import java.time.LocalTime;

public class MotivationalQuoteSetting {
    private boolean isNotificationOn = true;

    private LocalTime timeOfDay = LocalTime.of(8, 0);

    private String frequency = "Day";

    private int multiplier = 1;

    public MotivationalQuoteSetting(boolean isNotificationOn, LocalTime timeOfDay, String frequency, int multiplier) {
        setNotificationOn(isNotificationOn);
        setFrequency(frequency);
        setTimeOfDay(timeOfDay);
        setMultiplier(multiplier);
    }

    public boolean isNotificationOn() {
        return isNotificationOn;
    }

    public void setNotificationOn(boolean notificationOn) {
        isNotificationOn = notificationOn;
    }

    public LocalTime getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(LocalTime timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
