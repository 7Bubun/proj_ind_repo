package groupid.terminarz.view;

import groupid.terminarz.App;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

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

        GridPane layout = new GridPane();
        Label[] numberLabels = new Label[monthsLength];
        Label[] daysOfWeek = new Label[7];

        daysOfWeek[0] = new Label("Poniedziałek");
        daysOfWeek[1] = new Label("Wtorek");
        daysOfWeek[2] = new Label("Środa");
        daysOfWeek[3] = new Label("Czwartek");
        daysOfWeek[4] = new Label("Piątek");
        daysOfWeek[5] = new Label("Sobota");
        daysOfWeek[6] = new Label("Niedziela");

        for (int i = 1; i <= monthsLength; i++) {
            Label lab = new Label(String.valueOf(i));
            lab.setMinSize(80, 60);
            numberLabels[i - 1] = lab;
        }

        for (int i = 0; i < 7; i++) {
            GridPane.setConstraints(daysOfWeek[i], i, 0);
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
        layout.setPadding(new Insets(25));
        layout.getChildren().addAll(daysOfWeek);
        layout.getChildren().addAll(numberLabels);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(layout);
        mainLayout.setTop(new Label(currentMonth.toString()));

        return new Scene(mainLayout, 800, 600);
    }
}
