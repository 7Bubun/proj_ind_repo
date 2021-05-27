package groupid.terminarz.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

    private String url;
    private String dbusername;
    private String dbPassword;

    public DataBaseManager() {
        url = "jdbc:mysql://localhost:55555/Terminarz";
        dbusername = "testuser0";
        dbPassword = "123q";

        try {
            Connection c = DriverManager.getConnection(url, dbusername, dbPassword);
            Statement s = c.createStatement();

            //TMP
            ResultSet rs = s.executeQuery("SELECT * FROM Event");

            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
