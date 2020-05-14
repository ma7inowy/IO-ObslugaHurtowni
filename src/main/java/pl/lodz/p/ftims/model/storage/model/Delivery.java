package pl.lodz.p.ftims.model.storage.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Delivery {
    private String id;
    private LocalDate date;
    private boolean completed;
    private List<ProductLine> products;

    public Delivery(String id, LocalDate date, boolean completed) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.products = new ArrayList<>();
    }

    public Delivery(String id, LocalDate date, List<ProductLine> products, boolean completed) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.products = new ArrayList<>(products);
    }

    public List<ProductLine> getProducts() {
        return products;
    }

    public void setProducts(List<ProductLine> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(int year, int month, int day) {
        this.date = LocalDate.of(year, month, day);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Double getTotalPrice() {
        double totalPrice = 0;
        for (ProductLine productLine : products) {
            totalPrice += productLine.getProduct().getPurchasePrice() * productLine.getQuantity();
        }
        return totalPrice;
    }

}
