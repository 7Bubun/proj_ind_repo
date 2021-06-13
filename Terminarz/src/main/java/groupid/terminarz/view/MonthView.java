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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
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
            numberLabels[day - 1].setTextFill(Color.GOLDENROD);
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
        layout.setPadding(new Insets(10));
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
        monthNameLabel.setMinWidth(790);
        monthNameLabel.setStyle("-fx-alignment: CENTER; -fx-font-size: 32px;");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setBottom(monthNameLabel);
        mainLayout.setLeft(previousMonthButton);
        mainLayout.setRight(nextMonthButton);
        mainLayout.setTop(new ToolBar(
                prepareAddEventButton(),
                prepareAddUserButton(),
                prepareUserChoosingButton(),
                prepareChangeViewButton(new EventsTableView(mainGUI)),
                prepareHelpButton()
        ));

        return new Scene(mainLayout, 800, 600);
    }

    @Override
    public void showEventAddingWindow() {
        Stage window = new Stage();
        GridPane layout = prepareGridPane();
        Control[] controls = prepareControls();

        TextField nameField = (TextField) controls[3];
        TextField hourField = (TextField) controls[4];
        TextField minuteField = (TextField) controls[5];
        Button confirmingButton = (Button) controls[6];

        confirmingButton.setOnAction(eh -> {
            try {
                MyTimeFormat time = new MyTimeFormat(
                        Integer.parseInt(hourField.getText()),
                        Integer.parseInt(minuteField.getText())
                );

                eventsManager.addEvent(nameField.getText(), dateOfCertainDay, time, nameOfCurrentUser);
                mainGUI.refresh();
                refreshEventsOfDay();
                window.close();

            } catch (IOException | SQLException e) {
                Utilities.popUpErrorBox("Nie udało się załadować danych.");
            }
        });

        layout.getChildren().addAll(controls);
        initWindow(window, layout, "Dodawanie wydarzenia", 150);
    }

    @Override
    public void showEventEditingWindow(MyEvent editedEvent) {
        Stage window = new Stage();
        GridPane layout = prepareGridPane();
        Control[] controls = prepareControls();

        TextField nameField = (TextField) controls[3];
        nameField.setText(editedEvent.getName());

        TextField hourField = (TextField) controls[4];
        hourField.setText(String.valueOf(editedEvent.getTime().getHour()));

        TextField minuteField = (TextField) controls[5];
        minuteField.setText(String.valueOf(editedEvent.getTime().getMinute()));

        Button confirmingButton = (Button) controls[6];

        confirmingButton.setOnAction(eh -> {
            try {
                MyTimeFormat time = new MyTimeFormat(
                        Integer.parseInt(hourField.getText()),
                        Integer.parseInt(minuteField.getText())
                );

                editedEvent.setName(nameField.getText());
                editedEvent.setTime(time);

                eventsManager.updateEvent(editedEvent);
                mainGUI.refresh();
                refreshEventsOfDay();
                window.close();

            } catch (IOException | SQLException e) {
                Utilities.popUpErrorBox("Nie udało się załadować danych.");
            }
        });

        layout.getChildren().addAll(controls);
        initWindow(window, layout, "Dodawanie wydarzenia", 150);
    }

    @Override
    protected Button prepareAddEventButton() {
        Button aeButton = new Button("Dodaj wydarzenie");
        aeButton.setOnAction(eh -> super.showEventAddingWindow());
        return aeButton;
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

        Button addEventButton = new Button("Dodaj");
        addEventButton.setOnAction(eh -> {
            showEventAddingWindow();
        });

        Button editEventButton = new Button("Edytuj");
        editEventButton.setOnAction(eh -> {
            MyEvent editedEvent = centerLayout.getSelectionModel().getSelectedItem();
            showEventEditingWindow(editedEvent);
        });

        Button deleteEventButton = new Button("Usuń");
        deleteEventButton.setOnAction(eh -> {
            MyEvent editedEvent = centerLayout.getSelectionModel().getSelectedItem();

            try {
                eventsManager.deleteEvent(editedEvent.getId());
                refreshEventsOfDay();

            } catch (IOException | SQLException e) {
                Utilities.popUpErrorBox("Wystąpił błąd.");
            }
        });

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(centerLayout);
        mainLayout.setTop(new ToolBar(addEventButton, editEventButton, deleteEventButton));
        return mainLayout;
    }

    private void refreshEventsOfDay() throws IOException, SQLException {
        eventsOfCertainDay = eventsManager.loadEvents(nameOfCurrentUser, dateOfCertainDay);

        if (!eventsOfCertainDay.isEmpty()) {
            BorderPane mainLayout = prepareEventsOfDayLayout();
            eventsOfDayWindow.setScene(new Scene(mainLayout, 300, 250));
            return;
        }

        eventsOfDayWindow.close();
        mainGUI.refresh();
    }

    private Control[] prepareControls() {
        Label nameLabel = new Label("Nazwa:");
        Label hourLabel = new Label("Godzina:");
        Label minuteLabel = new Label("Minuta:");

        TextField nameTextField = new TextField();
        TextField hourTextField = new TextField();
        TextField minuteTextField = new TextField();

        Button confirmingButton = new Button("Potwierdź");

        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(hourLabel, 0, 1);
        GridPane.setConstraints(minuteLabel, 0, 2);
        GridPane.setConstraints(nameTextField, 1, 0);
        GridPane.setConstraints(hourTextField, 1, 1);
        GridPane.setConstraints(minuteTextField, 1, 2);
        GridPane.setConstraints(confirmingButton, 1, 3);

        return new Control[]{
            nameLabel,
            hourLabel,
            minuteLabel,
            nameTextField,
            hourTextField,
            minuteTextField,
            confirmingButton
        };
    }
}
