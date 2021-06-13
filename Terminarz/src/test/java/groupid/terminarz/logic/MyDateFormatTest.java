package groupid.terminarz.logic;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyDateFormatTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor0() {
        //given
        int year = 2024;
        int month = 5;
        int day = 300;
        MyDateFormat test;

        //when
        test = new MyDateFormat(day, month, year);

        //then
        assert false;
    }

    @Test
    public void testConstructor1() {
        //given
        int year = 2023;
        int month = 2;
        int day = 28;
        MyDateFormat test;

        //when
        test = new MyDateFormat(day, month, year);

        //then
        assert true;
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
