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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class EventsTableView extends SceneCreator {

    public EventsTableView(App mainGUI) {
        super(mainGUI);
    }

    @Override
    public Scene createScene() {
        ObservableList<MyEvent> obsList = FXCollections.observableArrayList(eventsManager.loadEvents());
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

        Label top = new Label("Góra");
        Label bottom = new Label("Dół");
        Label left = new Label("Lewo");
        Button right = new Button("Prawo");
        right.setOnAction(e -> {
            showEditingOrAddingWindow();
        });

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setTop(top);
        mainLayout.setBottom(bottom);
        mainLayout.setLeft(left);
        mainLayout.setRight(right);

        return new Scene(mainLayout, 800, 600);
    }
    /*
    private void delete(TableView<MyEvent> layout) {
        MyEvent itemToDelete = layout.getSelectionModel().getSelectedItem();
        layout.getItems().removeAll(itemToDelete);
    }
    */
}
