package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.*;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public abstract class SceneCreator {

    protected static DataBaseManager eventsManager;
    protected static String nameOfCurrentUser = "-";
    protected final App mainGUI;
    protected SpecialWindow smallWindow;

    public SceneCreator(App mainGUI) throws SQLException {
        this.mainGUI = mainGUI;

        if (eventsManager == null) {
            eventsManager = new DataBaseManager();
        }
    }

    public Scene createScene() throws IOException, SQLException {
        return new Scene(new StackPane(), 20, 20);
    }

    protected Button prepareUserChoosingButton() {
        Button userChooser = new Button("Zaloguj się");
        userChooser.setOnAction(eh -> {
            smallWindow = new LoggingInWindow(this);
            smallWindow.appear();
        });

        return userChooser;
    }

    protected Button prepareAddEventButton() {
        Button addEventButton = new Button("Dodaj wydarzenie");
        addEventButton.setOnAction(eh -> {
            smallWindow = new EventAddingWindow(this);
            smallWindow.appear();
        });

        return addEventButton;
    }

    protected Button prepareAddUserButton() {
        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.setOnAction(eh -> {
            smallWindow = new UserAddingWindow(this);
            smallWindow.appear();
        });

        return addUserButton;
    }

    protected Button prepareChangeViewButton(SceneCreator nextView) {
        Button changeViewButton = new Button("Zmień widok");
        changeViewButton.setOnAction(eh -> mainGUI.changeScene(nextView));
        return changeViewButton;
    }

    protected Button prepareHelpButton() {
        Button changeViewButton = new Button("Pomoc");
        changeViewButton.setOnAction(eh -> {
            smallWindow = new HelpWindow(this);
            smallWindow.appear();
        });

        return changeViewButton;
    }

    public App getMainGUI() {
        return mainGUI;
    }
}
