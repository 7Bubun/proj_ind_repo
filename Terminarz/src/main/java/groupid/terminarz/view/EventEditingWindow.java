package groupid.terminarz.view;

import groupid.terminarz.Utilities;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EventEditingWindow extends SpecialWindow {

    public EventEditingWindow(SceneCreator creator) {
        super(creator);
    }

    @Override
    public void appear(MyEvent event) {
        Stage window = new Stage();

        Label[] labels = prepareLabels();
        TextField[] textFields = prepareTextFields();
        TextField name = textFields[0];
        TextField day = textFields[1];
        TextField month = textFields[2];
        TextField year = textFields[3];
        TextField hour = textFields[4];
        TextField minute = textFields[5];

        MyDateFormat deadline = event.getDeadline();
        MyTimeFormat time = event.getTime();

        name.setText(event.getName());
        day.setText(String.valueOf(deadline.getDay()));
        month.setText(String.valueOf(deadline.getMonth()));
        year.setText(String.valueOf(deadline.getYear()));
        hour.setText(String.valueOf(time.getHour()));
        minute.setText(String.valueOf(time.getMinute()));

        Button confirmingButton = new Button("Potwierdź");
        GridPane.setConstraints(confirmingButton, 1, 6);
        confirmingButton.setOnAction(e -> {
            try {
                String updatedName = name.getText();
                MyDateFormat updatedDeadline = extractDateFromTextFields(textFields);
                MyTimeFormat updatedTime = extractTimeFromTextFields(textFields);

                if (updatedName.length() > 20) {
                    throw new IOException();
                }

                event.setName(updatedName);
                event.setDeadline(updatedDeadline);
                event.setTime(updatedTime);
                eventsManager.updateEvent(event);
                sceneCreator.getMainGUI().refresh();
                window.close();

            } catch (IOException | IllegalArgumentException exc) {
                Utilities.popUpErrorBox("Podane dane nie są poprawne.");
            }
        });

        GridPane layout = prepareGridPane();
        layout.getChildren().addAll(name, day, month, year, hour, minute, confirmingButton);
        layout.getChildren().addAll(labels);
        initWindow(window, layout, "Edycja wydarzenia", 250);
    }
}
