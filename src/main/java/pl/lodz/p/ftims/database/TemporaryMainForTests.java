package pl.lodz.p.ftims.database;

import pl.lodz.p.ftims.database.managers.*;
import pl.lodz.p.ftims.model.storage.model.Order;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.client.model.Comment;
import pl.lodz.p.ftims.model.finance.Model.Cashflow;
import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.user.model.Staff;
import pl.lodz.p.ftims.model.user.model.Storekeeper;
import pl.lodz.p.ftims.model.user.model.User;
import pl.lodz.p.ftims.model.user.model.WorkhouseManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TemporaryMainForTests {
    public static void main(String[] args) throws SQLException {
        System.out.println("Delete database file when testing");
        System.out.println("Hello world!");

        createDefaultUsers(new UserDatabaseManager());
//        clientTest(new ClientDatabaseManager());
//        transactionTest(new TransactionDatabaseManager());
//        orderTest(new OrderDatabaseManager());
//        productTest(new ProductDatabaseManager());
//        productLineTest(new ProductLineDatabaseManager());
//        deliveryTest(new DeliveryDatabaseManager());
//        commentTest(new CommentDatabaseManager());
//        cashflowTest(new CashflowDatabaseManager());
//        accountTest(new AccountDatabaseManager());
    }

    public static void accountTest(AccountDatabaseManager adm) {
        System.out.println("Account test ------------------------------");
        System.out.println(adm.addMoney(1000d));

    }

//    public static void commentTest(CommentDatabaseManager databaseManager) {
//        System.out.println("Comment test ------------------------------");
//        for (int i = 0; i < 10; i++) {
//            databaseManager.addComment(new Comment("1", "test" + i, "2"));
//        }
//        Comment comment = databaseManager.getComment("1");
//        System.out.println(comment.getComment());
//        comment.setComment("change");
//        System.out.println(databaseManager.modifyComment(comment));
//        databaseManager.removeComment("10");
//        System.out.println(databaseManager.getComments().size());
//    }

//    public static void transactionTest(TransactionDatabaseManager tdm) {
//        System.out.println("Transaction test ------------------------------");
//        ClientDatabaseManager cdm = new ClientDatabaseManager();
//        cdm.addClient(new Client("69", "testKlient", "new client", 0));
//        Client c = cdm.getClient("1");
//        UserDatabaseManager udm = new UserDatabaseManager();
//        User u = udm.getUser("1");
//
//        ProductDatabaseManager pdm = new ProductDatabaseManager();
//        pdm.addProduct(new Product("", "test22", 12.23, 14.44, 0.5));
//        ArrayList<ProductLine> test = new ArrayList<>();
//        test.add(new ProductLine(pdm.getProduct("1"), 12));
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        Transaction t = new Transaction("1", c, u, test, 1122.30, calendar, true, true);
//        Transaction t1 = new Transaction("1", c, u, test, 1222.30, calendar, false, true);
//        System.out.println(tdm.addTransaction(t));
//        System.out.println(tdm.getTransactions());
//        System.out.println(tdm.modifyTransaction(t1));
//        System.out.println(tdm.getTransactions());
//        //System.out.println(tdm.removeTransaction("1"));
//        //System.out.println(tdm.getTransactions());
//
//    }

//    public static void orderTest(OrderDatabaseManager odm) throws SQLException {
//        System.out.println("Order test ------------------------------");
//        ProductDatabaseManager pdm = new ProductDatabaseManager();
//        System.out.println(pdm.getProducts());
//        pdm.addProduct(new Product("", "test1", 1.0, 3.4, 0.5));
//        ProductLineDatabaseManager pldm = new ProductLineDatabaseManager();
//        pldm.addProductLine(new ProductLine(pdm.getProduct("1"), 2));
//        List<ProductLine> productLines = pldm.getProductLines();
//
//        LocalDate date = LocalDate.now();
////        odm.addOrder(new Order("", date, false, productLines));
//        System.out.println(odm.getOrders());
////        odm.modifyOrder(new Order("1", date, true, productLines));
//        System.out.println(odm.getOrders());
//        System.out.println(odm.getOrder("1"));
//        pdm.removeProduct("1");
//        //odm.removeOrder("1");
//        System.out.println(odm.getOrders());
//        System.out.println(odm.getOrder("1"));
//
//    }

    public static void clientTest(ClientDatabaseManager dm) {
        System.out.println("Client test ------------------------------");
        dm.addClient(new Client("", "Lidl", "new client", 0));
        dm.addClient(new Client("32", "Żabka", "new client", 0));
        dm.addClient(new Client("33", "Biedronka", "new client", 0));
        dm.addClient(new Client("34", "Kaufland", "new client", 0));
        dm.addClient(new Client("35", "Netto", "new client", 0));
        dm.addClient(new Client("36", "Mila", "new client", 0));
        dm.addClient(new Client("37", "Groszek", "new client", 0));
        dm.addClient(new Client("38", "Stokrotka", "new client", 0));
        dm.addClient(new Client("39", "Aldi", "new client", 0));
        dm.addClient(new Client("40", "Dino", "new client", 0));
        System.out.println(dm.getClient("4"));
        System.out.println(dm.removeClient("4"));
        Client client = dm.getClient("2");
        client.setName("test");
        System.out.println(dm.modifyClient(client));
        System.out.println(dm.getClients());
    }


    public static void createDefaultUsers(UserDatabaseManager databaseManager) {
        databaseManager.addUser(new Staff("1", "staff", "staff", "test", User.STAFF, 123, "staff", "staff123"));
        databaseManager.addUser(new WorkhouseManager("1", "workhouse", "manager", "admin", User.WORKHOUSE_MANAGER, 35000, "admin", "admin"));
        databaseManager.addUser(new Storekeeper("1", "storekeeper", "test", "test", User.STOREKEEPER, 93114, "storekeeper", "storekeeper123"));
    }

    public static void productTest(ProductDatabaseManager dm) {
        System.out.println("Product test ------------------------------");
        for (int i = 0; i < 10; i++) {
            dm.addProduct(new Product("", "test" + i, Double.valueOf(i), 5.0 + i, 0.5));
        }
        Product product = dm.getProduct("2");
        System.out.println(product.getName());
        product.setName("change");
        dm.modifyProduct(product);
        dm.removeProduct("9");
    }
//
//    public static void productLineTest(ProductLineDatabaseManager dm) {
//        System.out.println("ProductLine test ------------------------------");
//        ProductDatabaseManager productDatabaseManager = new ProductDatabaseManager();
//        for (int i = 0; i < 6; i++) {
//            dm.addProductLine(new ProductLine(productDatabaseManager.getProduct(String.valueOf(i + 2)), i));
//        }
//        ProductLine productLine = dm.getProductLine("3");
//        System.out.println(productLine.getQuantity());
//        productLine.setQuantity(129312);
//        dm.modifyProductLine(productLine);
//        dm.removeProductLine("8");
//    }

    public static void deliveryTest(DeliveryDatabaseManager dm) {
        System.out.println("Delivery test ------------------------------");
        ProductLineDatabaseManager productLineDatabaseManager = new ProductLineDatabaseManager();
        List<ProductLine> productLines = productLineDatabaseManager.getProductLines();
        for (int i = 0; i < 10; i++) {
            LocalDate date = LocalDate.of(2019, 12, i + 10);//now();
            dm.addDelivery(new Delivery("", date, productLines, false));
        }
        Delivery delivery = dm.getDelivery("1");
        System.out.println(delivery.getProducts().size());
        List<Delivery> deliveries = dm.getDeliveries();
        System.out.println(deliveries.size());
        System.out.println(dm.getDeliveries());
        System.out.println(dm.modifyDelivery(new Delivery("1", LocalDate.now(), productLines, true)));
        System.out.println(dm.getDeliveries());
        System.out.println(deliveries.get(0).getProducts().get(0).getQuantity());
        dm.removeDelivery("8");
        System.out.println(dm.getDeliveries());

    }

    public static void cashflowTest(CashflowDatabaseManager dm) {
        System.out.println("Cashflow test ------------------------------");
        CashflowDatabaseManager dbm = new CashflowDatabaseManager();
        Date date = new Date();
        date.setTime(191241241);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dbm.addCashflow(new Cashflow(6, calendar, 23, "Costam66"));
        System.out.println(dbm.getCashflows());
//         dbm.modifyCashflow(new Cashflow(1,date,23,"Costam2"));
         System.out.println(dbm.getCashflows());
         dbm.removeCashflow(1);
        System.out.println(dbm.getCashflow(1));
    }
}
