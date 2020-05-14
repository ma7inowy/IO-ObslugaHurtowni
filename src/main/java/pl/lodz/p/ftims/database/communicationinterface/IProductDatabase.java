package pl.lodz.p.ftims.database.communicationinterface;

import pl.lodz.p.ftims.model.product.model.Product;

import java.util.List;

public interface IProductDatabase {
    String addProduct(Product product);

    boolean modifyProduct(Product product);

    boolean removeProduct(String id);

    Product getProduct(String id);

    List<Product> getProducts();

}
