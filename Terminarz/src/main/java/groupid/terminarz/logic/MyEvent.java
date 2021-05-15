package groupid.terminarz.logic;

import java.util.LinkedList;
import java.util.List;

public class MyEvent {

    private final static List<Integer> takenIdentities = new LinkedList<>();
    private final int id;
    private String name;
    private MyDateFormat deadline;
    private MyTimeFormat time;
    private DayOfTheWeek dayOfTheWeek;
    private boolean regular;

    public MyEvent(String name, MyDateFormat deadline, MyTimeFormat time) {
        this.name = name;
        this.deadline = deadline;
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

    public static void freeId(int id){
        takenIdentities.remove(id);
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
}
