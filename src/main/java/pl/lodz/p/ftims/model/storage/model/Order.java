package pl.lodz.p.ftims.model.storage.model;

import pl.lodz.p.ftims.model.client.model.Client;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private String id;
    private LocalDate date;
    private boolean completed;
    private List<ProductLine> products;
    private Client client;

    public Order(String id, LocalDate date, boolean completed, List<ProductLine> products, Client client) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.products = products;
        this.client = client;
    }

    public Order(String id, LocalDate date, boolean completed) {
        this.id = id;
        this.date = date;
        this.completed = completed;
    }

    public Order() {

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

    public boolean getCompleted() {
        return completed;
    }

    public List<ProductLine> getProducts() {
        return products;
    }

    public void setProducts(List<ProductLine> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", completed=" + completed +
                ", products=" + products +
                '}';
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Double getTotalPrice() {
        double totalPrice = 0;
        for (ProductLine productLine : products) {
            totalPrice += productLine.getQuantity() * productLine.getProduct().getSellPrice() * productLine.getProduct().getDiscount();
        }
        return totalPrice;
    }
}
