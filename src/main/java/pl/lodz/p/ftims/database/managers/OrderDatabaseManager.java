package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.IOrderDatabase;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.storage.model.Order;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseManager implements IOrderDatabase {

    private Database database = DatabaseSingleton.getInstance();

    @Override
    public int addOrder(Order order) {
        int createdId = 0;
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "insert into orders (date, completed, clientID) values (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            prepStmt.setDate(1, Date.valueOf(order.getDate()));
            prepStmt.setBoolean(2, order.getCompleted());
            prepStmt.setInt(3,Integer.parseInt(order.getClient().getId()));
            prepStmt.execute();
            ResultSet set = prepStmt.getGeneratedKeys();
            for (ProductLine productLine : order.getProducts()) {
                PreparedStatement oplStatement = database.getConn().prepareStatement(
                        "insert into orderProductLine (orderID, productID, quantity) values (?, ?, ?)"
                );
                createdId = set.getInt(1);
                oplStatement.setInt(1, createdId);
                oplStatement.setInt(2, Integer.parseInt(productLine.getProduct().getProductID()));
                oplStatement.setInt(3, productLine.getQuantity());
                oplStatement.execute();
            }
            set.close();
            prepStmt.close();
            database.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error while inserting order: " + order.toString());
            ex.printStackTrace();
            return 0;
        }
        return createdId;
    }

    @Override
    public boolean modifyOrder(Order order) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "UPDATE orders " +
                            "SET date=?, completed=?, clientID=?" +
                            "WHERE orderID=?;");
            prepStmt.setDate(1, Date.valueOf(order.getDate()));
            prepStmt.setBoolean(2, order.getCompleted());
            prepStmt.setInt(3,Integer.parseInt(order.getClient().getId()));
            prepStmt.setInt(4, Integer.parseInt(order.getId()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return getOrder(order.getId()).toString().equals(order.toString());
        } catch (SQLException ex) {
            System.err.println("Error while modifying order: " + order.toString());
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeOrder(String id) {
        try {
            if (getOrder(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("DELETE FROM orders WHERE orderID=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing order with id= " + id + " from database");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Order> getOrders() {
        ClientDatabaseManager clientDatabaseManager = new ClientDatabaseManager();
        try {
            List<Order> orders = new ArrayList<>();
            Statement statement = database.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM orders");

            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                boolean completed = resultSet.getBoolean("completed");
                LocalDate localDate = resultSet.getDate("date").toLocalDate();
                Client client = clientDatabaseManager.getClient(String.valueOf(resultSet.getInt("clientID")));
                List<ProductLine> productLines = getProductsByOrderID(String.valueOf(orderID));
                orders.add(new Order(String.valueOf(orderID), localDate, completed, productLines, client));
            }
            resultSet.close();
            statement.close();
            database.closeConnection();
            return orders;
        } catch (SQLException e) {
            System.err.println("Error while getting orders from database");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Order getOrder(String id) {
        ClientDatabaseManager clientDatabaseManager = new ClientDatabaseManager();

        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM orders WHERE orderID=" + id);
            if (result.next()) {
                int orderID = result.getInt("orderID");
                LocalDate date = result.getDate("date").toLocalDate();
                boolean completed = result.getBoolean("completed");
                Client client = clientDatabaseManager.getClient(String.valueOf(result.getInt("clientID")));
                result.close();
                stat.close();
                database.closeConnection();
                List<ProductLine> productLines = getProductsByOrderID(id);
                return new Order(String.valueOf(orderID), date, completed, productLines, client);
            }
            result.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting order with orderID= " + id + " from database");
            e.printStackTrace();
            return null;
        }
    }

    private List<ProductLine> getProductsByOrderID(String orderID) {
        try {
            List<ProductLine> products = new ArrayList<>();
            Statement statement = database.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM orderProductLine WHERE orderID=" + orderID);
            while (resultSet.next()) {
                int productId = resultSet.getInt("productID");
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
