package groupid.terminarz.logic;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MyTimeFormatTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        //given
        int hour = 2022;
        int minute = 13;
        MyTimeFormat test;

        //when
        test = new MyTimeFormat(hour, minute);

        //then
        assert false;
    }

    @Test
    public void testToString() {
        //given
        MyTimeFormat time = new MyTimeFormat(16, 20);

        //when
        String result = time.toString();
        String expResult = "16:20";

        //then
        assertEquals(expResult, result);
    }
}
