package groupid.terminarz.view;

import groupid.terminarz.Utilities;
import groupid.terminarz.App;
import groupid.terminarz.logic.*;
import java.io.IOException;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class SceneCreator {

    protected static DataBaseManager eventsManager;
    protected static String nameOfCurrentUser = "-";
    protected final App mainGUI;

    public SceneCreator(App mainGUI) throws SQLException {
        this.mainGUI = mainGUI;

        if (eventsManager == null) {
            eventsManager = new DataBaseManager();
        }
    }

    public Scene createScene() throws IOException, SQLException {
        return new Scene(new StackPane(), 20, 20);
    }

    public void showEventAddingWindow() {
        Stage window = new Stage();

        Label[] labels = prepareLabels();
        TextField[] textFields = prepareTextFields();

        Button confirmingButton = new Button("Potwierdź");
        GridPane.setConstraints(confirmingButton, 1, 6);
        confirmingButton.setOnAction(e -> {
            try {
                String name = textFields[0].getText();
                MyDateFormat deadline = extractDateFromTextFields(textFields);
                MyTimeFormat time = extractTimeFromTextFields(textFields);

                if (name.length() > 20) {
                    throw new IOException();
                }

                eventsManager.addEvent(name, deadline, time, nameOfCurrentUser);
                mainGUI.refresh();
                window.close();

            } catch (IOException | IllegalArgumentException exc) {
                Utilities.popUpErrorBox("Podane dane nie są poprawne.");
            }
        });

        GridPane layout = prepareGridPane();
        layout.getChildren().addAll(labels);
        layout.getChildren().addAll(textFields);
        layout.getChildren().add(confirmingButton);
        initWindow(window, layout, "Nowe wydarzenie", 250);
    }

    public void showEventEditingWindow(MyEvent editedEvent) {
        Stage window = new Stage();

        Label[] labels = prepareLabels();
        TextField[] textFields = prepareTextFields();
        TextField name = textFields[0];
        TextField day = textFields[1];
        TextField month = textFields[2];
        TextField year = textFields[3];
        TextField hour = textFields[4];
        TextField minute = textFields[5];

        MyDateFormat deadline = editedEvent.getDeadline();
        MyTimeFormat time = editedEvent.getTime();

        name.setText(editedEvent.getName());
        day.setText(String.valueOf(deadline.getDay()));
        month.setText(String.valueOf(deadline.getMonth()));
        year.setText(String.valueOf(deadline.getYear()));
        hour.setText(String.valueOf(time.getHour()));
        minute.setText(String.valueOf(time.getMinute()));

        Button confirmingButton = new Button("Potwierdź");
        GridPane.setConstraints(confirmingButton, 1, 6);
        confirmingButton.setOnAction(e -> {
            try {
                String updatedName = name.getText();
                MyDateFormat updatedDeadline = extractDateFromTextFields(textFields);
                MyTimeFormat updatedTime = extractTimeFromTextFields(textFields);

                if (updatedName.length() > 20) {
                    throw new IOException();
                }

                editedEvent.setName(updatedName);
                editedEvent.setDeadline(updatedDeadline);
                editedEvent.setTime(updatedTime);
                eventsManager.updateEvent(editedEvent);
                mainGUI.refresh();
                window.close();

            } catch (IOException | IllegalArgumentException exc) {
                Utilities.popUpErrorBox("Podane dane nie są poprawne.");
            }
        });

        GridPane layout = prepareGridPane();
        layout.getChildren().addAll(name, day, month, year, hour, minute, confirmingButton);
        layout.getChildren().addAll(labels);
        initWindow(window, layout, "Edycja wydarzenia", 250);
    }

    public void showUserAddingWindow() {
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
                mainGUI.refresh();
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

    public void showLoggingInWindow() {
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
                mainGUI.refresh();
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

    public void showHelpWindow() {
        String[] labelTexts = {
            "Dane wydarzenia:",
            "Nazwa - maks 20 znaków",
            "Dzień - liczba",
            "Miesiąc - liczba",
            "Rok - liczba",
            "Godzina - liczba",
            "Minuta - liczba",
            "",
            "Dane użytkownika:",
            "Login - maksymalnie 20 znaków",
            "Hasło - maksymalnie 30 znaków"
        };

        GridPane layout = prepareGridPane();

        for (int i = 0; i < labelTexts.length; i++) {
            Label tmpLab = new Label(labelTexts[i]);
            layout.add(tmpLab, 0, i);
        }

        Stage window = new Stage();
        initWindow(window, layout, "Pomoc", 300);
    }

    protected void initWindow(Stage window, Parent layout, String title, int height) {
        window.setScene(new Scene(layout, 300, height));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);
        window.show();
    }

    protected GridPane prepareGridPane() {
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(25));
        gp.setVgap(5);
        gp.setHgap(5);
        return gp;
    }

    protected Button prepareUserChoosingButton() {
        Button userChooser = new Button("Zaloguj się");
        userChooser.setOnAction(eh -> showLoggingInWindow());

        return userChooser;
    }

    protected Button prepareAddEventButton() {
        Button addEventButton = new Button("Dodaj wydarzenie");
        addEventButton.setOnAction(eh -> showEventAddingWindow());
        return addEventButton;
    }

    protected Button prepareAddUserButton() {
        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.setOnAction(eh -> showUserAddingWindow());
        return addUserButton;
    }

    protected Button prepareChangeViewButton(SceneCreator nextView) {
        Button changeViewButton = new Button("Zmień widok");
        changeViewButton.setOnAction(eh -> mainGUI.changeScene(nextView));
        return changeViewButton;
    }

    protected Button prepareHelpButton() {
        Button changeViewButton = new Button("Pomoc");
        changeViewButton.setOnAction(eh -> showHelpWindow());
        return changeViewButton;
    }

    private TextField[] prepareTextFields() {
        TextField name = new TextField();
        GridPane.setConstraints(name, 1, 0);

        TextField day = new TextField();
        GridPane.setConstraints(day, 1, 1);

        TextField month = new TextField();
        GridPane.setConstraints(month, 1, 2);

        TextField year = new TextField();
        GridPane.setConstraints(year, 1, 3);

        TextField hour = new TextField();
        GridPane.setConstraints(hour, 1, 4);

        TextField minute = new TextField();
        GridPane.setConstraints(minute, 1, 5);

        TextField[] textfields = {name, day, month, year, hour, minute};
        return textfields;
    }

    private Label[] prepareLabels() {
        Label name = new Label("Nazwa:");
        GridPane.setConstraints(name, 0, 0);

        Label day = new Label("Dzień:");
        GridPane.setConstraints(day, 0, 1);

        Label month = new Label("Miesiąc:");
        GridPane.setConstraints(month, 0, 2);

        Label year = new Label("Rok:");
        GridPane.setConstraints(year, 0, 3);

        Label hour = new Label("Godzina:");
        GridPane.setConstraints(hour, 0, 4);

        Label minute = new Label("Minuta:");
        GridPane.setConstraints(minute, 0, 5);

        Label[] labels = {name, day, month, year, hour, minute};
        return labels;
    }

    private MyDateFormat extractDateFromTextFields(TextField[] textfields) throws IOException {
        return new MyDateFormat(
                Integer.parseInt(textfields[1].getText()),
                Integer.parseInt(textfields[2].getText()),
                Integer.parseInt(textfields[3].getText())
        );
    }

    private MyTimeFormat extractTimeFromTextFields(TextField[] textfields) throws IOException {
        return new MyTimeFormat(
                Integer.parseInt(textfields[4].getText()),
                Integer.parseInt(textfields[5].getText())
        );
    }
}
