package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.IDeliveryDatabase;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDatabaseManager implements IDeliveryDatabase {
    private Database database = DatabaseSingleton.getInstance();

    @Override
    public int addDelivery(Delivery delivery) {
        int createdId = 0;
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "insert into deliveries (date, completed) values (?, ?);", Statement.RETURN_GENERATED_KEYS);
            prepStmt.setDate(1, java.sql.Date.valueOf(delivery.getDate()));
            prepStmt.setBoolean(2, delivery.isCompleted());
            prepStmt.execute();
            ResultSet set = prepStmt.getGeneratedKeys();
            for (ProductLine productLine : delivery.getProducts()) {
                PreparedStatement deliveryProductLine = database.getConn().prepareStatement(
                        "insert into deliveryProductLine (deliveryId, productId, quantity) values (?,?, ?)"
                );
                createdId = set.getInt(1);
                deliveryProductLine.setInt(1, createdId);
                deliveryProductLine.setInt(2, Integer.parseInt(productLine.getProduct().getProductID()));
                deliveryProductLine.setInt(3, productLine.getQuantity());

                deliveryProductLine.execute();
            }
            set.close();
            prepStmt.close();
            database.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error while inserting delivery: " + delivery.toString());
            ex.printStackTrace();
            return 0;
        }
        return createdId;
    }

    @Override
    public boolean modifyDelivery(Delivery delivery) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "update deliveries " +
                            "set date=?, completed=?" +
                            "where id=?;");
            prepStmt.setDate(1, java.sql.Date.valueOf(delivery.getDate()));
            prepStmt.setBoolean(2, delivery.isCompleted());
            prepStmt.setInt(3, Integer.parseInt(delivery.getId()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return getDelivery(delivery.getId()).getId().equals(delivery.getId());
        } catch (SQLException ex) {
            System.err.println("Error while modifying delivery");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeDelivery(String id) {
        try {
            if (getDelivery(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("DELETE FROM deliveries WHERE id=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing delivery with id= " + id + " from database");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Delivery> getDeliveries() {
        try {
            List<Delivery> deliveries = new ArrayList<>();
            Statement statement = database.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM deliveries"
            );

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                boolean completed = resultSet.getBoolean("completed");
                LocalDate localDate = resultSet.getDate("date").toLocalDate();
                List<ProductLine> productLines = getItemsOrder(String.valueOf(id));
                deliveries.add(new Delivery(String.valueOf(id), localDate, productLines, completed));
            }
            resultSet.close();
            statement.close();
            database.closeConnection();
            return deliveries;
        } catch (SQLException e) {
            System.err.println("Error while getting deliveries from database");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Delivery getDelivery(String id) {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM deliveries WHERE id=" + id);
            if (result.next()) {
                int deliveryID = result.getInt("id");
                LocalDate date = result.getDate("date").toLocalDate();
                boolean completed = result.getBoolean("completed");
                result.close();
                stat.close();
                database.closeConnection();
                List<ProductLine> productLines = getItemsOrder(id);
                return new Delivery(String.valueOf(deliveryID), date, productLines, completed);
            }
            result.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting delivery with id= " + id + " from database");
            e.printStackTrace();
            return null;
        }
    }

    private List<ProductLine> getItemsOrder(String deliveryId) {
        try {
            List<ProductLine> products = new ArrayList<>();
            Statement statement = database.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM deliveryProductLine WHERE deliveryId=" + deliveryId
            );
            while (resultSet.next()) {
                int productId = resultSet.getInt("productId");
                ProductDatabaseManager productDatabaseManager = new ProductDatabaseManager();
                Product product = productDatabaseManager.getProduct(String.valueOf(productId));
                products.add(new ProductLine("", product, resultSet.getInt("quantity")));
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
