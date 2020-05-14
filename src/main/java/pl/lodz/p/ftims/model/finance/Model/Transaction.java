package pl.lodz.p.ftims.model.finance.Model;

import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.Order;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.user.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Transaction {
    private String id = "";
    private Client client;
    private User employee;
    private List<ProductLine> products;
    private double price;
    private Calendar date;
    private boolean state;  // zrealizowana lub anulowana
    private boolean isDelivery; //true - dostawa, false - zamowienie klienta
    private Order order;
    private Delivery delivery;

    //Konstruktor głównie do db
    public Transaction(String id, Client client, User employee, List<ProductLine> products, double price, Calendar date, boolean state, boolean isDelivery, Order order, Delivery delivery) {
        this.id = id;
        this.client = client;
        this.employee = employee;
        this.products = new ArrayList<>(products);
        this.price = price;
        this.date = date;
        this.state = state;
        this.isDelivery = isDelivery;
        this.order = order;
        this.delivery = delivery;
    }

    public Transaction(Client client, User employee, List<ProductLine> products, Calendar date) {
        this.client = client;
        this.employee = employee;
        this.products = new ArrayList<>(products);
        this.date = date;
        this.isDelivery = isDelivery();
        double price = 0;
        if (isDelivery) {
            for (ProductLine pl : products) {
                price += pl.getProduct().getPurchasePrice() * pl.getQuantity(); //Bedzie tutaj trzeba uwzgledniac znizki
            }
        } else {
            for (ProductLine pl : products) {
                price += pl.getProduct().getSellPrice() * pl.getQuantity(); //Bedzie tutaj trzeba uwzgledniac znizki
            }
        }
        this.price = price;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client getClient() {
        return this.client;
    }

    public User getEmployee() {
        return this.employee;
    }

    public List<ProductLine> getProducts() {
        return this.products;
    }

    public double getPrice() {
        return this.price;
    }

    public Calendar getDate() {
        return this.date;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "order: " + order + " delivery: " + delivery +
                '}';
    }
}
