package groupid.terminarz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    SceneHolder holder = new SceneHolder();

    @Override
    public void start(Stage stage) {
        Scene scene = holder.getViewOfTable();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
