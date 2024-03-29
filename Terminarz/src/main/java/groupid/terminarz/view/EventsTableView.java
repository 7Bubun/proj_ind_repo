package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class EventsTableView extends SceneCreator {

    public EventsTableView(App mainGUI) throws SQLException {
        super(mainGUI);
    }

    @Override
    public Scene createScene() throws IOException, SQLException {
        ObservableList<MyEvent> obsList = FXCollections.observableArrayList(eventsManager.loadEvents(nameOfCurrentUser));
        TableView<MyEvent> layout = new TableView<>(obsList);
        layout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        layout.setFixedCellSize(50);

        TableColumn<MyEvent, String> name = new TableColumn<>("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MyEvent, MyDateFormat> deadline = new TableColumn<>("Termin");
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        TableColumn<MyEvent, MyTimeFormat> time = new TableColumn<>("Godzina");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<MyEvent, String> dayOfWeek = new TableColumn<>("Dzień tygodnia");
        dayOfWeek.setCellValueFactory(new PropertyValueFactory<>("dayOfTheWeek"));

        ObservableList<TableColumn<MyEvent, ?>> columns = layout.getColumns();
        columns.addAll(name, deadline, time, dayOfWeek);
        columns.stream().forEach(e -> {
            e.setMinWidth(199);
            e.setStyle("-fx-alignment: CENTER; -fx-font-size: large;");
        });

        Button editEventButton = new Button("Edytuj wydarzenie");
        editEventButton.setOnAction(e -> {
            MyEvent editedEvent = layout.getSelectionModel().getSelectedItem();

            if (editedEvent != null) {
                smallWindow = new EventEditingWindow(this);
                smallWindow.appear(editedEvent);
            }
        });

        Button deleteEventButton = new Button("Usuń wydarzenie");
        deleteEventButton.setOnAction(e -> {
            MyEvent event = layout.getSelectionModel().getSelectedItem();

            if (event != null) {
                eventsManager.deleteEvent(event.getId());
                mainGUI.refresh();
            }
        });

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setTop(new ToolBar(
                prepareAddEventButton(),
                editEventButton,
                deleteEventButton,
                prepareAddUserButton(),
                prepareUserChoosingButton(),
                prepareChangeViewButton(new MonthView(mainGUI)),
                prepareHelpButton()
        ));

        layout.getSortOrder().setAll(deadline, time);

        return new Scene(mainLayout, 800, 600);
    }
}
