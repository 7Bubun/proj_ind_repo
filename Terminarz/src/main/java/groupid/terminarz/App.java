package groupid.terminarz;

import groupid.terminarz.view.EventsTableView;
import groupid.terminarz.view.SceneCreator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    SceneCreator creator;

    @Override
    public void start(Stage stage) {
        creator = new EventsTableView();
        Scene scene = creator.createScene();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
