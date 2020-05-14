package pl.lodz.p.ftims.model.storage.service;

import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.finance.Repository.TransactionRepository;
import pl.lodz.p.ftims.model.finance.Service.FinanceService;
import pl.lodz.p.ftims.model.finance.Service.TransactionService;
import pl.lodz.p.ftims.model.storage.model.Order;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.storage.repository.IOrderRepository;
import pl.lodz.p.ftims.model.storage.repository.OrderRepository;

import java.util.Calendar;
import java.util.List;

public class OrderService implements IOrderService {

    private IOrderRepository orderRepository = new OrderRepository();

    public OrderService() {

    }

    @Override
    public int addOrder(Order order) {
        return orderRepository.addOrder(order);
    }

    @Override
    public Order getOrder(String id) {
        return orderRepository.getOrder(id);
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.getAllOrders();
    }

    @Override
    public void removeOrder(String id) {
        orderRepository.removeOrder(id);
    }

    @Override
    public void modifyOrder(Order order) {
        orderRepository.modifyOrder(order);
    }

    @Override
    public void createOrderWithTransaction(Order order) {
        TransactionService transactionService = new TransactionService(new TransactionRepository());
        int id = addOrder(order);
        order.setId(String.valueOf(id));
        Transaction transaction = new Transaction("", order.getClient(), null, order.getProducts(), order.getTotalPrice(), Calendar.getInstance(), false, false, order, null);
        transactionService.addTransaction(transaction);
    }

    @Override
    public boolean finishOrder(Order order) {
        TransactionService transactionService = new TransactionService(new TransactionRepository());
        ProductLineService productLineService = new ProductLineService();

        Transaction transaction = transactionService.getAllTransactions().stream()
                .filter(item -> {
                    Order transactionOrder = item.getOrder();
                    if (transactionOrder != null) {
                        return transactionOrder.getId().equals(order.getId());
                    }
                    return false;
                }).findAny().orElse(null);

        if (transaction != null && transaction.getState()) {
            List<ProductLine> list = productLineService.getAllProductLines();

            for (int i = 0; i < order.getProducts().size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (order.getProducts().get(i).getProduct().getProductID().equals(list.get(j).getProduct().getProductID())) {
                        ProductLine productLine = list.get(j);
                        if (productLine.getQuantity() - order.getProducts().get(i).getQuantity() < 0) {
                            return false;
                        }
                        productLine.setQuantity(productLine.getQuantity() - order.getProducts().get(i).getQuantity());
                        productLineService.updateProductLine(productLine);
                    }
                }
            }

            order.setCompleted(true);
            this.orderRepository.modifyOrder(order);
            return true;
        }
        return false;
    }

    @Override
    public boolean canDeleteOrder(Order order) {
        FinanceService financeService = new FinanceService();
        Transaction transaction = financeService.getTransactionByOrderId(order.getId());
        if (transaction != null)
            return !transaction.getState();
        return false;
    }

    @Override
    public boolean removeOrderWithTransaction(Order order) {
        if (this.canDeleteOrder(order)) {
            FinanceService financeService = new FinanceService();
            Transaction transaction = financeService.getTransactionByOrderId(order.getId());
            removeOrder(order.getId());
            TransactionService transactionService = new TransactionService(new TransactionRepository());
            return transactionService.deleteTransaction(transaction.getId());
        }
        return false;
    }
}
