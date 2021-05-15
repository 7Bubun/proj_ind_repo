package groupid.terminarz.logic;

public class MyTimeFormat {
    private int hour;
    private int minute;

    public MyTimeFormat(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return String.valueOf(hour) + ":" + String.valueOf(minute);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
