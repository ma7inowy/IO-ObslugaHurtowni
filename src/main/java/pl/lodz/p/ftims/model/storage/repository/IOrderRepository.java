package pl.lodz.p.ftims.model.storage.repository;

import pl.lodz.p.ftims.model.storage.model.Order;

import java.util.List;

public interface IOrderRepository {
    Order getOrder(String id);

    List<Order> getAllOrders();

    void removeOrder(String id);

    int addOrder(Order order);

    void modifyOrder(Order order);
}
