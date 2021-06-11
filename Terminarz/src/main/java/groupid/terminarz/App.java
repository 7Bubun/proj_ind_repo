package groupid.terminarz;

import groupid.terminarz.logic.Utilities;
import groupid.terminarz.view.EventsTableView;
import groupid.terminarz.view.SceneCreator;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Stage mainStage;
    private SceneCreator creator;

    @Override
    public void start(Stage stage) {
        mainStage = stage;

        try {
            creator = new EventsTableView(this);
            Scene scene = creator.createScene();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Aplikacja Terminarz");
            stage.setOnCloseRequest(e -> Platform.exit());
            stage.show();

        } catch (IOException | SQLException e) {
            Utilities.popUpErrorBox("Błąd. Nie udało się załadować danych.");
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public void changeScene(SceneCreator creator) {
        this.creator = creator;
        refresh();
    }

    public void refresh() {
        try {
            Scene nextScene = creator.createScene();
            mainStage.setScene(nextScene);

        } catch (IOException | SQLException e) {
            Utilities.popUpErrorBox("Błąd. Nie udało się załadować danych.");
        }
    }
}
