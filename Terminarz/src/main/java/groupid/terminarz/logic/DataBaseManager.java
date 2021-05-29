package groupid.terminarz.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    private String url;
    private String dbUsername;
    private String dbPassword;
    private Statement statement;

    public DataBaseManager() {
        url = "jdbc:mysql://localhost:55555/Terminarz";
        dbUsername = "testuser0";
        dbPassword = "123q";

        try {
            Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
            statement = conn.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String username, String password) {
        String query = "INSERT INTO USERS_TBL (USERNAME, PASS) "
                + "VALUES ('" + username + "', '" + password + "')";

        try {
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadUsernames() {
        List<String> usernames = new ArrayList<>();

        try {
            ResultSet results = statement.executeQuery("SELECT USERNAME FROM USERS_TBL");

            while (results.next()) {
                usernames.add(results.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
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

    public List<MyEvent> loadEvents(String currentUser) {
        List<MyEvent> events = new ArrayList<>();
        String query = String.format("SELECT * FROM EVENTS_TBL WHERE USERNAME = '%s'", currentUser);

        try {
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int id = results.getInt(1);
                MyDateFormat date = extractDate(results.getString(2));
                MyTimeFormat time = extractTime(results.getString(2));
                String name = results.getString(3);
                events.add(new MyEvent(id, date, time, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return events;
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
        return "'" + date.toString() + " " + time.toString() + "'";
    }

    private void executeTheQuery(String query) {
        try {
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
