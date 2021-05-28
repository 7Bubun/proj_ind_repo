package groupid.terminarz.logic;

public class Utilities {

    public static String format(int number) {
        String result = String.valueOf(number);

        if (number < 10) {
            result = "0" + result;
        }

        return result;
    }
}
