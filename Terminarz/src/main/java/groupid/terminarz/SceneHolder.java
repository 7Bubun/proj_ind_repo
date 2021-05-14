package groupid.terminarz;

import groupid.terminarz.view.EventsTableView;
import groupid.terminarz.view.SceneMaker;
import javafx.scene.Scene;

public class SceneHolder {

    private Scene viewOfTable;
    private Scene viewOfMonth;
    private Scene viewOfYear;

    public SceneHolder() {
        SceneMaker creator = new EventsTableView();
        viewOfTable = creator.makeScene();
    }

    public Scene getViewOfTable() {
        return viewOfTable;
    }
}
