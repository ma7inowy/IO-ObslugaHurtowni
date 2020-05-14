package pl.lodz.p.ftims.model.storage.repository;

import pl.lodz.p.ftims.database.communicationinterface.IOrderDatabase;
import pl.lodz.p.ftims.database.managers.OrderDatabaseManager;
import pl.lodz.p.ftims.model.storage.model.Order;

import java.util.List;

public class OrderRepository implements IOrderRepository {
    private IOrderDatabase orderDatabase;

    public OrderRepository() {
        this.orderDatabase = new OrderDatabaseManager();
    }

    public Order getOrder(String id) {
        return orderDatabase.getOrder(id);
    }

    public List<Order> getAllOrders() {
        return orderDatabase.getOrders();
    }

    public int addOrder(Order order) {
        return orderDatabase.addOrder(order);
    }

    public void removeOrder(String id) {
        orderDatabase.removeOrder(id);
    }

    public void modifyOrder(Order order) {
        orderDatabase.modifyOrder(order);
    }
}