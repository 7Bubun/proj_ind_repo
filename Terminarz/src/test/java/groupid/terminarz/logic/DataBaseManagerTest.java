package groupid.terminarz.logic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DataBaseManagerTest {

    private DataBaseManager testedObject;

    @Before
    public void setUp() throws SQLException {
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
    public void testLoadEvents() throws IOException, SQLException{
        List<MyEvent> returnedList = testedObject.loadEvents("co?");
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
