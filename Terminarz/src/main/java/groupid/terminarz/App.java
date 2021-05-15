package groupid.terminarz;

import groupid.terminarz.view.EventsTableView;
import groupid.terminarz.view.SceneCreator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Stage mainStage;
    private SceneCreator creator;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        creator = new EventsTableView(this);
        Scene scene = creator.createScene();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void refresh() {
        Scene nextScene = creator.createScene();
        mainStage.setScene(nextScene);
    }
}
