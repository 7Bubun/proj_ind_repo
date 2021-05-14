package groupid.terminarz.view;

import groupid.terminarz.logic.DataBaseManager;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public abstract class SceneMaker {

    protected static DataBaseManager eventsManager;

    public Scene makeScene() {
        return new Scene(new StackPane(), 20, 20);
    }
}
