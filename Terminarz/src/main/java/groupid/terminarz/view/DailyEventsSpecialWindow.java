package groupid.terminarz.view;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public abstract class DailyEventsSpecialWindow extends SpecialWindow {

    protected MonthView monthView;

    public DailyEventsSpecialWindow(MonthView monthView) {
        super(monthView);
        this.monthView = monthView;
    }

    protected Control[] prepareControls() {
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
