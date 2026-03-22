package bank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {

    public static Connection getConnection() throws SQLException {
        // Reads from environment variables set on Render
        String url      = System.getenv("DB_URL");
        String user     = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Fallback to local for development
        if (url == null)      url      = "jdbc:mysql://caboose.proxy.rlwy.net:58083/railway";
        if (user == null)     user     = "root";
        if (password == null) password = "luMDaXTGNQbVSCGNojFQyUgVBGeUbutO";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(url, user, password);
    }
}