package groupid.terminarz.logic;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class Utilities {

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
