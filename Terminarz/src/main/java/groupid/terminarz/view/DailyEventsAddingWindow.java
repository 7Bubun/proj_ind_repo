package groupid.terminarz.view;

import groupid.terminarz.Utilities;
import groupid.terminarz.logic.MyTimeFormat;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import static groupid.terminarz.view.SceneCreator.nameOfCurrentUser;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DailyEventsAddingWindow extends DailyEventsSpecialWindow {

    public DailyEventsAddingWindow(MonthView monthView) {
        super(monthView);
    }

    @Override
    public void appear() {
        Stage window = new Stage();
        GridPane layout = prepareGridPane();
        Control[] controls = prepareControls();

        TextField nameField = (TextField) controls[3];
        TextField hourField = (TextField) controls[4];
        TextField minuteField = (TextField) controls[5];
        Button confirmingButton = (Button) controls[6];

        confirmingButton.setOnAction(eh -> {
            try {
                MyTimeFormat time = new MyTimeFormat(
                        Integer.parseInt(hourField.getText()),
                        Integer.parseInt(minuteField.getText())
                );

                eventsManager.addEvent(nameField.getText(), monthView.getDateOfCertainDay(), time, nameOfCurrentUser);
                monthView.getMainGUI().refresh();
                monthView.refreshEventsOfDay();
                window.close();

            } catch (IOException | SQLException e) {
                Utilities.popUpErrorBox("Nie udało się załadować danych.");
            }
        });

        layout.getChildren().addAll(controls);
        initWindow(window, layout, "Dodawanie wydarzenia", 150);
    }
}
