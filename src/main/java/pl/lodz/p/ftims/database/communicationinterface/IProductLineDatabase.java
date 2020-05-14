package pl.lodz.p.ftims.database.communicationinterface;

import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.util.List;

public interface IProductLineDatabase {
    boolean addProductLine(ProductLine productLine);

    boolean modifyProductLine(ProductLine productLine);

    boolean removeProductLine(String id);

    List<ProductLine> getProductLines();

    ProductLine getProductLine(String id);
}
