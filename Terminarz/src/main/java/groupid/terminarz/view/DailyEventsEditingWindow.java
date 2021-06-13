package groupid.terminarz.view;

import groupid.terminarz.Utilities;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DailyEventsEditingWindow extends DailyEventsSpecialWindow {

    public DailyEventsEditingWindow(MonthView monthView) {
        super(monthView);
    }

    @Override
    public void appear(MyEvent event) {
        Stage window = new Stage();
        GridPane layout = prepareGridPane();
        Control[] controls = prepareControls();

        TextField nameField = (TextField) controls[3];
        nameField.setText(event.getName());

        TextField hourField = (TextField) controls[4];
        hourField.setText(String.valueOf(event.getTime().getHour()));

        TextField minuteField = (TextField) controls[5];
        minuteField.setText(String.valueOf(event.getTime().getMinute()));

        Button confirmingButton = (Button) controls[6];

        confirmingButton.setOnAction(eh -> {
            try {
                MyTimeFormat time = new MyTimeFormat(
                        Integer.parseInt(hourField.getText()),
                        Integer.parseInt(minuteField.getText())
                );

                event.setName(nameField.getText());
                event.setTime(time);
                eventsManager.updateEvent(event);
                sceneCreator.getMainGUI().refresh();
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
