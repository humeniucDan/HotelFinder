package HotelFinder.hotelFinder.DBConnection;

import java.sql.*;

public class ConnectionFactory {
    private static final MSDataBaseCredentials dbCred = MSDataBaseCredentials.getConfiguration();
    private static final ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            Class.forName(dbCred.getDriver());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {
        Connection connection = null;
        try {
            System.out.println("URL" + dbCred.getConnUrl());
            connection = DriverManager.getConnection(dbCred.getConnUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
