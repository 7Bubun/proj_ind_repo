package groupid.terminarz.logic;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyDateFormatTest {

    public MyDateFormatTest() {
    }

    @Before
    public void setUp() {
    }

    @Test(expected = IOException.class)
    public void constructorTest0() throws IOException {
        //given
        MyDateFormat test;

        //when
        test = new MyDateFormat(300, 5, 2004);

        //then
        assert false;
    }

    @Test
    public void testToString() throws IOException {
        //given
        MyDateFormat date = new MyDateFormat(21, 7, 2022);

        //when
        String result = date.toString();
        String expResult = "2022-07-21";

        //then
        assertEquals(expResult, result);
    }
}
