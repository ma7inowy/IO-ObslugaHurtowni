package pl.lodz.p.ftims.database.communicationinterface;


import pl.lodz.p.ftims.model.storage.model.Order;

import java.util.List;

public interface IOrderDatabase {
    int addOrder(Order order);

    boolean modifyOrder(Order order);

    boolean removeOrder(String id);

    List<Order> getOrders();

    Order getOrder(String id);
}
