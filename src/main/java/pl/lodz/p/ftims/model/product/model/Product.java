package pl.lodz.p.ftims.model.product.model;

public class Product {
    private String productID;
    private String name;
    private Double purchasePrice;
    private Double sellPrice;
    private Double discount; // wartosc od 0.5 do 1

    public Product(String productID, String name, Double purchasePrice, Double sellPrice, Double discount) {
        this.productID = productID;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
        this.discount = discount;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", name='" + name + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", sellPrice=" + sellPrice +
                ", discount=" + discount +
                '}';
    }
}
