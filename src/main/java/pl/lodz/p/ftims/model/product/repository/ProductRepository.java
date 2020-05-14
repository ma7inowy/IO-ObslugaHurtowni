package pl.lodz.p.ftims.model.product.repository;

import pl.lodz.p.ftims.database.communicationinterface.IProductDatabase;
import pl.lodz.p.ftims.database.managers.ProductDatabaseManager;
import pl.lodz.p.ftims.model.product.model.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductRepository implements IProductRepository{
    private IProductDatabase productDatabase;

    public ProductRepository(){
        this.productDatabase = new ProductDatabaseManager();
    }

    @Override
    public String addProduct(Product product){
        return this.productDatabase.addProduct(product);
    }

    @Override
    public boolean updateProduct(Product product) {
        return this.productDatabase.modifyProduct(product);
    }

    @Override
    public boolean removeProduct(String id) {
        return this.productDatabase.removeProduct(id);
    }

    @Override
    public Product getProduct(String id) {
        return this.productDatabase.getProduct(id);
    }

    @Override
    public List<Product> getAll() {
        return this.productDatabase.getProducts();
    }
}
