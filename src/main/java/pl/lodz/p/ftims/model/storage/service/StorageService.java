package pl.lodz.p.ftims.model.storage.service;

import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.finance.Repository.TransactionRepository;
import pl.lodz.p.ftims.model.finance.Service.FinanceService;
import pl.lodz.p.ftims.model.finance.Service.TransactionService;
import pl.lodz.p.ftims.model.login.LoginService;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.storage.repository.DeliveryRepository;
import pl.lodz.p.ftims.model.storage.repository.ProductLineRepository;

import java.util.Calendar;
import java.util.List;

public class StorageService implements IStorageService {
    private DeliveryRepository deliveryRepository;

    public StorageService(DeliveryRepository deliveryRepository, ProductLineRepository productLineRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public StorageService() {
        this.deliveryRepository = new DeliveryRepository();
    }

    public int addDelivery(Delivery delivery) {
        return deliveryRepository.addDelivery(delivery);
    }

    public void updateDelivery(Delivery delivery) {
        this.deliveryRepository.updateDelivery(delivery);
    }

    public Delivery getDelivery(String id) {
        return deliveryRepository.getDelivery(id);
    }

    public List<Delivery> getAllDeliveries() {
        return this.deliveryRepository.getAllDeliveries();
    }

    public void removeDelivery(String id) {
        deliveryRepository.removeDelivery(id);
    }

    public void createDeliveryWithTransaction(Delivery delivery) {
        TransactionService transactionService = new TransactionService(new TransactionRepository());
        int id = addDelivery(delivery);
        delivery.setId(String.valueOf(id));
        Transaction transaction = new Transaction("", null, LoginService.user, delivery.getProducts(), delivery.getTotalPrice(), Calendar.getInstance(), false, true, null, delivery);
        transactionService.addTransaction(transaction);
    }

    @Override
    public boolean finishDelivery(Delivery delivery) {
        TransactionService transactionService = new TransactionService(new TransactionRepository());
        ProductLineService productLineService = new ProductLineService();

        Transaction transaction = transactionService.getAllTransactions().stream()
                .filter(item -> {
                    Delivery transactionDelivery = item.getDelivery();
                    if (transactionDelivery != null) {
                        if (transactionDelivery.getId().equals(delivery.getId())) {
                            return true;
                        }
                    }
                    return false;
                }).findAny().orElse(null);
        if (transaction != null && transaction.getState()) {
            List<ProductLine> list = productLineService.getAllProductLines();

            for (int i = 0; i < delivery.getProducts().size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (delivery.getProducts().get(i).getProduct().getProductID().equals(list.get(j).getProduct().getProductID())) {
                        ProductLine productLine = list.get(j);
                        productLine.setQuantity(productLine.getQuantity() + delivery.getProducts().get(i).getQuantity());
                        productLineService.updateProductLine(productLine);
                    }
                }
            }

            delivery.setCompleted(true);
            this.deliveryRepository.updateDelivery(delivery);
            return true;
        }
        return false;
    }


    @Override
    public boolean canDeleteDelivery(Delivery delivery) {
        FinanceService financeService = new FinanceService();
        Transaction transaction = financeService.getTransactionByDeliveryId(delivery.getId());
        if (transaction != null)
            return !transaction.getState();
        return false;
    }

    @Override
    public boolean removeDeliveryWithTransaction(Delivery delivery) {
        if (this.canDeleteDelivery(delivery)) {
            FinanceService financeService = new FinanceService();
            Transaction transaction = financeService.getTransactionByDeliveryId(delivery.getId());
            removeDelivery(delivery.getId());
            TransactionService transactionService = new TransactionService(new TransactionRepository());
            return transactionService.deleteTransaction(transaction.getId());
        }
        return false;
    }

}
