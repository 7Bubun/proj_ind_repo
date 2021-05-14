package groupid.terminarz.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataBaseManager {

    private Map<Integer, MyEvent> allEvents;

    public DataBaseManager() {
        allEvents = new HashMap<>();

        try {
            MyEvent m = new MyEvent("chillera", new MyDateFormat(30, 9, 2021), new MyTimeFormat(15, 0));
            allEvents.put(m.getId(), m);
            m = new MyEvent("utopia", new MyDateFormat(5, 5, 1999), new MyTimeFormat(3, 9));
            allEvents.put(m.getId(), m);
            m = new MyEvent("tere", new MyDateFormat(11, 9, 2001), new MyTimeFormat(15, 45));
            allEvents.put(m.getId(), m);
            m = new MyEvent("fere", new MyDateFormat(12, 5, 2011), new MyTimeFormat(15, 123));
            allEvents.put(m.getId(), m);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public Map getAllEvents() {
        return allEvents;
    }
}
