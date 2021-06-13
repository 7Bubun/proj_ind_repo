package groupid.terminarz.view;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HelpWindow extends SpecialWindow {

    public HelpWindow(SceneCreator creator) {
        super(creator);
    }

    @Override
    public void appear() {
        String[] labelTexts = {
            "Dane wydarzenia:",
            "Nazwa - maks 20 znaków",
            "Dzień - liczba",
            "Miesiąc - liczba",
            "Rok - liczba",
            "Godzina - liczba",
            "Minuta - liczba",
            "",
            "Dane użytkownika:",
            "Login - maksymalnie 20 znaków",
            "Hasło - maksymalnie 30 znaków"
        };

        GridPane layout = prepareGridPane();

        for (int i = 0; i < labelTexts.length; i++) {
            Label tmpLab = new Label(labelTexts[i]);
            layout.add(tmpLab, 0, i);
        }

        Stage window = new Stage();
        initWindow(window, layout, "Pomoc", 300);
    }
}
