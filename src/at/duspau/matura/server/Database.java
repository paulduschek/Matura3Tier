package at.duspau.matura.server;

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
    private PreparedStatement pstmtSelectNameDate;
    private PreparedStatement pstmSelectAvSeats;
    private PreparedStatement pstmtInsert;
    private PreparedStatement pstmtUpdate;
    private PreparedStatement pstmtUpdateSeats;

    private Database() {
        try (FileInputStream in = new FileInputStream("dbconnect.properties");) {
            Properties prop = new Properties();
            prop.load(in);
            driver = prop.getProperty("driver");
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            if (driver == null || url == null || username == null || password == null) {
                throw new Exception("Error! missing properties in dbconnect");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

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
            instance.createPstmtSelectNameDateId();
            instance.createPstmtInsert();
            instance.createPstmtUpdate();
            instance.createPstmtSelectAvSeats();
            instance.createPstmtUpdateAvSeats();
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

    public PreparedStatement getPstmtSelectNameDate() {
        return pstmtSelectNameDate;
    }

    public PreparedStatement getPstmSelectAvSeats() {
        return pstmSelectAvSeats;
    }

    public PreparedStatement getPstmtInsert() {
        return pstmtInsert;
    }

    public PreparedStatement getPstmtUpdate() {
        return pstmtUpdate;
    }

    public PreparedStatement getPstmtUpdateSeats() {
        return pstmtUpdateSeats;
    }

    public void setPstmtUpdateSeats(PreparedStatement pstmtUpdateSeats) {
        this.pstmtUpdateSeats = pstmtUpdateSeats;
    }

    private void createPstmtSelectAvSeats() throws SQLException {
        String sql = " select numOfSeats from Event where id = ?";

        pstmSelectAvSeats = connection.prepareStatement(sql);
    }

    private void createPstmtUpdateAvSeats() throws SQLException {
        String sql = "update Event set numOfSeats = ? where id = ?";

        pstmtUpdateSeats = connection.prepareStatement(sql);
    }

    private void createPstmtSelectNameDateId() throws SQLException {
        String sql = " select id, name, date from Event";
        pstmtSelectNameDate = connection.prepareStatement(sql);
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