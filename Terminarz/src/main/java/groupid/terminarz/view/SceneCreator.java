package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.DataStorage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class SceneCreator {

    private App mainGUI;
    protected static DataStorage eventsManager;

    public SceneCreator(App mainGUI) {
        this.mainGUI = mainGUI;
    }

    public Scene createScene() {
        return new Scene(new StackPane(), 20, 20);
    }

    public void showEditingOrAddingWindow() {
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

        Button confirmingButton = new Button("Confirm");
        confirmingButton.setOnAction(e -> {
            eventsManager.addEvent(
                name.getText(),
                Integer.parseInt(day.getText()),
                Integer.parseInt(month.getText()),
                Integer.parseInt(year.getText()),
                Integer.parseInt(hour.getText()),
                Integer.parseInt(minute.getText())
                );
            mainGUI.refresh();
        });
        
        layout.getChildren().add(confirmingButton);

        Stage window = new Stage();
        window.setScene(new Scene(layout, 200, 100));
        window.show();
    }
}
