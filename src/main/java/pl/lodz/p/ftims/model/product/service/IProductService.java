package pl.lodz.p.ftims.model.product.service;

import pl.lodz.p.ftims.model.product.model.Product;

import java.util.List;

public interface IProductService {
    String addProduct(String name,
                      Double purchasePrice,
                      Double sellPrice,
                      Double discount);

    void updateProduct(String id,
                       String name,
                       Double purchasePrice,
                       Double sellPrice,
                       Double discount);

    void removeProduct(String id);

    Product getProduct(String id);

    List<Product> getAllProducts();

    Product getProductByName(String name);

    List<String> getAllProductsName();

    void createProductRaport(List<Product> productList);
}
