package groupid.terminarz.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataStorageTest {

    private DataStorage dataStorage;

    @Before
    public void setUp() {
        dataStorage = new DataStorage();
    }

    @Test
    public void testAddEvent() throws IOException {
        //given
        String name = "test-nazwa";
        int day = 24;
        int month = 12;
        int year = 2024;
        int hour = 12;
        int minutes = 0;

        //when
        dataStorage.addEvent(name, day, month, year, hour, minutes);
        List<MyEvent> events = new ArrayList<>(dataStorage.getAllEvents().values());
        MyEvent result = events.get(0);

        //then
        assertEquals(name, result.getName());
        assertEquals(day, result.getDeadline().getDay());
        assertEquals(month, result.getDeadline().getMonth());
        assertEquals(year, result.getDeadline().getYear());
        assertEquals(hour, result.getTime().getHour());
        assertEquals(minutes, result.getTime().getMinute());
    }
    /*
    @Test
    public void testDeleteEvent() {
        System.out.println("deleteEvent");
        int id = 0;
        DataStorage instance = new DataStorage();
        instance.deleteEvent(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAllEvents() {
        System.out.println("getAllEvents");
        DataStorage instance = new DataStorage();
        Map expResult = null;
        Map result = instance.getAllEvents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */
}
