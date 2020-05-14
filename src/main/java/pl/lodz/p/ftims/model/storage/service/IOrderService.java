package pl.lodz.p.ftims.model.storage.service;

import pl.lodz.p.ftims.model.storage.model.Order;

import java.util.List;

public interface IOrderService {

    int addOrder(Order order);

    Order getOrder(String id);

    List<Order> getAllOrder();

    void removeOrder(String id);

    void modifyOrder(Order order);

    void createOrderWithTransaction(Order order);

    boolean finishOrder(Order order);

    boolean canDeleteOrder(Order order);

    boolean removeOrderWithTransaction(Order order);
}
