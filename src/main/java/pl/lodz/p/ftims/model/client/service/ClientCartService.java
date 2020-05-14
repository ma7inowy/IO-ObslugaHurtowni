package pl.lodz.p.ftims.model.client.service;


import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.client.model.ClientStateEnum;
import pl.lodz.p.ftims.model.client.repository.ClientRepository;
import pl.lodz.p.ftims.model.client.repository.IClientRepository;
import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.finance.Service.FinanceService;
import pl.lodz.p.ftims.model.finance.Service.IFinanceService;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.product.service.IProductService;
import pl.lodz.p.ftims.model.product.service.ProductService;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.Order;
import pl.lodz.p.ftims.model.storage.model.ProductLine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientCartService implements IClientCartService, IClientTransactionService {

    private IProductService productService;
    private IFinanceService financeService;
    private List<ProductLine> productsCart;
    private IClientRepository clientRepository;

    public ClientCartService() {
        this.clientRepository = new ClientRepository();
        this.productService = new ProductService();
        this.financeService = new FinanceService();
        productsCart = new ArrayList<>();
    }

    public IProductService getProductService() {
        return productService;
    }

    public void setProductService(IProductService productService) {
        this.productService = productService;
    }

    public IFinanceService getFinanceService() {
        return financeService;
    }

    public void setFinanceService(IFinanceService financeService) {
        this.financeService = financeService;
    }

    public List<ProductLine> getProductsCart() {
        return productsCart;
    }

    public void setProductsCart(List<ProductLine> productsCart) {
        this.productsCart = productsCart;
    }

    public IClientRepository getClientRepository() {
        return clientRepository;
    }

    public void setClientRepository(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Product getProduct(String id) {
        return productService.getProduct(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public void addProductToCart(String id, int amount) {
        productsCart.add(new ProductLine("",getProduct(id), amount));
    }

    public void removeProductFromCart(String name) {
      productsCart.removeIf(productLine -> productLine.getProduct().getName().equals(name));
    }

    public ProductLine getProductLineFromCart(String name) {
        return productsCart.stream().filter(productLine -> productLine.getProduct().getName().equals(name)).findFirst().get();
    }

    public boolean canClientAddToCart(Client client){
        return clientRepository.getClient(client.getId()).getIsBanned();
    }

    @Override
    public List<Transaction> getClientTransactions(Client client) {
        return financeService.getClientTransactions(client);
    }

    private int countDoneTransaction(Client client){
        return (int)getClientTransactions(client).stream().
                filter(Transaction::getState).count();
    }

    private int countCancelledTransaction(Client client){
        return (int)getClientTransactions(client).stream().
                filter(t -> !t.getState()).count();
    }

    public void updateStatusClient(Client client) {
        int doneTransaction = countDoneTransaction(client);

        if(doneTransaction > 1 && doneTransaction <= 10)
            client.setState(ClientStateEnum.CUSTOM);
        else if(doneTransaction > 10)
            client.setState(ClientStateEnum.VIP);

        clientRepository.updateClient(client);
    }

    public Double getTotalPrice(){
        double totalPrice = 0.0;
        for(ProductLine productLine : getProductsCart()){
            totalPrice += productLine.getProduct().getSellPrice() * productLine.getQuantity() * productLine.getProduct().getDiscount();
        }
        return totalPrice;
    }

    public Order createOrder(Client client){
        LocalDate date = LocalDate.now();
        return new Order("", date, false, this.productsCart, client);
    }

    public Delivery createDelivery(){
        LocalDate date = LocalDate.now();
        return new Delivery("", date, productsCart, false);
    }

    public Double calculateDeliveryPrice(){
        double totalPrice = 0.0;
        for(ProductLine productLine : getProductsCart()){
            totalPrice += productLine.getProduct().getPurchasePrice() * productLine.getQuantity();
        }
        return totalPrice;
    }

    public void updateAllClientStatus() {
        List<Client> clients = clientRepository.getAll();
        for(Client client : clients){
            updateStatusClient(client);
        }
    }

}
