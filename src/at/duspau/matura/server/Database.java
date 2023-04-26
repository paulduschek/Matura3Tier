package at.duspau.matura.server;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class Database {
    // singleton definition of Database
    private static Database instance;
    // for the connection with the database
    private Connection connection;
    // for executing static sql statements
    private Statement statement;

    /* parameters for the properties file */
    private String driver;
    private String url;
    private String username;
    private String password;

    /* definitions of prepared statements */
    private PreparedStatement pstmtSelectNameDate;
    private PreparedStatement pstmSelectAvSeats;
    private PreparedStatement pstmtInsert;
    private PreparedStatement pstmtUpdate;
    private PreparedStatement pstmtUpdateSeats;

    // constructor has to be private
    private Database() {
        // read properties file
        try (FileInputStream in = new FileInputStream("dbconnect.properties")) {
            // load data from properties
            Properties prop = new Properties();
            prop.load(in);
            driver = prop.getProperty("driver");
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            // check if properties file is OK
            if (driver == null || url == null || username == null || password == null) {
                throw new Exception("Error! missing properties in dbconnect");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // initialize connection with database
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * @return singleton instance of the database
     */
    public static Database getInstance() {
        return instance;
    }

    /**
     * initializes the database and possibly the prepared statements
     * @param createPrepStatements check if prepared statements should be created
     * @throws SQLException
     */
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
}