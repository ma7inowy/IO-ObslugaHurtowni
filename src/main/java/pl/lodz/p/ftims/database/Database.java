package pl.lodz.p.ftims.database;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DRIVER = "org.sqlite.JDBC";
    private String DB_URL;
    private Connection conn;
    private Statement stat;

    //fileName - np baza.db
    public Database(String fileName, boolean deleteOldDatabase) {
        if (deleteOldDatabase) {
            try {
                new File(fileName).delete();
            } catch (Exception ignored) {
//                e.printStackTrace();
            }
        }
        this.DB_URL = "jdbc:sqlite:src\\main\\java\\pl\\lodz\\p\\ftims\\database\\" + fileName;
        connectToDatabase();
        createTables();
    }

    public Database() {
        this.DB_URL = "jdbc:sqlite:src\\main\\java\\pl\\lodz\\p\\ftims\\database\\databaseHurtownia.db";
        connectToDatabase();
        createTables();
    }

    public Connection getConn() throws SQLException {
        this.conn = DriverManager.getConnection(DB_URL);
        return conn;
    }

    private void connectToDatabase() {
        try {
            Class.forName(Database.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("There is no JDBC driver");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Some error with opening connection occurred");
            e.printStackTrace();
        }
    }

    private void createTables() {
        String createClients = "CREATE TABLE IF NOT EXISTS clients " +
                "(clientID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255), " +
                "state VARCHAR(255), " +
                "isBanned INT CHECK(isBanned = 1 OR isBanned = 0))";

        String createComments = "CREATE TABLE IF NOT EXISTS comments" +
                "(commentID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "comment VARCHAR(255)," +
                "clientID INTEGER," +
                "userID INTEGER," +
                "FOREIGN KEY (clientID) REFERENCES clients(clientID) ON DELETE CASCADE," +
                "FOREIGN KEY (userID) REFERENCES employees(employeeID) ON DELETE CASCADE)";

        String createEmployees = "CREATE TABLE IF NOT EXISTS employees " +
                "(employeeID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstName VARCHAR(255), " +
                "secondName VARCHAR(255), " +
                "address VARCHAR(255), " +
                "typeId INTEGER, " +
                "salary DOUBLE," +
                "login varchar(255) UNIQUE, " +
                "password varchar(255) NOT NULL)";

        String createProducts = "CREATE TABLE IF NOT EXISTS products " +
                "(productID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255) UNIQUE, " +
                "purchasePrice FLOAT, " +
                "sellPrice FLOAT, " +
                "discount FLOAT)";

        String createDeliveries = "CREATE TABLE IF NOT EXISTS deliveries " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "completed BOOLEAN)";

        String createProductLine = "CREATE TABLE IF NOT EXISTS productLine " +
                "(productLineId INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "quantity INTEGER, " +
                "productId INTEGER, " +
                "FOREIGN KEY (productId) REFERENCES products(productID) ON DELETE CASCADE)";

        String createDeliveryProductLine = "CREATE TABLE IF NOT EXISTS deliveryProductLine" +
                "(deliveryProductLine INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "deliveryId INTEGER, " +
                "productId INTEGER, " +
                "quantity INTEGER NOT NULL, " +
                "FOREIGN KEY (deliveryId) REFERENCES  deliveries(id) ON DELETE CASCADE," +
                "FOREIGN KEY (productId) REFERENCES products(productID) ON DELETE CASCADE)";

        String createOrders = "CREATE TABLE IF NOT EXISTS orders " +
                "(orderID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "clientID INTEGER," +
                "completed BOOLEAN," +
                "FOREIGN KEY (clientID) REFERENCES clients(clientID))";

        String createOrderProductLine = "CREATE TABLE IF NOT EXISTS orderProductLine" +
                "(orderProductLineID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderID INTEGER, " +
                "productID INTEGER, " +
                "quantity INTEGER NOT NULL, " +
                "FOREIGN KEY (orderID) REFERENCES  orders(orderID) ON DELETE CASCADE," +
                "FOREIGN KEY (productID) REFERENCES products(productID) ON DELETE CASCADE)";

        String createTransactions = "CREATE TABLE IF NOT EXISTS transactions " +
                "(transactionID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "clientID INTEGER," +
                "userID INTEGER," +
                "price FLOAT," +
                "date TEXT," +
                "state BOOLEAN," +
                "isDelivery BOOLEAN," +
                "deliveryID INTEGER," +
                "orderID INTEGER," +
                "FOREIGN KEY (clientID) REFERENCES clients(clientID) ON DELETE CASCADE," +
                "FOREIGN KEY (userID) REFERENCES employees(employeeID) ON DELETE CASCADE, " +
                "FOREIGN KEY (deliveryID) REFERENCES deliveries(id) ON DELETE CASCADE," +
                "FOREIGN KEY (orderID) REFERENCES orders(orderID) ON DELETE CASCADE )";

        String createTransactionProductLine = "CREATE TABLE IF NOT EXISTS transactionProductLine " +
                "(transactionProductLineID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "transactionID INTEGER," +
                "productID INTEGER," +
                "quantity INTEGER NOT NULL," +
                "FOREIGN KEY (transactionID) REFERENCES transactions(transactionID) ON DELETE CASCADE," +
                "FOREIGN KEY (productID) REFERENCES products(productID) ON DELETE CASCADE)";

        String createCashflow = "CREATE TABLE IF NOT EXISTS cashflows " +
                "(cashflowID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT," +
                "amount DOUBLE," +
                "name VARCHAR(255))";

        String createAccount = "CREATE TABLE IF NOT EXISTS account" +
                "(money DOUBLE);";

        try {
            stat.execute("PRAGMA foreign_keys=on;");
            stat.execute(createClients);
            stat.execute(createComments);
            stat.execute(createEmployees);
            stat.execute(createProducts);
            stat.execute(createDeliveries);
            stat.execute(createProductLine);
            stat.execute(createDeliveryProductLine);
            stat.execute(createOrders);
            stat.execute(createOrderProductLine);
            stat.execute(createTransactions);
            stat.execute(createTransactionProductLine);
            stat.execute(createCashflow);
            stat.execute(createAccount);
//            stat.execute("INSERT INTO account VALUES (1000)");
            stat.close();


        } catch (SQLException e) {
            System.err.println("Error while creating tables");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error while closing connection occurred");
            e.printStackTrace();
        }
    }
}