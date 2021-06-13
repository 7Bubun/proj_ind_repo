package groupid.terminarz.view;

import groupid.terminarz.logic.MyDateFormat;
import groupid.terminarz.logic.MyEvent;
import groupid.terminarz.logic.MyTimeFormat;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class SpecialWindow {

    protected SceneCreator sceneCreator;

    public SpecialWindow(SceneCreator creator) {
        sceneCreator = creator;
    }

    public void appear() {
        throw new UnsupportedOperationException();
    }

    public void appear(MyEvent event) {
        throw new UnsupportedOperationException();
    }

    protected void initWindow(Stage window, Parent layout, String title, int height) {
        window.setScene(new Scene(layout, 300, height));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);
        window.show();
    }

    protected GridPane prepareGridPane() {
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(25));
        gp.setVgap(5);
        gp.setHgap(5);
        return gp;
    }

    protected TextField[] prepareTextFields() {
        TextField name = new TextField();
        GridPane.setConstraints(name, 1, 0);

        TextField day = new TextField();
        GridPane.setConstraints(day, 1, 1);

        TextField month = new TextField();
        GridPane.setConstraints(month, 1, 2);

        TextField year = new TextField();
        GridPane.setConstraints(year, 1, 3);

        TextField hour = new TextField();
        GridPane.setConstraints(hour, 1, 4);

        TextField minute = new TextField();
        GridPane.setConstraints(minute, 1, 5);

        TextField[] textfields = {name, day, month, year, hour, minute};
        return textfields;
    }

    protected Label[] prepareLabels() {
        Label name = new Label("Nazwa:");
        GridPane.setConstraints(name, 0, 0);

        Label day = new Label("Dzień:");
        GridPane.setConstraints(day, 0, 1);

        Label month = new Label("Miesiąc:");
        GridPane.setConstraints(month, 0, 2);

        Label year = new Label("Rok:");
        GridPane.setConstraints(year, 0, 3);

        Label hour = new Label("Godzina:");
        GridPane.setConstraints(hour, 0, 4);

        Label minute = new Label("Minuta:");
        GridPane.setConstraints(minute, 0, 5);

        Label[] labels = {name, day, month, year, hour, minute};
        return labels;
    }

    protected MyDateFormat extractDateFromTextFields(TextField[] textfields) throws IOException {
        return new MyDateFormat(
                Integer.parseInt(textfields[1].getText()),
                Integer.parseInt(textfields[2].getText()),
                Integer.parseInt(textfields[3].getText())
        );
    }

    protected MyTimeFormat extractTimeFromTextFields(TextField[] textfields) throws IOException {
        return new MyTimeFormat(
                Integer.parseInt(textfields[4].getText()),
                Integer.parseInt(textfields[5].getText())
        );
    }
}
