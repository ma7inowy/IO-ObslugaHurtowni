package pl.lodz.p.ftims.model.storage.model;

import pl.lodz.p.ftims.model.product.model.Product;

public class ProductLine {

    private Product product;
    private int quantity;
    private String productLineId;

    public ProductLine(String id, Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.productLineId = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductLineId() {
        return productLineId;
    }

    public void setProductLineId(String productId) {
        this.productLineId = productId;
    }

    @Override
    public String toString() {
        return "ProductLine{" +
                "quantity=" + quantity +
                ", productId=" + productLineId +
                '}';
    }
}

