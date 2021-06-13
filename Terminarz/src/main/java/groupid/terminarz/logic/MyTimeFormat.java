package groupid.terminarz.logic;

import groupid.terminarz.Utilities;

public class MyTimeFormat {

    private int hour;
    private int minute;

    public MyTimeFormat(int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException();
        }

        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return Utilities.format(hour) + ":" + Utilities.format(minute);
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
