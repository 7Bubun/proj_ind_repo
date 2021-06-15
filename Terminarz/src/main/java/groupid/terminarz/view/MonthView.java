package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import groupid.terminarz.Utilities;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import static groupid.terminarz.view.SceneCreator.nameOfCurrentUser;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MonthView extends SceneCreator {

    private static int certainMonth = LocalDate.now().getMonthValue();
    private static int certainYear = LocalDate.now().getYear();
    private Stage eventsOfDayWindow;
    private List<MyEvent> eventsOfCertainDay;
    private MyDateFormat dateOfCertainDay;

    public MonthView(App mainGUI) throws SQLException {
        super(mainGUI);
    }

    @Override
    public Scene createScene() throws IOException, SQLException {
        boolean leapYear = certainYear % 4 == 0;

        if (certainMonth > 12) {
            certainMonth = 1;
            certainYear++;

        } else if (certainMonth <= 0) {
            certainMonth = 12;
            certainYear--;
        }

        Month certainMonthObject = Month.of(certainMonth);
        LocalDate firstDayOfMonth = LocalDate.of(certainYear, certainMonth, 1);
        DayOfWeek dayOfWeekOfFirstDay = DayOfWeek.from(firstDayOfMonth);
        int startingDay = dayOfWeekOfFirstDay.getValue();
        int monthsLength = certainMonthObject.length(leapYear);
        int daysLeft = monthsLength;

        List<MyEvent> events = eventsManager.loadEvents(nameOfCurrentUser);
        List<MyEvent> eventsFromCertainMonth = events.stream().filter(
                e -> e.getDeadline().getMonth() == certainMonth
                && e.getDeadline().getYear() == certainYear
        ).collect(toList());

        GridPane layout = new GridPane();
        Label[] numberLabels = new Label[monthsLength];
        String[] daysOfWeek = {
            "Poniedziałek",
            "Wtorek",
            "Środa",
            "Czwartek",
            "Piątek",
            "Sobota",
            "Niedziela"
        };

        for (int i = 1; i <= monthsLength; i++) {
            Label lab = new Label(Utilities.addSpace(i));
            lab.setMinSize(105, 80);
            lab.setAlignment(Pos.TOP_LEFT);
            numberLabels[i - 1] = lab;
        }

        Map<Integer, List<MyEvent>> daysAndEvents = new HashMap<>();
        eventsFromCertainMonth.stream().forEach(e -> {
            int day = e.getDeadline().getDay();
            numberLabels[day - 1].setTextFill(Color.RED);
            numberLabels[day - 1].setOnMouseClicked(f -> {
                eventsOfCertainDay = daysAndEvents.get(day);
                dateOfCertainDay = eventsOfCertainDay.get(0).getDeadline();

                try {
                    showEventsOfDay();

                } catch (IOException | SQLException exc) {
                    Utilities.popUpErrorBox("Nie udało się załadować danych.");
                }
            });

            if (daysAndEvents.containsKey(day)) {
                daysAndEvents.get(day).add(e);

            } else {
                List<MyEvent> list = new ArrayList<>();
                list.add(e);
                daysAndEvents.put(day, list);
            }
        });

        for (int i = 0; i < 7; i++) {
            Label tmpLab = new Label(Utilities.addSpace(daysOfWeek[i]));
            GridPane.setConstraints(tmpLab, i, 0);
            layout.getChildren().add(tmpLab);
        }

        for (int i = startingDay - 1; i < 7; i++) {
            GridPane.setConstraints(numberLabels[monthsLength - daysLeft], i, 1);
            daysLeft--;
        }

        for (int i = 2; i < 7 && daysLeft > 0; i++) {
            for (int j = 0; j < 7 && daysLeft > 0; j++) {
                GridPane.setConstraints(numberLabels[monthsLength - daysLeft], j, i);
                daysLeft--;
            }
        }

        layout.setGridLinesVisible(true);
        layout.setPadding(new Insets(10, 30, 5, 30));
        layout.getChildren().addAll(numberLabels);

        Button previousMonthButton = new Button("<");
        previousMonthButton.setOnAction(eh -> {
            certainMonth--;
            mainGUI.refresh();
        });

        Button nextMonthButton = new Button(">");
        nextMonthButton.setOnAction(eh -> {
            certainMonth++;
            mainGUI.refresh();
        });

        Label monthNameLabel = new Label(Utilities.translateMonth(certainMonthObject) + " " + certainYear);
        monthNameLabel.setMinWidth(300);
        monthNameLabel.setStyle("-fx-alignment: CENTER; -fx-font-size: 32px;");
        FlowPane bottomLayout = new FlowPane();
        bottomLayout.setAlignment(Pos.CENTER);
        bottomLayout.getChildren().addAll(previousMonthButton, monthNameLabel, nextMonthButton);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setBottom(bottomLayout);
        mainLayout.setTop(new ToolBar(
                prepareAddEventButton(),
                prepareAddUserButton(),
                prepareUserChoosingButton(),
                prepareChangeViewButton(new EventsTableView(mainGUI)),
                prepareHelpButton()
        ));

        return new Scene(mainLayout, 800, 600);
    }

    private void showEventsOfDay() throws IOException, SQLException {
        BorderPane mainLayout = prepareEventsOfDayLayout();
        MyEvent firstEvent = eventsOfCertainDay.get(0);

        Scene scene = new Scene(mainLayout, 300, 250);
        eventsOfDayWindow = new Stage();
        eventsOfDayWindow.setTitle("Wydarzenia " + firstEvent.getDeadline());
        eventsOfDayWindow.setScene(scene);
        eventsOfDayWindow.initModality(Modality.APPLICATION_MODAL);
        eventsOfDayWindow.setResizable(false);
        eventsOfDayWindow.show();
    }

    private BorderPane prepareEventsOfDayLayout() throws IOException, SQLException {
        ObservableList<MyEvent> obsListOfEvents = FXCollections.observableArrayList(eventsOfCertainDay);
        TableView<MyEvent> centerLayout = new TableView<>(obsListOfEvents);
        centerLayout.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MyEvent, String> name = new TableColumn<>("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setMinWidth(195);
        name.setStyle("-fx-alignment: CENTER;");

        TableColumn<MyEvent, MyTimeFormat> hour = new TableColumn<>("Godzina");
        hour.setCellValueFactory(new PropertyValueFactory<>("time"));
        hour.setMinWidth(103);
        hour.setStyle("-fx-alignment: CENTER;");

        centerLayout.getColumns().addAll(hour, name);
        centerLayout.getSortOrder().setAll(hour);

        Button addEventButton = new Button("Dodaj");
        addEventButton.setOnAction(eh -> {
            smallWindow = new DailyEventsAddingWindow(this);
            smallWindow.appear();
        });

        Button editEventButton = new Button("Edytuj");
        editEventButton.setOnAction(eh -> {
            MyEvent editedEvent = centerLayout.getSelectionModel().getSelectedItem();

            if (editedEvent != null) {
                smallWindow = new DailyEventsEditingWindow(this);
                smallWindow.appear(editedEvent);
            }
        });

        Button deleteEventButton = new Button("Usuń");
        deleteEventButton.setOnAction(eh -> {
            MyEvent event = centerLayout.getSelectionModel().getSelectedItem();

            if (event != null) {
                try {
                    eventsManager.deleteEvent(event.getId());
                    refreshEventsOfDay();

                } catch (IOException | SQLException e) {
                    Utilities.popUpErrorBox("Wystąpił błąd.");
                }
            }
        });

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(centerLayout);
        mainLayout.setTop(new ToolBar(addEventButton, editEventButton, deleteEventButton));
        return mainLayout;
    }

    void refreshEventsOfDay() throws IOException, SQLException {
        eventsOfCertainDay = eventsManager.loadEvents(nameOfCurrentUser, dateOfCertainDay);

        if (!eventsOfCertainDay.isEmpty()) {
            BorderPane mainLayout = prepareEventsOfDayLayout();
            eventsOfDayWindow.setScene(new Scene(mainLayout, 300, 250));
            return;
        }

        eventsOfDayWindow.close();
        mainGUI.refresh();
    }

    public MyDateFormat getDateOfCertainDay() {
        return dateOfCertainDay;
    }
}
