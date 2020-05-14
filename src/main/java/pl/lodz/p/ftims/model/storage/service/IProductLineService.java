package pl.lodz.p.ftims.model.storage.service;

import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.util.List;

public interface IProductLineService {
    void addProductLine(int quantity, Product product);

    void removeProductLine(String id);

    ProductLine getProductLine(String Id);

    List<ProductLine> getAllProductLines();

    void updateProductLine(ProductLine productLine);
}
