package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class EventsTableView extends SceneCreator {

    public EventsTableView(App mainGUI) {
        super(mainGUI);
    }

    @Override
    public Scene createScene() {
        ObservableList<MyEvent> obsList = FXCollections.observableArrayList(eventsManager.loadEvents(nameOfCurrentUser));
        TableView<MyEvent> layout = new TableView<>(obsList);
        layout.setFixedCellSize(50);

        TableColumn<MyEvent, String> name = new TableColumn<>("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MyEvent, MyDateFormat> deadline = new TableColumn<>("Termin");
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        TableColumn<MyEvent, MyTimeFormat> time = new TableColumn<>("Godzina");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<MyEvent, String> dayOfWeek = new TableColumn<>("Dzień tygodnia");
        dayOfWeek.setCellValueFactory(new PropertyValueFactory<>("dayOfTheWeek"));

        layout.getColumns().addAll(name, deadline, time, dayOfWeek);

        for (TableColumn col : layout.getColumns()) {
            col.setMinWidth(200);
            col.setStyle("-fx-alignment: CENTER; -fx-font-size: large;");
        }

        ObservableList<String> usernames = FXCollections.observableArrayList(eventsManager.loadUsernames());
        ComboBox<String> userChooser = new ComboBox<>(usernames);

        if (!nameOfCurrentUser.equals("-")) {
            userChooser.setValue(nameOfCurrentUser);
        }

        userChooser.setOnAction(e -> {
            showLoggingInWindow(userChooser.getValue());
            mainGUI.refresh();
        });

        Button addEventButton = new Button("Dodaj wydarzenie");
        addEventButton.setOnAction(e -> showEditingOrAddingWindow(null));

        Button editEventButton = new Button("Edytuj wydarzenie");
        editEventButton.setOnAction(e -> {
            MyEvent editedEvent = layout.getSelectionModel().getSelectedItem();
            showEditingOrAddingWindow(editedEvent);
        });

        Button deleteEventButton = new Button("Usuń wydarzenie");
        deleteEventButton.setOnAction(e -> {
            MyEvent editedEvent = layout.getSelectionModel().getSelectedItem();

            if (editedEvent != null) {
                eventsManager.deleteEvent(editedEvent.getId());
                mainGUI.refresh();
            }
        });

        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.setOnAction(e -> showUserAddingWindow());

        Button changeViewButton = new Button("Zmień widok");
        changeViewButton.setOnAction(e -> mainGUI.changeScene(new MonthView(mainGUI)));

        Label usersLabel = new Label("Użytkownik:");

        ToolBar top = new ToolBar(
                addEventButton,
                editEventButton,
                deleteEventButton,
                addUserButton,
                changeViewButton,
                usersLabel,
                userChooser
        );

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setTop(top);

        return new Scene(mainLayout, 800, 600);
    }
}
