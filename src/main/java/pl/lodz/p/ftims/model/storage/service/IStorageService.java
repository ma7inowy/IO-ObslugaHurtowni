package pl.lodz.p.ftims.model.storage.service;


import pl.lodz.p.ftims.model.storage.model.Delivery;

import java.util.List;

public interface IStorageService {
    int addDelivery(Delivery delivery);

    void updateDelivery(Delivery delivery);

    Delivery getDelivery(String id);

    List<Delivery> getAllDeliveries();

    void removeDelivery(String id);

    void createDeliveryWithTransaction(Delivery delivery);

    boolean finishDelivery(Delivery delivery);

    boolean canDeleteDelivery(Delivery delivery);

    boolean removeDeliveryWithTransaction(Delivery delivery);
}
