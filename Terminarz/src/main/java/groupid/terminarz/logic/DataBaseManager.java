package groupid.terminarz.logic;

import groupid.terminarz.Utilities;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    private final Statement statement;

    public DataBaseManager() throws SQLException {
        final String url = "jdbc:mysql://192.168.0.120:55555/timetable";
        final String dbUsername = "timetable_app";
        final String dbPassword = "123q";

        Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
        statement = conn.createStatement();
    }

    public void addUser(String username, String password) {
        String query = "INSERT INTO USERS_TBL (USERNAME, PASS) "
                + "VALUES ('" + username + "', '" + password + "')";

        executeTheQuery(query);
    }

    public void addEvent(String name, MyDateFormat date, MyTimeFormat time, String username) {
        String query = "INSERT INTO EVENTS_TBL (DEADLINE, NAME_OF_EVENT, USERNAME) "
                + "VALUES (" + makeSqlDatetimeFormat(date, time) + ", '" + name + "', '" + username + "')";

        executeTheQuery(query);
    }

    public void updateEvent(MyEvent editedEvent) {
        String query = String.format(
                "UPDATE EVENTS_TBL SET DEADLINE=%s, NAME_OF_EVENT='%s' WHERE ID=%d",
                makeSqlDatetimeFormat(editedEvent.getDeadline(), editedEvent.getTime()),
                editedEvent.getName(),
                editedEvent.getId()
        );

        executeTheQuery(query);
    }

    public void deleteEvent(int id) {
        executeTheQuery(String.format("DELETE FROM EVENTS_TBL WHERE ID=%d", id));
    }

    public List<MyEvent> loadEvents(String currentUser) throws IOException, SQLException {
        String query = String.format("SELECT * FROM EVENTS_TBL WHERE USERNAME = '%s'", currentUser);
        return takeResults(query);
    }

    public List<MyEvent> loadEvents(String currentUser, MyDateFormat certainDate) throws IOException, SQLException {
        String query = String.format(
                "SELECT * FROM EVENTS_TBL WHERE (USERNAME = '%s') AND (DEADLINE REGEXP '^%s')",
                currentUser,
                certainDate
        );

        return takeResults(query);
    }

    public boolean checkPassword(String username, String password) {
        String query = String.format("SELECT PASS FROM USERS_TBL WHERE USERNAME='%s'", username);

        try {
            ResultSet results = statement.executeQuery(query);
            String correctPassword;

            if (results.next()) {
                correctPassword = results.getString(1);

                if (password.equals(correctPassword)) {
                    return true;
                }
            }

        } catch (SQLException e) {
            Utilities.popUpErrorBox("Wystąpił błąd.");
        }

        return false;
    }

    private List<MyEvent> takeResults(String query) throws IOException, SQLException {
        List<MyEvent> list = new ArrayList<>();
        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            int id = results.getInt(1);
            MyDateFormat date = extractDate(results.getString(2));
            MyTimeFormat time = extractTime(results.getString(2));
            String name = results.getString(3);
            list.add(new MyEvent(id, date, time, name));
        }

        return list;
    }

    private MyDateFormat extractDate(String sqlDatetime) throws IOException {
        int year = Integer.parseInt(sqlDatetime.substring(0, 4));
        int month = Integer.parseInt(sqlDatetime.substring(5, 7));
        int day = Integer.parseInt(sqlDatetime.substring(8, 10));

        return new MyDateFormat(day, month, year);
    }

    private MyTimeFormat extractTime(String sqlDatetime) {
        int hour = Integer.parseInt(sqlDatetime.substring(11, 13));
        int minute = Integer.parseInt(sqlDatetime.substring(14, 16));

        return new MyTimeFormat(hour, minute);
    }

    private String makeSqlDatetimeFormat(MyDateFormat date, MyTimeFormat time) {
        return "'" + date + " " + time + "'";
    }

    private void executeTheQuery(String query) {
        try {
            statement.executeUpdate(query);

        } catch (SQLException e) {
            Utilities.popUpErrorBox("Błąd. Nie udało się wykonać tej operacji.");
        }
    }
}
