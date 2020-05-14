package pl.lodz.p.ftims.model.storage.service;

import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.storage.repository.ProductLineRepository;

import java.util.List;

public class ProductLineService implements IProductLineService {
    private ProductLineRepository productLineRepository;

    public ProductLineService() {
        this.productLineRepository = new ProductLineRepository();
    }

    public void addProductLine(int quantity, Product product) {
        productLineRepository.addProductLine(new ProductLine("", product, quantity));
    }

    public void removeProductLine(String id) {
        productLineRepository.removeProductLine(id);
    }

    public ProductLine getProductLine(String id) {
        return productLineRepository.getProductLine(id);
    }

    public List<ProductLine> getAllProductLines() {
        return this.productLineRepository.getProductLines();
    }

    public void updateProductLine(ProductLine productLine) {
        this.productLineRepository.updateProductLine(productLine);
    }

}
