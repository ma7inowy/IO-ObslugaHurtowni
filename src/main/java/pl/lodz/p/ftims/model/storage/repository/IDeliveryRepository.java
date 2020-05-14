package pl.lodz.p.ftims.model.storage.repository;

import pl.lodz.p.ftims.model.storage.model.Delivery;

import java.util.List;

public interface IDeliveryRepository {
    Delivery getDelivery(String id);

    List<Delivery> getAllDeliveries();

    int addDelivery(Delivery delivery);

    void removeDelivery(String id);

    void updateDelivery(Delivery delivery);


}
