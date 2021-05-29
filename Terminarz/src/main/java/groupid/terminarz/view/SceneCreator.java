package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.*;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class SceneCreator {

    protected static DataBaseManager eventsManager;
    protected static String nameOfCurrentUser = "-";
    protected final App mainGUI;

    public SceneCreator(App mainGUI) {
        this.mainGUI = mainGUI;

        if (eventsManager == null) {
            eventsManager = new DataBaseManager();
        }
    }

    public Scene createScene() {
        return new Scene(new StackPane(), 20, 20);
    }

    public void showEditingOrAddingWindow(MyEvent editedEvent) {
        Stage window = new Stage();
        FlowPane layout = new FlowPane();

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

        if (editedEvent != null) {
            MyDateFormat deadline = editedEvent.getDeadline();
            MyTimeFormat time = editedEvent.getTime();

            name.setText(editedEvent.getName());
            day.setText(String.valueOf(deadline.getDay()));
            month.setText(String.valueOf(deadline.getMonth()));
            year.setText(String.valueOf(deadline.getYear()));
            hour.setText(String.valueOf(time.getHour()));
            minute.setText(String.valueOf(time.getMinute()));
        }

        Button confirmingButton = new Button("Potwierdź");
        confirmingButton.setOnAction(e -> {
            try {
                MyDateFormat date = new MyDateFormat(
                        Integer.parseInt(day.getText()),
                        Integer.parseInt(month.getText()),
                        Integer.parseInt(year.getText())
                );

                MyTimeFormat time = new MyTimeFormat(
                        Integer.parseInt(hour.getText()),
                        Integer.parseInt(minute.getText())
                );

                if (editedEvent == null) {
                    eventsManager.addEvent(name.getText(), date, time, nameOfCurrentUser);

                } else {
                    editedEvent.setName(name.getText());
                    editedEvent.setDeadline(date);
                    editedEvent.setTime(time);
                    eventsManager.updateEvent(editedEvent);
                }

                mainGUI.refresh();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            window.close();
        });

        layout.getChildren().addAll(name, day, month, year, hour, minute, confirmingButton);
        window.setScene(new Scene(layout, 400, 200));
        window.setAlwaysOnTop(true);
        window.show();
    }

    public void showUserAddingWindow() {
        Stage window = new Stage();
        FlowPane layout = new FlowPane();

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

        layout.getChildren().addAll(usernameTextField, passwordTextField, passwordAgainTextField, confirmButton);
        window.setScene(new Scene(layout, 400, 200));
        window.show();
    }

    public void showLoggingInWindow(String username) {
        Stage window = new Stage();
        FlowPane layout = new FlowPane();

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
                Alert messageBox = new Alert(Alert.AlertType.ERROR);
                messageBox.setContentText("To się nazywa cyberprzestępstwo! xD");
                messageBox.show();
            }
        });

        layout.getChildren().addAll(passwordTextField, confirmButton);
        window.setScene(new Scene(layout, 400, 200));
        window.show();
    }
}
