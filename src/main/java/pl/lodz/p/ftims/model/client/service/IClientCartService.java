package pl.lodz.p.ftims.model.client.service;

import pl.lodz.p.ftims.model.product.model.Product;

import java.util.List;

public interface IClientCartService {

    Product getProduct(String id);

    List<Product> getAllProducts();
}
