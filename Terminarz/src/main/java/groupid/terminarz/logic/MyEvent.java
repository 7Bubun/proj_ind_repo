package groupid.terminarz.logic;

import java.time.DayOfWeek;
import java.time.LocalDate;

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

        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDate.of(deadline.getYear(), deadline.getMonth(), deadline.getDay()));

        switch (dayOfWeek) {
            case MONDAY:
                dayOfTheWeek = "poniedziałek";
                break;

            case TUESDAY:
                dayOfTheWeek = "wtorek";
                break;

            case WEDNESDAY:
                dayOfTheWeek = "środa";
                break;

            case THURSDAY:
                dayOfTheWeek = "czwartek";
                break;

            case FRIDAY:
                dayOfTheWeek = "piątek";
                break;

            case SATURDAY:
                dayOfTheWeek = "sobota";
                break;

            case SUNDAY:
                dayOfTheWeek = "niedziela";
                break;

            default:
                throw new Error("MOTYLA NOGA!");
        }
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
