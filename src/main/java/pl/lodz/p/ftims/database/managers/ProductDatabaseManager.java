package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.IProductDatabase;
import pl.lodz.p.ftims.model.product.model.Product;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseManager implements IProductDatabase {

    private Database database = DatabaseSingleton.getInstance();

    @Override
    public String addProduct(Product product) {
        String id = null;
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "INSERT INTO products (name, purchasePrice, sellPrice, discount) VALUES (?, ?, ?, ?);");
            prepStmt.setString(1, product.getName());
            prepStmt.setDouble(2, product.getPurchasePrice());
            prepStmt.setDouble(3, product.getSellPrice());
            prepStmt.setDouble(4, product.getDiscount());
            prepStmt.execute();
            ResultSet set = prepStmt.getGeneratedKeys();
            id = String.valueOf(set.getInt(1));
            prepStmt.close();;
            database.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error while inserting product: " + product.toString());
            ex.printStackTrace();
            return null;
        }
        return id;
    }

    @Override
    public boolean modifyProduct(Product product) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "UPDATE products " +
                            "SET name=?, purchasePrice=?, sellPrice=?, discount=? " +
                            "WHERE productID=?;");
            prepStmt.setString(1, product.getName());
            prepStmt.setDouble(2, product.getPurchasePrice());
            prepStmt.setDouble(3, product.getSellPrice());
            prepStmt.setDouble(4, product.getDiscount());
            prepStmt.setInt(5, Integer.parseInt(product.getProductID()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return getProduct(product.getProductID()).toString().equals(product.toString());
        } catch (SQLException ex) {
            System.err.println("Error while modifying product: " + product.toString());
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeProduct(String id) {
        try {
            if (getProduct(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("delete FROM products WHERE productID=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing product with id=" + id + " from database");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getProduct(String id) {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM products WHERE productID=" + id);
            if (result.next()) {
                int productID = result.getInt("productID");
                String name = result.getString("name");
                double purchasePrice = result.getDouble("purchasePrice");
                double sellPrice = result.getDouble("sellPrice");
                double discount = result.getDouble("discount");
                result.close();
                stat.close();
                database.closeConnection();
                return new Product(String.valueOf(productID), name, purchasePrice, sellPrice, discount);
            }
            result.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting product with id=" + id + " from database");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>(1);
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM products");
            while (result.next()) {
                int id = result.getInt("productID");
                String name = result.getString("name");
                double purchasePrice = result.getDouble("purchasePrice");
                double sellPrice = result.getDouble("sellPrice");
                double discount = result.getDouble("discount");
                products.add(new Product(String.valueOf(id), name, purchasePrice, sellPrice, discount));
            }
            result.close();
            stat.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while getting products from database");
            e.printStackTrace();
            return null;
        }
        return products;
    }
}
