package groupid.terminarz.view;

import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import java.io.IOException;
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
        ObservableList<MyEvent> lista = FXCollections.observableArrayList();

        //temporaryyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
        try {
            lista.add(new MyEvent("chillera", new MyDateFormat(30, 9, 2021), new MyTimeFormat(15, 0)));
            lista.add(new MyEvent("utopia", new MyDateFormat(5, 5, 1999), new MyTimeFormat(3, 9)));
            lista.add(new MyEvent("tere", new MyDateFormat(11, 9, 2001), new MyTimeFormat(15, 45)));
            lista.add(new MyEvent("fere", new MyDateFormat(12, 5, 2011), new MyTimeFormat(15, 123)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        TableView<MyEvent> layout = new TableView<>(lista);

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
