package pl.lodz.p.ftims.database.communicationinterface;

import pl.lodz.p.ftims.model.storage.model.Delivery;

import java.util.List;

public interface IDeliveryDatabase {
    int addDelivery(Delivery delivery);

    boolean modifyDelivery(Delivery delivery);

    boolean removeDelivery(String id);

    List<Delivery> getDeliveries();

    Delivery getDelivery(String id);
}

