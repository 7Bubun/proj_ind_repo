package groupid.terminarz.logic;

import groupid.terminarz.Utilities;
import java.io.IOException;
import java.util.Arrays;

public class MyDateFormat {

    private final static int[] months30 = {4, 6, 9, 11};
    private final static int[] months31 = {1, 3, 5, 7, 8, 10, 12};
    private int year;
    private int month;
    private int day;

    public MyDateFormat(int day, int month, int year) throws IOException {
        if (day <= 0 || month <= 0 || month > 12 || year < 0
                || (Arrays.binarySearch(months30, month) > 0 && day > 30)
                || (Arrays.binarySearch(months31, month) > 0 && day > 31)
                || (month == 2 && day > 29)) {

            throw new IOException("Podana data jest niepoprawna.");
        }

        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.valueOf(year)
                + "-" + Utilities.format(month)
                + "-" + Utilities.format(day);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
