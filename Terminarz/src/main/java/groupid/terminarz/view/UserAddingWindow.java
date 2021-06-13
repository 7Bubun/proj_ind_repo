package groupid.terminarz.view;

import groupid.terminarz.Utilities;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserAddingWindow extends SpecialWindow {

    public UserAddingWindow(SceneCreator creator) {
        super(creator);
    }

    @Override
    public void appear() {
        Stage window = new Stage();

        Label usernameLabel = new Label("Login:");
        Label passwordLabel = new Label("Hasło:");
        Label passwordAgainLabel = new Label("Powtórz hasło:");

        TextField usernameTextField = new TextField();
        TextField passwordTextField = new PasswordField();
        TextField passwordAgainTextField = new PasswordField();

        Button confirmButton = new Button("Zatwierdź");
        confirmButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String passwordAgain = passwordAgainTextField.getText();

            if (username.length() < 1 || password.length() < 1 || passwordAgain.length() < 1) {
                Utilities.popUpErrorBox("Wszystkie pola muszą być wypełnione.");

            } else if (password.equals(passwordAgain)) {
                eventsManager.addUser(username, password);
                sceneCreator.getMainGUI().refresh();
                window.close();

            } else {
                Utilities.popUpErrorBox("Hasło oraz powtórzone hasło muszą być takie same.");
            }
        });

        GridPane layout = prepareGridPane();
        layout.add(usernameLabel, 0, 0);
        layout.add(passwordLabel, 0, 1);
        layout.add(passwordAgainLabel, 0, 2);
        layout.add(usernameTextField, 1, 0);
        layout.add(passwordTextField, 1, 1);
        layout.add(passwordAgainTextField, 1, 2);
        layout.add(confirmButton, 1, 3);

        initWindow(window, layout, "Dodawanie użytkownika", 180);
    }
}
