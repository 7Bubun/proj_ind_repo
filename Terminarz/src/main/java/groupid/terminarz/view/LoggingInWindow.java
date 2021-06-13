package groupid.terminarz.view;

import groupid.terminarz.Utilities;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import static groupid.terminarz.view.SceneCreator.nameOfCurrentUser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoggingInWindow extends SpecialWindow {

    public LoggingInWindow(SceneCreator creator) {
        super(creator);
    }

    @Override
    public void appear() {
        Stage window = new Stage();

        Label loginLabel = new Label("Login:");
        Label passwordLabel = new Label("Hasło:");
        TextField loginTextField = new TextField();
        TextField passwordTextField = new PasswordField();
        Button confirmButton = new Button("Zatwierdź");

        passwordTextField.setPromptText("Hasło");

        confirmButton.setOnAction(eh -> {
            String username = loginTextField.getText();
            String password = passwordTextField.getText();

            if (eventsManager.checkPassword(username, password)) {
                nameOfCurrentUser = username;
                sceneCreator.getMainGUI().refresh();
                window.close();

            } else {
                Utilities.popUpErrorBox("Podane dane logowania są niepoprawne.");
            }
        });

        GridPane layout = prepareGridPane();
        layout.add(loginLabel, 0, 0);
        layout.add(passwordLabel, 0, 1);
        layout.add(loginTextField, 1, 0);
        layout.add(passwordTextField, 1, 1);
        layout.add(confirmButton, 1, 2);

        initWindow(window, layout, "Logowanie", 200);
    }
}
