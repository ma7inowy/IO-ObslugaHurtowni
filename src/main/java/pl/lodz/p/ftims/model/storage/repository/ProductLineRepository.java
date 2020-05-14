package pl.lodz.p.ftims.model.storage.repository;

import pl.lodz.p.ftims.database.communicationinterface.IProductLineDatabase;
import pl.lodz.p.ftims.database.managers.ProductLineDatabaseManager;
import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.util.List;

public class ProductLineRepository implements IProductLineRepository {
    private IProductLineDatabase productLineDatabase;

    public ProductLineRepository() {
        productLineDatabase = new ProductLineDatabaseManager();
    }

    public void removeProductLine(String id) {
        this.productLineDatabase.removeProductLine(id);
    }

    public void addProductLine(ProductLine productLine) {
        this.productLineDatabase.addProductLine(productLine);
    }

    public ProductLine getProductLine(String id) {
        return this.productLineDatabase.getProductLine(id);
    }

    public List<ProductLine> getProductLines() {
        return this.productLineDatabase.getProductLines();
    }

    public void updateProductLine(ProductLine productLine) {
        this.productLineDatabase.modifyProductLine(productLine);
    }


}

