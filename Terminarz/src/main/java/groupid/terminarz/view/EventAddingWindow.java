package groupid.terminarz.view;

import groupid.terminarz.Utilities;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyTimeFormat;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import static groupid.terminarz.view.SceneCreator.nameOfCurrentUser;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EventAddingWindow extends SpecialWindow {

    public EventAddingWindow(SceneCreator creator) {
        super(creator);
    }

    @Override
    public void appear() {
        Stage window = new Stage();

        Label[] labels = prepareLabels();
        TextField[] textFields = prepareTextFields();

        Button confirmingButton = new Button("Potwierdź");
        GridPane.setConstraints(confirmingButton, 1, 6);
        confirmingButton.setOnAction(e -> {
            try {
                String name = textFields[0].getText();
                MyDateFormat deadline = extractDateFromTextFields(textFields);
                MyTimeFormat time = extractTimeFromTextFields(textFields);

                if (name.length() > 20 || !Utilities.validateDate(deadline)) {
                    throw new IOException();
                }

                eventsManager.addEvent(name, deadline, time, nameOfCurrentUser);
                sceneCreator.getMainGUI().refresh();
                window.close();

            } catch (IOException | IllegalArgumentException exc) {
                Utilities.popUpErrorBox("Niepoprawne dane lub wybrano datę z przeszłości.");
            }
        });

        GridPane layout = prepareGridPane();
        layout.getChildren().addAll(labels);
        layout.getChildren().addAll(textFields);
        layout.getChildren().add(confirmingButton);
        initWindow(window, layout, "Nowe wydarzenie", 250);
    }
}
