package groupid.terminarz.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataStorage {
    
    private Map<Integer, MyEvent> allEvents;
    
    public DataStorage() {
        allEvents = new HashMap<>();
        
        /*
        try {
            MyEvent m = new MyEvent("chillera", new MyDateFormat(30, 9, 2021), new MyTimeFormat(15, 0));
            allEvents.put(m.getId(), m);
            m = new MyEvent("utopia", new MyDateFormat(5, 5, 1999), new MyTimeFormat(3, 9));
            allEvents.put(m.getId(), m);
            m = new MyEvent("tere", new MyDateFormat(20, 4, 2022), new MyTimeFormat(15, 45));
            allEvents.put(m.getId(), m);
            m = new MyEvent("fere", new MyDateFormat(12, 5, 2011), new MyTimeFormat(15, 123));
            allEvents.put(m.getId(), m);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        */
    }
    /*
    public void addEvent(String name, int day, int month, int year, int hour, int minute) {
        try {
            MyEvent m = new MyEvent(name, new MyDateFormat(day, month, year), new MyTimeFormat(hour, minute));
            allEvents.put(m.getId(), m);
            
        } catch (IOException ioe) {
            System.err.println("BUUUUUUUUUUUUU");   //do zmianyyyyyyyyyyyyyyyy
        }
    }
    
    public void deleteEvent(int id) {
        allEvents.remove(id);
        MyEvent.freeId(id);
    }
    */
    public Map getAllEvents() {
        return allEvents;
    }
}
