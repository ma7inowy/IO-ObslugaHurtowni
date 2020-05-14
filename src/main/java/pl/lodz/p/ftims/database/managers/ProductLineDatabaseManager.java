package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.IProductLineDatabase;
import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductLineDatabaseManager implements IProductLineDatabase {
    private Database database = DatabaseSingleton.getInstance();

    public ProductLineDatabaseManager() {

    }


    @Override
    public boolean addProductLine(ProductLine productLine) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "insert into productLine (quantity, productId) values (?, ?);");
            prepStmt.setInt(1, productLine.getQuantity());
            prepStmt.setInt(2, Integer.parseInt(productLine.getProduct().getProductID()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error while adding productLine to database");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyProductLine(ProductLine productLine) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "UPDATE productLine " +
                            "SET productId=?, quantity=? " +
                            "WHERE productLineId=?;");
            prepStmt.setInt(1, Integer.parseInt(productLine.getProduct().getProductID()));
            prepStmt.setInt(2, productLine.getQuantity());
            prepStmt.setInt(3, Integer.parseInt(productLine.getProductLineId()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error while modifying productLine: " + productLine.toString());
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeProductLine(String id) {
        try {
            if (getProductLine(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("delete FROM productLine WHERE productId=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing productLine with id=" + id + " from database");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ProductLine> getProductLines() {
        List<ProductLine> productLineAll = new ArrayList<>(1);
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM productLine");
            while (result.next()) {
                int productLineId = result.getInt("productLineId");
                int productId = result.getInt("productId");
                int quantity = result.getInt("quantity");
                ProductDatabaseManager productDatabaseManager = new ProductDatabaseManager();
                productLineAll.add(new ProductLine(String.valueOf(productLineId),productDatabaseManager.getProduct(String.valueOf(productId)), quantity));
            }
            result.close();
            stat.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while getting productLines");
            e.printStackTrace();
            return null;
        }
        return productLineAll;
    }

    @Override
    public ProductLine getProductLine(String id) {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM productLine WHERE productLineId=" + id);
            if (result.next()) {
                int productLineId = result.getInt("productLineId");
                int productId = result.getInt("productId");
                int quantity = result.getInt("quantity");
                ProductDatabaseManager productDatabaseManager = new ProductDatabaseManager();
                result.close();
                stat.close();
                database.closeConnection();
                return new ProductLine(String.valueOf(productLineId), productDatabaseManager.getProduct(String.valueOf(productId)), quantity);
            }
            result.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting productLine with id=" + id);
            e.printStackTrace();
            return null;
        }
    }
}
