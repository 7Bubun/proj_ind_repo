package groupid.terminarz.view;

import groupid.terminarz.App;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.Utilities;
import static groupid.terminarz.view.SceneCreator.eventsManager;
import static groupid.terminarz.view.SceneCreator.nameOfCurrentUser;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MonthView extends SceneCreator {

    public MonthView(App mainGUI) {
        super(mainGUI);
    }

    @Override
    public Scene createScene() {
        LocalDate today = LocalDate.now();
        Month currentMonth = today.getMonth();
        int currentYear = today.getYear();
        boolean leapYear = currentYear % 4 == 0;

        LocalDate firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1);
        DayOfWeek dayOfWeekOfFirstDay = DayOfWeek.from(firstDayOfMonth);
        int startingDay = dayOfWeekOfFirstDay.getValue();
        int monthsLength = currentMonth.length(leapYear);
        int daysLeft = monthsLength;

        List<MyEvent> events = eventsManager.loadEvents(nameOfCurrentUser);
        List<MyEvent> eventsFromCurrentMonth = events.stream().filter(
                e -> e.getDeadline().getMonth() == currentMonth.getValue()
        ).collect(toList());

        Map<Integer, List<MyEvent>> daysAndEvents = new HashMap<>();

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

        eventsFromCurrentMonth.stream().forEach(e -> {
            int day = e.getDeadline().getDay();
            numberLabels[day - 1].setTextFill(Color.GOLDENROD);
            numberLabels[day - 1].setOnMouseClicked(f -> showDaysEvents(daysAndEvents.get(day)));

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

        //layout.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setGridLinesVisible(true);
        layout.setPadding(new Insets(25));
        layout.getChildren().addAll(numberLabels);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setBottom(new Label(currentMonth.toString()));
        mainLayout.setTop(new ToolBar(
                prepareAddUserButton(),
                prepareUserChooser(),
                prepareChangeViewButton(new EventsTableView(mainGUI))
        ));

        return new Scene(mainLayout, 800, 600);
    }

    private void showDaysEvents(List<MyEvent> eventsOfDay) {
        ObservableList<String> obsListOfEvents = FXCollections.observableArrayList();
        eventsOfDay.stream().forEach(e -> obsListOfEvents.add(e.toString()));

        ListView<String> layout = new ListView(obsListOfEvents);

        Scene scene = new Scene(layout, 400, 300);
        Stage window = new Stage();
        window.setScene(scene);
        window.show();
    }
}
