package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.*;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
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

        TextField[] textFields = prepareTextFields();
        TextField name = textFields[0];
        TextField day = textFields[1];
        TextField month = textFields[2];
        TextField year = textFields[3];
        TextField hour = textFields[4];
        TextField minute = textFields[5];

        Button confirmingButton = new Button("Potwierdź");
        confirmingButton.setOnAction(e -> {
            try {
                MyDateFormat updatedDeadline = extractDateFromTextFields(textFields);
                MyTimeFormat updatedTime = extractTimeFromTextFields(textFields);

                eventsManager.addEvent(name.getText(), updatedDeadline, updatedTime, nameOfCurrentUser);
                mainGUI.refresh();
                window.close();

            } catch (IOException | NumberFormatException exc) {
                Utilities.popUpErrorBox("Podane dane nie są poprawne.");
            }
        });

        FlowPane layout = new FlowPane();
        layout.getChildren().addAll(name, day, month, year, hour, minute, confirmingButton);
        initWindow(window, layout);
    }

    public void showEventEditingWindow(MyEvent editedEvent) {
        Stage window = new Stage();

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
        confirmingButton.setOnAction(e -> {
            try {
                MyDateFormat updatedDeadline = extractDateFromTextFields(textFields);
                MyTimeFormat updatedTime = extractTimeFromTextFields(textFields);

                editedEvent.setName(name.getText());
                editedEvent.setDeadline(updatedDeadline);
                editedEvent.setTime(updatedTime);
                eventsManager.updateEvent(editedEvent);
                mainGUI.refresh();

            } catch (IOException ioe) {
                Utilities.popUpErrorBox("Podane dane nie są poprawne.");
            }

            window.close();
        });

        FlowPane layout = new FlowPane();
        layout.getChildren().addAll(name, day, month, year, hour, minute, confirmingButton);
        initWindow(window, layout);
    }

    public void showUserAddingWindow() {
        Stage window = new Stage();

        TextField usernameTextField = new TextField();
        TextField passwordTextField = new TextField();
        TextField passwordAgainTextField = new TextField();
        Button confirmButton = new Button("Zatwierdź");

        usernameTextField.setPromptText("Login");
        passwordTextField.setPromptText("Hasło");
        passwordAgainTextField.setPromptText("Powtórz hasło");

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

        FlowPane layout = new FlowPane();
        layout.getChildren().addAll(usernameTextField, passwordTextField, passwordAgainTextField, confirmButton);
        initWindow(window, layout);
    }

    public void showLoggingInWindow(String username) {
        Stage window = new Stage();

        TextField passwordTextField = new TextField();
        Button confirmButton = new Button("Zatwierdź");

        passwordTextField.setPromptText("Hasło");

        confirmButton.setOnAction(e -> {
            String password = passwordTextField.getText();

            if (eventsManager.checkPassword(username, password)) {
                nameOfCurrentUser = username;
                mainGUI.refresh();
                window.close();

            } else {
                Utilities.popUpErrorBox("Podane dane logowania są niepoprawne.");
            }
        });

        FlowPane layout = new FlowPane();
        layout.getChildren().addAll(passwordTextField, confirmButton);
        initWindow(window, layout);
    }

    protected void initWindow(Stage window, Parent layout) {
        window.setScene(new Scene(layout, 400, 200));
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
    }

    protected ComboBox<String> prepareUserChooser() throws SQLException {
        ObservableList<String> usernames = FXCollections.observableArrayList(eventsManager.loadUsernames());
        ComboBox<String> userChooser = new ComboBox<>(usernames);
        userChooser.setPromptText("Użytkownik");

        if (!nameOfCurrentUser.equals("-")) {
            userChooser.setValue(nameOfCurrentUser);
        }

        userChooser.setOnAction(e -> {
            showLoggingInWindow(userChooser.getValue());
            mainGUI.refresh();
        });

        return userChooser;
    }

    protected Button prepareAddUserButton() {
        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.setOnAction(e -> showUserAddingWindow());
        return addUserButton;
    }

    protected Button prepareChangeViewButton(SceneCreator nextView) {
        Button changeViewButton = new Button("Zmień widok");
        changeViewButton.setOnAction(e -> mainGUI.changeScene(nextView));
        return changeViewButton;
    }

    private TextField[] prepareTextFields() {
        TextField name = new TextField();
        name.setPromptText("nazwa");

        TextField day = new TextField();
        day.setPromptText("dzień");

        TextField month = new TextField();
        month.setPromptText("miesiąc");

        TextField year = new TextField();
        year.setPromptText("rok");

        TextField hour = new TextField();
        hour.setPromptText("godzina");

        TextField minute = new TextField();
        minute.setPromptText("minuta");

        TextField[] textfields = {name, day, month, year, hour, minute};
        return textfields;
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
