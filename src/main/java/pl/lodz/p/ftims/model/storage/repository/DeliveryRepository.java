package pl.lodz.p.ftims.model.storage.repository;


import pl.lodz.p.ftims.database.communicationinterface.IDeliveryDatabase;
import pl.lodz.p.ftims.database.communicationinterface.IOrderDatabase;
import pl.lodz.p.ftims.database.managers.DeliveryDatabaseManager;
import pl.lodz.p.ftims.database.managers.OrderDatabaseManager;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.Order;

import java.time.LocalDate;
import java.util.List;

public class DeliveryRepository implements IDeliveryRepository {
    private IDeliveryDatabase deliveryDatabase;

    public DeliveryRepository() {
        deliveryDatabase = new DeliveryDatabaseManager();
    }

    public static LocalDate getDateFromYearMonthDay(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public Delivery getDelivery(String id) {
        return this.deliveryDatabase.getDelivery(id);
    }

    public List<Delivery> getAllDeliveries() {
        return this.deliveryDatabase.getDeliveries();
    }

    public int addDelivery(Delivery delivery) {
        return this.deliveryDatabase.addDelivery(delivery);
    }

    public void removeDelivery(String id) {
        this.deliveryDatabase.removeDelivery(id);
    }

    public void updateDelivery(Delivery delivery) {
        this.deliveryDatabase.modifyDelivery(delivery);
    }

}
