package groupid.terminarz.logic;

import java.util.LinkedList;
import java.util.List;

public class MyEvent {

    private final static List<Integer> takenIdentities = new LinkedList<>();
    private final int id;
    private String name;
    private MyDateFormat termin;
    private MyTimeFormat time;
    private DayOfTheWeek dayOfTheWeek;
    private boolean regular;

    public MyEvent(String name, MyDateFormat termin, MyTimeFormat time) {
        this.name = name;
        this.termin = termin;
        this.time = time;
        id = nextIdentity();
    }

    private int nextIdentity() {
        int n = 0;

        while (takenIdentities.contains(n)) {
            n++;
        }

        takenIdentities.add(n);
        return n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyDateFormat getTermin() {
        return termin;
    }

    public void setTermin(MyDateFormat termin) {
        this.termin = termin;
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
}
