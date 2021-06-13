package groupid.terminarz.view;

import static groupid.terminarz.view.SceneCreator.eventsManager;
import javafx.scene.control.Alert;
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

            if (password.equals(passwordAgain)) {
                eventsManager.addUser(username, password);
                sceneCreator.getMainGUI().refresh();
                window.close();

            } else {
                Alert messageBox = new Alert(Alert.AlertType.ERROR);
                messageBox.setContentText("Hasło oraz powtórzone hasło muszą być takie same.");
                messageBox.show();
            }
        });

        GridPane layout = prepareGridPane();
        GridPane.setConstraints(usernameLabel, 0, 0);
        GridPane.setConstraints(passwordLabel, 0, 1);
        GridPane.setConstraints(passwordAgainLabel, 0, 2);
        GridPane.setConstraints(usernameTextField, 1, 0);
        GridPane.setConstraints(passwordTextField, 1, 1);
        GridPane.setConstraints(passwordAgainTextField, 1, 2);
        GridPane.setConstraints(confirmButton, 1, 3);

        layout.getChildren().addAll(
                usernameLabel,
                passwordLabel,
                passwordAgainLabel,
                usernameTextField,
                passwordTextField,
                passwordAgainTextField,
                confirmButton
        );

        initWindow(window, layout, "Dodawanie użytkownika", 180);
    }
}
