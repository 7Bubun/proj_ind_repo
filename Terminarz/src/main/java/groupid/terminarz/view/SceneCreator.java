package groupid.terminarz.view;

import groupid.terminarz.logic.DataBaseManager;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class SceneCreator {

    protected static DataBaseManager eventsManager;

    public Scene createScene() {
        return new Scene(new StackPane(), 20, 20);
    }

    public void showEditingOrAddingWindow() {
        Stage window = new Stage();
        window.setScene(new Scene(new StackPane(), 200, 100));
        window.show();
    }
}
