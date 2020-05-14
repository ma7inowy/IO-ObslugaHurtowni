package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.ITransactionDatabase;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.user.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionDatabaseManager implements ITransactionDatabase {

    private Database database = DatabaseSingleton.getInstance();

    @Override
    public boolean addTransaction(Transaction transaction) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "INSERT INTO transactions (clientID, userID, price, date, state, isDelivery, deliveryID, orderID) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            if(transaction.getClient() != null)
                prepStmt.setInt(1, Integer.parseInt(transaction.getClient().getId()));
            else
                prepStmt.setObject(1, null);

            if(transaction.getEmployee() != null)
                prepStmt.setInt(2, Integer.parseInt(transaction.getEmployee().getId()));
            else
                prepStmt.setObject(2, null);

            prepStmt.setDouble(3, transaction.getPrice());
            prepStmt.setDate(4, new java.sql.Date(transaction.getDate().getTimeInMillis()));
            prepStmt.setBoolean(5, transaction.getState());
            prepStmt.setBoolean(6, transaction.isDelivery());

            if(transaction.getDelivery() != null){
                prepStmt.setInt(7, Integer.parseInt(transaction.getDelivery().getId()));
                prepStmt.setObject(8, null);
            }
            else{
                prepStmt.setInt(8, Integer.parseInt(transaction.getOrder().getId()));
                prepStmt.setObject(7, null);
            }


            prepStmt.execute();
            ResultSet set = prepStmt.getGeneratedKeys();
            for (ProductLine productLine : transaction.getProducts()) {
                PreparedStatement tplStatement = database.getConn().prepareStatement(
                        "insert into transactionProductLine (transactionID, productID, quantity) values (?, ?, ?);"
                );
                tplStatement.setInt(1, set.getInt(1));
                tplStatement.setInt(2, Integer.parseInt(productLine.getProduct().getProductID()));
                tplStatement.setInt(3, productLine.getQuantity());
                tplStatement.execute();
            }
            set.close();
            prepStmt.close();
            database.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error while inserting transaction: " + transaction.toString());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyTransaction(Transaction transaction) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "update transactions " +
                            "set clientID=?, userID=?, price=?, date=?, state=?, isDelivery=?" +
                            "where transactionID=?;");
            prepStmt.setInt(1, Integer.parseInt(transaction.getClient().getId()));
            prepStmt.setInt(2, Integer.parseInt(transaction.getEmployee().getId()));
            prepStmt.setDouble(3, transaction.getPrice());
            prepStmt.setDate(4, new java.sql.Date(transaction.getDate().getTimeInMillis()));
            prepStmt.setBoolean(5, transaction.getState());
            prepStmt.setBoolean(6, transaction.isDelivery());
            prepStmt.setInt(7, Integer.parseInt(transaction.getId()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return getTransaction(transaction.getId()).getId().equals(transaction.getId());
        } catch (SQLException ex) {
            System.err.println("Error while modifying transaction");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeTransaction(String id) {
        try {
            if (getTransaction(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("DELETE FROM transactions WHERE transactionID=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing transaction with transactionID= " + id + " from database");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> getTransactions() {
        DeliveryDatabaseManager deliveryDatabaseManager = new DeliveryDatabaseManager();
        OrderDatabaseManager orderDatabaseManager = new OrderDatabaseManager();
        try {
            List<Transaction> transactions = new ArrayList<>();
            Statement statement = database.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM transactions"
            );
            while (resultSet.next()) {
                int transactionID = resultSet.getInt("transactionID");
                int clientID = resultSet.getInt("clientID");
                int userID = resultSet.getInt("userID");
                double price = resultSet.getDouble("price");
                Date date = resultSet.getDate("date");
                boolean state = resultSet.getBoolean("state");
                boolean isDelivery = resultSet.getBoolean("isDelivery");
                Integer deliveryID = resultSet.getInt("deliveryID");
                Integer orderID = resultSet.getInt("orderID");
                List<ProductLine> productLines = getProductsByTransactionID(String.valueOf(transactionID));
                ClientDatabaseManager cdm = new ClientDatabaseManager();
                Client c = null;
                if(clientID != 0)
                    c = cdm.getClient(String.valueOf(clientID));
                UserDatabaseManager udm = new UserDatabaseManager();
                User u = null;
                if(userID != 0)
                    u = udm.getUser(String.valueOf(userID));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                transactions.add(new Transaction(String.valueOf(transactionID), c, u, productLines, price, calendar, state, isDelivery, orderDatabaseManager.getOrder(orderID.toString()), deliveryDatabaseManager.getDelivery(deliveryID.toString())));
            }
            resultSet.close();
            statement.close();
            database.closeConnection();
            return transactions;
        } catch (SQLException e) {
            System.err.println("Error while getting transactions from database");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Transaction getTransaction(String id) {
        DeliveryDatabaseManager deliveryDatabaseManager = new DeliveryDatabaseManager();
        OrderDatabaseManager orderDatabaseManager = new OrderDatabaseManager();
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet resultSet = stat.executeQuery("SELECT * FROM transactions WHERE transactionID=" + id);
            if (resultSet.next()) {
                int transactionID = resultSet.getInt("transactionID");
                int clientID = resultSet.getInt("clientID");
                int userID = resultSet.getInt("userID");
                double price = resultSet.getDouble("price");
                Date date = resultSet.getDate("date");
                boolean state = resultSet.getBoolean("state");
                boolean isDelivery = resultSet.getBoolean("isDelivery");
                Integer deliveryID = resultSet.getInt("deliveryID");
                Integer orderID = resultSet.getInt("orderID");
                List<ProductLine> productLines = getProductsByTransactionID(String.valueOf(transactionID));
                ClientDatabaseManager cdm = new ClientDatabaseManager();

                Client c = null;
                if(clientID != 0)
                    c = cdm.getClient(String.valueOf(clientID));
                UserDatabaseManager udm = new UserDatabaseManager();
                User u = null;
                if(userID != 0)
                     u = udm.getUser(String.valueOf(userID));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                resultSet.close();
                stat.close();
                database.closeConnection();
                return new Transaction(String.valueOf(transactionID), c, u, productLines, price, calendar, state, isDelivery, orderDatabaseManager.getOrder(orderID.toString()), deliveryDatabaseManager.getDelivery(deliveryID.toString()));
            }
            resultSet.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting transaction with id= " + id + " from database");
            e.printStackTrace();
            return null;
        }
    }

    private List<ProductLine> getProductsByTransactionID(String transactionID) {
        try {
            List<ProductLine> products = new ArrayList<>();
            Statement statement = database.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM transactionProductLine WHERE transactionID=" + transactionID
            );
            while (resultSet.next()) {
                int productId = resultSet.getInt("productId");
                int quantity = resultSet.getInt("quantity");
                ProductDatabaseManager productDatabaseManager = new ProductDatabaseManager();
                Product product = productDatabaseManager.getProduct(String.valueOf(productId));
                products.add(new ProductLine("",product, quantity));
            }
            resultSet.close();
            statement.close();
            database.closeConnection();
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
