package groupid.terminarz;

import groupid.terminarz.logic.MyDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class Utilities {

    public static String translateDayOfWeek(MyDateFormat date) {
        DayOfWeek dayOfWeek = DayOfWeek.from(LocalDate.of(date.getYear(), date.getMonth(), date.getDay()));

        switch (dayOfWeek) {
            case MONDAY:
                return "poniedziałek";

            case TUESDAY:
                return "wtorek";

            case WEDNESDAY:
                return "środa";

            case THURSDAY:
                return "czwartek";

            case FRIDAY:
                return "piątek";

            case SATURDAY:
                return "sobota";

            case SUNDAY:
                return "niedziela";

            default:
                throw new Error("MOTYLA NOGA!");
        }
    }

    public static String translateMonth(Month month) {
        switch (month) {
            case JANUARY:
                return "Styczeń";

            case FEBRUARY:
                return "Luty";

            case MARCH:
                return "Marzec";

            case APRIL:
                return "Kwiecień";

            case MAY:
                return "Maj";

            case JUNE:
                return "Czerwiec";

            case JULY:
                return "Lipiec";

            case AUGUST:
                return "Sierpień";

            case SEPTEMBER:
                return "Wrzesień";

            case OCTOBER:
                return "Październik";

            case NOVEMBER:
                return "Listopad";

            case DECEMBER:
                return "Grudzień";

            default:
                throw new Error("MOTYLA NOGA!");
        }
    }

    public static String format(int number) {
        String result = String.valueOf(number);

        if (number < 10) {
            result = "0" + result;
        }

        return result;
    }

    public static void popUpErrorBox(String message) {
        Alert messageBox = new Alert(Alert.AlertType.ERROR);
        messageBox.setContentText(message);
        messageBox.initModality(Modality.APPLICATION_MODAL);
        messageBox.show();
    }

    public static String addSpace(String base) {
        return " " + base;
    }

    public static String addSpace(int base) {
        return " " + String.valueOf(base);
    }
}
