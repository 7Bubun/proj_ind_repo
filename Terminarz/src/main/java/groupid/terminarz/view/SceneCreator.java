package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.DataBaseManager;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyTimeFormat;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class SceneCreator {

    private App mainGUI;
    protected static DataBaseManager eventsManager;

    public SceneCreator(App mainGUI) {
        this.mainGUI = mainGUI;

        if (eventsManager == null) {
            eventsManager = new DataBaseManager();
        }
    }

    public Scene createScene() {
        return new Scene(new StackPane(), 20, 20);
    }

    public void showEditingOrAddingWindow() {
        Stage window = new Stage();
        FlowPane layout = new FlowPane();

        TextField name = new TextField();
        layout.getChildren().add(name);

        TextField day = new TextField();
        layout.getChildren().add(day);

        TextField month = new TextField();
        layout.getChildren().add(month);

        TextField year = new TextField();
        layout.getChildren().add(year);

        TextField hour = new TextField();
        layout.getChildren().add(hour);

        TextField minute = new TextField();
        layout.getChildren().add(minute);

        Button confirmingButton = new Button("Potwierdź");
        confirmingButton.setOnAction(e -> {
            try {
                MyDateFormat date = new MyDateFormat(
                        Integer.parseInt(day.getText()),
                        Integer.parseInt(month.getText()),
                        Integer.parseInt(year.getText())
                );

                MyTimeFormat time = new MyTimeFormat(
                        Integer.parseInt(hour.getText()),
                        Integer.parseInt(minute.getText())
                );

                eventsManager.addEvent(name.getText(), date, time);
                mainGUI.refresh();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            window.close();
        });

        layout.getChildren().add(confirmingButton);

        window.setScene(new Scene(layout, 400, 200));
        window.show();
    }
}
