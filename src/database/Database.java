package database;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class Database {

    private static Database instance;
    private Connection connection;
    private Statement statement;
    private String driver;
    private String url;
    private String username;
    private String password;
    private PreparedStatement pstmtSelect;
    private PreparedStatement pstmtSelectAll;
    private PreparedStatement pstmtInsert;
    private PreparedStatement pstmtUpdate;

    private Database() {
        // DB-Properties laden
        try (FileInputStream in = new FileInputStream("dbconnect.properties");) {
            Properties prop = new Properties();
            prop.load(in);
            driver = prop.getProperty("driver");
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            // Alles da?
            if (driver == null || url == null || username == null || password == null) {
                throw new Exception("Fehler! Property File muss driver, url, username, password enthalten!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Verbindung erstellen
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static Database getInstance() {
        return instance;
    }

    public static void open(boolean createPrepStatements) throws SQLException {
        instance = new Database();
        if(createPrepStatements){
            instance.createPstmtSelect();
            instance.createPstmtInsert();
            instance.createPstmtUpdate();
            instance.createPstmtSelectAll();
        }
    }

    public static void close() {
        try {
            getInstance().connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public PreparedStatement getPstmtSelect() {
        return pstmtSelect;
    }

    public PreparedStatement getPstmtSelectAll() {
        return pstmtSelectAll;
    }

    public PreparedStatement getPstmtInsert() {
        return pstmtInsert;
    }

    public PreparedStatement getPstmtUpdate() {
        return pstmtUpdate;
    }


    private void createPstmtSelectAll() throws SQLException {
        String sql = " select name, date, numOfSeats from Event ";

        pstmtSelectAll = connection.prepareStatement(sql);
    }

    private void createPstmtSelect() throws SQLException {
        String sql = " select name, date, numOfSeats from Event where id = ?";
        pstmtSelect = connection.prepareStatement(sql);
    }

    private void createPstmtInsert() throws SQLException {
        String sql = "  insert into Event (name, date, numOfSeats) values (?, ?, ?)";

        pstmtInsert = connection.prepareStatement(sql);
    }

    private void createPstmtUpdate() throws SQLException {
        String sql = "update Event set name = ?, date = ?, numOfSeats = ? ";

        pstmtUpdate = connection.prepareStatement(sql);
    }
}