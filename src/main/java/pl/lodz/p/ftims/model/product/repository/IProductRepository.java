package pl.lodz.p.ftims.model.product.repository;

import pl.lodz.p.ftims.model.product.model.Product;

import java.util.List;

public interface IProductRepository {
    String addProduct(Product product);

    boolean updateProduct(Product product);

    boolean removeProduct(String id);

    Product getProduct(String id);

    List<Product> getAll();
}
