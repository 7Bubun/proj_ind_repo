package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import groupid.terminarz.logic.Utilities;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MonthView extends SceneCreator {

    private static int monthDiff = 0;
    private Stage eventsOfDayWindow;
    private List<MyEvent> eventsOnCertainDay;
    private MyDateFormat dateOfCertainDay;

    public MonthView(App mainGUI) throws SQLException {
        super(mainGUI);
    }

    @Override
    public Scene createScene() throws IOException, SQLException {
        LocalDate today = LocalDate.now();
        Month currentMonth = today.getMonth();
        int currentYear = today.getYear();
        boolean leapYear = currentYear % 4 == 0;
        int valueOfCertainMonth = currentMonth.getValue() + monthDiff;
        Month certainMonth = LocalDate.of(currentYear, valueOfCertainMonth, 1).getMonth();

        LocalDate firstDayOfMonth = LocalDate.of(currentYear, valueOfCertainMonth, 1);
        DayOfWeek dayOfWeekOfFirstDay = DayOfWeek.from(firstDayOfMonth);
        int startingDay = dayOfWeekOfFirstDay.getValue();
        int monthsLength = certainMonth.length(leapYear);
        int daysLeft = monthsLength;

        List<MyEvent> events = eventsManager.loadEvents(nameOfCurrentUser);
        List<MyEvent> eventsFromCertainMonth = events.stream().filter(
                e -> e.getDeadline().getMonth() == certainMonth.getValue()
                && e.getDeadline().getYear() == currentYear
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
                eventsOnCertainDay = daysAndEvents.get(day);
                dateOfCertainDay = eventsOnCertainDay.get(0).getDeadline();

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
            monthDiff--;
            mainGUI.refresh();
        });

        Button nextMonthButton = new Button(">");
        nextMonthButton.setOnAction(eh -> {
            monthDiff++;
            mainGUI.refresh();
        });

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setBottom(new Label(certainMonth.toString()));
        mainLayout.setLeft(previousMonthButton);
        mainLayout.setRight(nextMonthButton);
        mainLayout.setTop(new ToolBar(
                prepareAddEventButton(),
                prepareAddUserButton(),
                prepareUserChooser(),
                prepareChangeViewButton(new EventsTableView(mainGUI))
        ));

        return new Scene(mainLayout, 800, 600);
    }

    @Override
    public void showEventAddingWindow() {
        Stage window = new Stage();
        FlowPane layout = new FlowPane();

        TextField name = new TextField();
        name.setPromptText("nazwa");

        TextField hour = new TextField();
        hour.setPromptText("godzina");

        TextField minute = new TextField();
        minute.setPromptText("minuta");

        Button confirmingButton = new Button("Potwierdź");
        confirmingButton.setOnAction(eh -> {
            MyTimeFormat time = new MyTimeFormat(
                    Integer.parseInt(hour.getText()),
                    Integer.parseInt(minute.getText())
            );

            try {
                eventsManager.addEvent(name.getText(), dateOfCertainDay, time, nameOfCurrentUser);
                mainGUI.refresh();
                refreshEventsOfDay();
                window.close();

            } catch (IOException | SQLException e) {
                Utilities.popUpErrorBox("Nie udało się załadować danych.");
            }
        });

        layout.getChildren().addAll(name, hour, minute, confirmingButton);
        initWindow(window, layout, "Dodawanie wydarzenia", 250);
    }

    @Override
    protected Button prepareAddEventButton() {
        Button aeButton = new Button("Dodaj wydarzenie");
        aeButton.setOnAction(eh -> super.showEventAddingWindow());
        return aeButton;
    }

    private void showEventsOfDay() throws IOException, SQLException {
        BorderPane mainLayout = prepareEventsOfDayLayout();
        MyEvent firstEvent = eventsOnCertainDay.get(0);

        Scene scene = new Scene(mainLayout, 400, 300);
        eventsOfDayWindow = new Stage();
        eventsOfDayWindow.setTitle("Wydarzenia " + firstEvent.getDeadline());
        eventsOfDayWindow.setScene(scene);
        eventsOfDayWindow.initModality(Modality.APPLICATION_MODAL);
        eventsOfDayWindow.show();
    }

    private BorderPane prepareEventsOfDayLayout() throws IOException, SQLException {
        ObservableList<String> obsListOfEvents = FXCollections.observableArrayList();
        eventsOnCertainDay.stream().forEach(e -> obsListOfEvents.add(e.toString()));
        ListView<String> centerLayout = new ListView(obsListOfEvents);

        Button addEventButton = new Button("Dodaj");
        addEventButton.setOnAction(eh -> {
            showEventAddingWindow();
        });

        Button editEventButton = new Button("Edytuj");
        editEventButton.setOnAction(eh -> {
            MyEvent editedEvent = eventsOnCertainDay.get(centerLayout.getSelectionModel().getSelectedIndex());
            showEventEditingWindow(editedEvent);
        });

        Button deleteEventButton = new Button("Usuń");
        deleteEventButton.setOnAction(eh -> {
            MyEvent editedEvent = eventsOnCertainDay.get(centerLayout.getSelectionModel().getSelectedIndex());

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
        eventsOnCertainDay = eventsManager.loadEvents(nameOfCurrentUser, dateOfCertainDay);

        if (!eventsOnCertainDay.isEmpty()) {
            BorderPane mainLayout = prepareEventsOfDayLayout();
            eventsOfDayWindow.setScene(new Scene(mainLayout, 400, 300));
            return;
        }

        eventsOfDayWindow.close();
        mainGUI.refresh();
    }
}
