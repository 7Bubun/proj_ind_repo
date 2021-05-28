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
    private String dbusername;
    private String dbPassword;
    private Connection conn;

    public DataBaseManager() {
        url = "jdbc:mysql://localhost:55555/Terminarz";
        dbusername = "testuser0";
        dbPassword = "123q";

        try {
            conn = DriverManager.getConnection(url, dbusername, dbPassword);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEvent(String name, MyDateFormat date, MyTimeFormat time) {
        String sqlFormattedDatetime = "'" + date.toString() + " " + time.toString() + "'";

        String query = "INSERT INTO EVENTS_TBL (DEADLINE, NAME_OF_EVENT) "
                + "VALUES (" + sqlFormattedDatetime + ", '" + name + "')";

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MyEvent> loadEvents() {
        List<MyEvent> events = new ArrayList<>();

        try {
            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM EVENTS_TBL");

            while (results.next()) {
                int id = Integer.parseInt(results.getString(1));
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
}
