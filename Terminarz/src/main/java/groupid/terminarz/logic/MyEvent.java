package groupid.terminarz.logic;

import groupid.terminarz.Utilities;

public class MyEvent {

    private final int id;
    private String name;
    private MyDateFormat deadline;
    private MyTimeFormat time;
    private final String dayOfTheWeek;

    public MyEvent(int id, MyDateFormat deadline, MyTimeFormat time, String name) {
        this.name = name;
        this.deadline = deadline;
        this.time = time;
        this.id = id;
        this.dayOfTheWeek = Utilities.translateDayOfWeek(deadline);
    }

    @Override
    public String toString() {
        return name + time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyDateFormat getDeadline() {
        return deadline;
    }

    public void setDeadline(MyDateFormat termin) {
        this.deadline = termin;
    }

    public MyTimeFormat getTime() {
        return time;
    }

    public void setTime(MyTimeFormat time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }
}
