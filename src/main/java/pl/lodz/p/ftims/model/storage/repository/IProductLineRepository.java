package pl.lodz.p.ftims.model.storage.repository;

import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.util.List;

public interface IProductLineRepository {
    void removeProductLine(String id);

    void addProductLine(ProductLine productLine);

    ProductLine getProductLine(String id);

    List<ProductLine> getProductLines();

    void updateProductLine(ProductLine productLine);
}
