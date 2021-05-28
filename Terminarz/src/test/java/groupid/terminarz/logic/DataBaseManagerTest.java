package groupid.terminarz.logic;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DataBaseManagerTest {

    private DataBaseManager testedObject;

    @Before
    public void setUp() {
        testedObject = new DataBaseManager();
    }

    //best test ever
    /*
    @Test
    public void testConstructor() {
        new DataBaseManager();
        assert true;
    }
     */
    //one more
    @Test
    public void testLoadEvents() {
        List<MyEvent> returnedList = testedObject.loadEvents();
        for (MyEvent event : returnedList) {
            System.out.println(event.getId());
            System.out.println(event.getDeadline());
            System.out.println(event.getTime());
            System.out.println(event.getName());
            System.out.println("---------------");
        }

        assert true;
    }
}
