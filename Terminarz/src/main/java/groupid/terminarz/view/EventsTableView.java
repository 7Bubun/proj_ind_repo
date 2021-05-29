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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class EventsTableView extends SceneCreator {

    public EventsTableView(App mainGUI) {
        super(mainGUI);
    }

    @Override
    public Scene createScene() {
        ObservableList<MyEvent> obsList = FXCollections.observableArrayList(eventsManager.loadEvents(nameOfCurrentUser));
        TableView<MyEvent> layout = new TableView<>(obsList);

        TableColumn<MyEvent, Integer> identity = new TableColumn<>("ID");
        identity.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<MyEvent, String> name = new TableColumn<>("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MyEvent, MyDateFormat> deadline = new TableColumn<>("Termin");
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        TableColumn<MyEvent, MyTimeFormat> time = new TableColumn<>("Hour");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        layout.getColumns().addAll(identity, name, deadline, time);

        ObservableList<String> usernames = FXCollections.observableArrayList(eventsManager.loadUsernames());
        ComboBox<String> userChooser = new ComboBox<>(usernames);
        
        if(!nameOfCurrentUser.equals("-")) {
            userChooser.setValue(nameOfCurrentUser);
        }
        
        userChooser.setOnAction(e -> {
            nameOfCurrentUser = userChooser.getValue();
            mainGUI.refresh();
        });

        Button addEventButton = new Button("Dodaj wydarzenie");
        addEventButton.setOnAction(e -> showEditingOrAddingWindow());

        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.setOnAction(e -> showUserAddingWindow());

        //menubar in future?
        FlowPane top = new FlowPane(addEventButton, addUserButton, userChooser);
        top.minHeight(100);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setTop(top);

        return new Scene(mainLayout, 800, 600);
    }
    /*
    private void delete(TableView<MyEvent> layout) {
        MyEvent itemToDelete = layout.getSelectionModel().getSelectedItem();
        layout.getItems().removeAll(itemToDelete);
    }
     */
}
