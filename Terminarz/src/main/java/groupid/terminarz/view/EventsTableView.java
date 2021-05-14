package groupid.terminarz.view;

import groupid.terminarz.logic.DataBaseManager;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import static groupid.terminarz.view.SceneMaker.eventsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class EventsTableView extends SceneMaker {

    @Override
    public Scene makeScene() {
        if (eventsManager == null) {
            eventsManager = new DataBaseManager();
        }

        ObservableList<MyEvent> obsList = FXCollections.observableArrayList(eventsManager.getAllEvents().values());
        //MyEvent[] events = (MyEvent[]) eventsManager.getAllEvents().values().toArray();
        //for (MyEvent e : events) {
        //    obsList.add(e);
        //}

        TableView<MyEvent> layout = new TableView<>(obsList);

        TableColumn<MyEvent, Integer> identity = new TableColumn<>("ID");
        identity.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<MyEvent, String> name = new TableColumn<>("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MyEvent, MyDateFormat> deadline = new TableColumn<>("Termin");
        deadline.setCellValueFactory(new PropertyValueFactory<>("termin"));

        TableColumn<MyEvent, MyTimeFormat> time = new TableColumn<>("Hour");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        layout.getColumns().addAll(identity, name, deadline, time);

        Label top = new Label("Góra");
        Label bottom = new Label("Dół");
        Label left = new Label("Lewo");
        Button right = new Button("Prawo");
        right.setOnAction(e -> delete(layout));

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setTop(top);
        mainLayout.setBottom(bottom);
        mainLayout.setLeft(left);
        mainLayout.setRight(right);

        return new Scene(mainLayout, 800, 600);
    }

    private void delete(javafx.scene.control.TableView<MyEvent> layout) {
        MyEvent itemToDelete = layout.getSelectionModel().getSelectedItem();
        layout.getItems().removeAll(itemToDelete);
    }

}
