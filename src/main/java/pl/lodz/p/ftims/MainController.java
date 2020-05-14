package pl.lodz.p.ftims;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.client.model.Comment;
import pl.lodz.p.ftims.model.client.service.*;
import pl.lodz.p.ftims.model.finance.Model.Cashflow;
import pl.lodz.p.ftims.model.finance.Model.Transaction;
import pl.lodz.p.ftims.model.finance.Service.FinanceService;
import pl.lodz.p.ftims.model.finance.Service.IFinanceService;
import pl.lodz.p.ftims.model.login.LoginService;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.product.service.IProductService;
import pl.lodz.p.ftims.model.product.service.ProductService;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.Order;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.storage.service.*;
import pl.lodz.p.ftims.model.user.model.User;
import pl.lodz.p.ftims.model.user.service.IUserService;
import pl.lodz.p.ftims.model.user.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    //region [Fields]
    private IUserService userService = new UserService();
    private IProductService productService = new ProductService();
    private IClientService clientService = new ClientService();
    private IFinanceService financeService = new FinanceService();
    private IStorageService storageService = new StorageService();
    private IOrderService orderService = new OrderService();
    private ICommentService commentService = new CommentService();
    private IProductLineService productLineService = new ProductLineService();

    @FXML
    private Tab products, orders, clients, magazine, deliveries, users, comments, finance;
    //endregion

    //region [clients]
    @FXML
    private TableView<Client> tableViewClients;
    @FXML
    private TableColumn<Client, String>
            columnClientId,
            columnClientName,
            ColumnClientStatus;
    @FXML
    private TableColumn<Client, Boolean> tableColumnBannedClient;
    //endregion

    //region [product]
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String>
            productId,
            productName,
            productPurchasePrice,
            productSellPrice,
            productDiscount;
    //endregion

    //region [productLine]
    @FXML
    private TableView<ProductLine> productLineTable;
    @FXML
    private TableColumn<ProductLine, String>
            productLineId,
            productLineName,
            productLineQuantity;
    //endregion

    //region [users]
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String>
            userId,
            userFirstName,
            userLastName,
            userLogin,
            userAddress,
            userType,
            userSalary;

    //endregion

    //region [finance]
    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> transactionId,
            transactionDate,
            transactionClient,
            transactionAmount,
            transactionState,
            transactionType;
    //endregion

    @FXML
    private TableView<Cashflow> cashFlowTable;

    @FXML
    private TableColumn<Cashflow, String> cashFlowId, cashFlowDate, cashFlowAmount, cashFlowDescription;

    @FXML
    private Label actualState;

    private ObservableList<Cashflow> allCashFlow = FXCollections.observableArrayList(financeService.getAllCashflows());

    //region [delivery]
    @FXML
    private TableView<Delivery> deliveryTable;
    @FXML
    private TableColumn<Delivery, String> deliveryId, deliveryDate, deliveryCompleted, deliveryTotalPrice;
    private ObservableList<Delivery> allDeliveries = FXCollections.observableArrayList(storageService.getAllDeliveries());

    @FXML
    private TableView<ProductLine> deliveryTableDetails;

    @FXML
    private TableColumn<ProductLine, String> deliveryProductDetail, deliveryQuantityDetail;
    private ObservableList<ProductLine> deliveryProductLineDetails;
    //endregion

    //region [order]
    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> orderId, clientOrderName, totalPrice, orderCompleted;

    @FXML
    private TextField loggedAs;
    private ObservableList<Order> allOrders = FXCollections.observableArrayList(orderService.getAllOrder());

    @FXML
    private TableView<ProductLine> orderTableDetails;

    @FXML
    private TableColumn<ProductLine, String> orderProductDetail, orderQuantityDetail;

    private ObservableList<ProductLine> orderProductLineDetails;
    //endregion

    //region [comment]
    @FXML
    private TableView<Comment> commentTable;

    @FXML
    private TableColumn<Comment, String>
            userName,
            commentId,
            clientName,
            comment;
    private ObservableList<Comment> allComments = FXCollections.observableArrayList(commentService.getAllComment());
    //endregion
    //region [transaction management]
    private ObservableList<Transaction> transactions = FXCollections.observableList(financeService.getAllTransactions());
    //region [User Management]
    private ObservableList<User> usersList = FXCollections.observableList(userService.getAllUsers());
    //region [Product Management]
    private ObservableList<Product> productsList = FXCollections.observableList(productService.getAllProducts());
    //region [ProductLine Management}
    private ObservableList<ProductLine> allProductLines = FXCollections.observableArrayList(productLineService.getAllProductLines());


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.check();
        loggedAs.setText(LoginService.user.getLogin());
        initUserTable();
        initProductTable();
        initializeClientsTable();
        initTransactionTable();
        initDeliveriesTable();
        initOrderTable();
        initCommentTable();
        initProductLineTable();
        initCashFlowTable();
        refreshAccountState();
        refreshClientStatus();
    }

    private void refreshAccountState(){
        this.actualState.setText(String.valueOf(financeService.getBalance()));
        this.actualState.setFont(Font.font(15));
    }

    //endregion

    private void initTransactionTable() {
        transactionId.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleStringProperty(transactionStringCellDataFeatures.getValue().getId()));
        transactionAmount.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(transactionStringCellDataFeatures.getValue().getPrice())));
        transactionClient.setCellValueFactory(transactionStringCellDataFeatures -> {
            Client client = transactionStringCellDataFeatures.getValue().getClient();
            User user = transactionStringCellDataFeatures.getValue().getEmployee();
            if (client != null)
                return new SimpleStringProperty(client.getName());
            else return new SimpleStringProperty(user.getLogin());
        });
        transactionDate.setCellValueFactory(transactionStringCellDataFeatures -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(transactionStringCellDataFeatures.getValue().getDate().getTimeInMillis());
            return new SimpleStringProperty(simpleDateFormat.format(date));
        });
        transactionState.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(transactionStringCellDataFeatures.getValue().getState())));
        transactionType.setCellValueFactory(transactionStringCellDataFeatures -> {
            Transaction transaction = transactionStringCellDataFeatures.getValue();
            if (transaction.isDelivery())
                return new SimpleStringProperty("Delivery");
            else return new SimpleStringProperty("Order");
        });
        transactionTable.setItems(transactions);
    }
    //endregion

    private void initCashFlowTable() {
        this.cashFlowId.setCellValueFactory(cashflowStringCellDataFeatures ->
                new SimpleStringProperty(cashflowStringCellDataFeatures.getValue().getId())
        );

        this.cashFlowAmount.setCellValueFactory(cashflowStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(cashflowStringCellDataFeatures.getValue().getAmount()))
        );

        this.cashFlowDate.setCellValueFactory(cashflowStringCellDataFeatures -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(simpleDateFormat.format(cashflowStringCellDataFeatures.getValue().getDate().getTimeInMillis()));
        });

        this.cashFlowDescription.setCellValueFactory(cashflowStringCellDataFeatures ->
                new SimpleStringProperty(cashflowStringCellDataFeatures.getValue().getName())
        );
        this.cashFlowTable.setItems(this.allCashFlow);
    }

    private void refreshCashFlowTable() {
        this.allCashFlow = FXCollections.observableArrayList(financeService.getAllCashflows());
        this.cashFlowTable.setItems(this.allCashFlow);
    }

    //region [Login Management]
    @FXML
    private void switchEnsureWindow() {
        try {
            App.setRoot("EnsureWindow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchAddUserWindow() {
        try {
            App.setRoot("AddUserWindow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchModifyUserWindow() throws IOException {
        User user = this.usersTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("ModifyUserWindow.fxml"));
            Parent parent = loader.load();
            App.setRoot(parent);
            UserController userController = loader.getController();
            userController.setUser(user);
        }
    }

    private void initUserTable() {
        userId.setCellValueFactory(userStringCellDataFeatures ->
                new SimpleStringProperty(userStringCellDataFeatures.getValue().getId())
        );

        userFirstName.setCellValueFactory(userStringCellDataFeatures ->
                new SimpleStringProperty(userStringCellDataFeatures.getValue().getFirstName())
        );

        userLastName.setCellValueFactory(userStringCellDataFeatures ->
                new SimpleStringProperty(userStringCellDataFeatures.getValue().getSecondName())
        );

        userLogin.setCellValueFactory(userStringCellDataFeatures ->
                new SimpleStringProperty(userStringCellDataFeatures.getValue().getLogin())
        );

        userAddress.setCellValueFactory(userStringCellDataFeatures ->
                new SimpleStringProperty(userStringCellDataFeatures.getValue().getAddress()))
        ;
        userType.setCellValueFactory(userStringCellDataFeatures -> {
            int type = userStringCellDataFeatures.getValue().getTypeId();
            if (type == User.WORKHOUSE_MANAGER)
                return new SimpleStringProperty("Workhouse Manager");
            if (type == User.STAFF)
                return new SimpleStringProperty("Staff");

            return new SimpleStringProperty("Storekeeper");
        });
        userSalary.setCellValueFactory(userDoubleCellDataFeatures -> {
            Double salary = userDoubleCellDataFeatures.getValue().getSalary();
            return new SimpleStringProperty(salary.toString());
        });
        usersTable.setItems(usersList);
    }
    //endregion

    @FXML
    private void refreshUsers() {
        this.usersList = FXCollections.observableList(userService.getAllUsers());
        usersTable.setItems(usersList);
    }

    @FXML
    private void deleteUser() {
        User user = this.usersTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm delete user");
            alert.setTitle("Are you sure to delete this user?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                userService.removeUser(user.getId());
            }
        }
        refreshAll();
    }

    //region [Comment Management]
    private void initCommentTable() {
        userName.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getUser().getLogin()));
        commentId.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getCommentID()));
        clientName.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getClient().getName()));
        comment.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getComment()));
        commentTable.setItems(allComments);
    }
    //endregion

    @FXML
    private void deleteComment() {
        Comment comment = this.commentTable.getSelectionModel().getSelectedItem();

        if (comment != null) {
            Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
            alert.setTitle("Confirm delete comment");
            alert.setTitle("Are you sure to delete this comment?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if(LoginService.user.getTypeId() == User.WORKHOUSE_MANAGER) {
                    this.commentService.removeComment(comment.getCommentID());
                }
                else{
                    alert.setTitle("Error!");
                    alert.setHeaderText("Operation prohibited");
                    alert.setContentText("Comment cannot be deleted");
                    alert.showAndWait();
                }
            }
        }
        refreshAll();
    }

    @FXML
    private void refreshComments() {
        this.allComments = FXCollections.observableList(commentService.getAllComment());
        commentTable.setItems(allComments);
    }

    @FXML
    private void switchAddProductWindow() {
        try {
            App.setRoot("AddProductWindow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchModifyProductWindow() throws IOException {
        Product product = this.productsTable.getSelectionModel().getSelectedItem();
        if (product != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("ModifyProductWindow.fxml"));
            Parent parent = loader.load();
            App.setRoot(parent);
            ProductController productController = loader.getController();
            productController.setProduct(product);
        }
    }

    private void initProductTable() {
        productId.setCellValueFactory(productStringCellDataFeatures ->
                new SimpleStringProperty(productStringCellDataFeatures.getValue().getProductID())
        );

        productName.setCellValueFactory(productStringCellDataFeatures ->
                new SimpleStringProperty(productStringCellDataFeatures.getValue().getName())
        );

        productPurchasePrice.setCellValueFactory(productStringCellDataFeatures ->
                new SimpleStringProperty(productStringCellDataFeatures.getValue().getPurchasePrice().toString())
        );

        productSellPrice.setCellValueFactory(productStringCellDataFeatures ->
                new SimpleStringProperty(productStringCellDataFeatures.getValue().getSellPrice().toString())
        );

        productDiscount.setCellValueFactory(productStringCellDataFeatures ->
                new SimpleStringProperty(productStringCellDataFeatures.getValue().getDiscount().toString())
        );
        productsTable.setItems(productsList);
    }

    @FXML
    private void deleteProduct() {
        Product product = this.productsTable.getSelectionModel().getSelectedItem();

        if (product != null) {
            Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
            alert.setTitle("Confirm delete product");
            alert.setTitle("Are you sure to delete this product?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                List<ProductLine> productLines = this.productLineService.getAllProductLines().stream().filter(productLine -> productLine.getProduct().getProductID().equals(product.getProductID())).collect(Collectors.toList());
                this.productService.removeProduct(product.getProductID());
                for(ProductLine productLine : productLines){
                    this.productLineService.removeProductLine(productLine.getProductLineId());
                }

            }
        }
        refreshAll();
    }
    //endregion

    @FXML
    private void refreshProducts() {
        this.productsList = FXCollections.observableList(productService.getAllProducts());
        productsTable.setItems(productsList);
    }

    //region [MainWindow]
    @FXML
    private void check() {
        if (LoginService.user.getTypeId() == User.STAFF) {
            this.deliveries.setDisable(true);
            this.users.setDisable(true);
            this.magazine.setDisable(true);
            this.finance.setDisable(true);
        } else if (LoginService.user.getTypeId() == User.STOREKEEPER) {
            this.clients.setDisable(true);
            this.users.setDisable(true);
            this.finance.setDisable(true);
        } else if (LoginService.user.getTypeId() == User.WORKHOUSE_MANAGER) {
            // manager is able to do everything
        }
    }

    @FXML
    private void switchAddClientWindow() throws IOException {
        App.setRoot("AddClientWindow");
    }

    @FXML
    private void switchModifyClientWindow() throws IOException {
        Client client = setSelectedClient();
        if (client != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("ModifyClientWindow.fxml"));
            Parent parent = loader.load();
            App.setRoot(parent);
            ClientsController clientController = loader.getController();
            clientController.setClient(client);
        }
    }

    private void initializeClientsTable() {
        columnClientId.setCellValueFactory(new PropertyValueFactory<Client, String>("Id"));
        columnClientName.setCellValueFactory(new PropertyValueFactory<Client, String>("Name"));
        ColumnClientStatus.setCellValueFactory(new PropertyValueFactory<Client, String>("State"));
        tableColumnBannedClient.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().getIsBanned()));
        tableColumnBannedClient.setCellFactory(CheckBoxTableCell.forTableColumn(tableColumnBannedClient));
        ObservableList<Client> clients = FXCollections.observableArrayList((clientService.getClients()));
        tableViewClients.setItems(clients);
    }

    private void updateClientTable(List<Client> array) {
        ObservableList<Client> obsevableClients = FXCollections.observableArrayList(array);
        tableViewClients.setItems(obsevableClients);
    }

    @FXML
    public Client setSelectedClient() {
        return tableViewClients.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void refreshClients() {
        updateClientTable(clientService.getClients());
    }

    //endregion

    @FXML
    public void deleteClient() {
        Client client = setSelectedClient();
        if (client != null) {
            Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
            alert.setTitle("Confirm delete client");
            alert.setTitle("Are you sure to delete this client?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
             if(financeService.getClientTransactions(client).size() == 0)
                clientService.removeClient(client);
                else{
                 Alert error = new Alert(Alert.AlertType.ERROR);
                 error.setTitle("Error!");
                 error.setHeaderText("Operation prohibited");
                 error.setContentText("Client cannot be deleted");
                 error.showAndWait();
             }
            }
        }
        refreshAll();
    }

    public void initProductLineTable() {
        productLineId.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getProductLineId()));
        productLineName.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getProduct().getName()));
        productLineQuantity.setCellValueFactory(val -> new SimpleStringProperty(Integer.toString(val.getValue().getQuantity())));
        productLineTable.setItems(allProductLines);
    }
    //endregion

    //region [deliveries Management]
    public void initDeliveriesTable() {
        deliveryId.setCellValueFactory(deliveryStringCellDataFeatures ->
                new SimpleStringProperty(deliveryStringCellDataFeatures.getValue().getId()));
        deliveryDate.setCellValueFactory(deliveryStringCellDataFeatures -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = java.sql.Date.valueOf(deliveryStringCellDataFeatures.getValue().getDate());
            return new SimpleStringProperty(simpleDateFormat.format(date));
        });
        deliveryCompleted.setCellValueFactory(deliveryStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(deliveryStringCellDataFeatures.getValue().isCompleted())));
        deliveryTotalPrice.setCellValueFactory(deliveryStringCellDataFeatures ->
                new SimpleStringProperty(deliveryStringCellDataFeatures.getValue().getTotalPrice().toString())
        );
        deliveryTable.setItems(allDeliveries);
    }

    public void finishDelivery() {
        Delivery delivery = this.deliveryTable.getSelectionModel().getSelectedItem();
        if (delivery == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Nothing checked");
            alert.setContentText("You have to choose some item");
            alert.showAndWait();
        } else if (delivery.isCompleted() != true) {
            if (!this.storageService.finishDelivery(delivery)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Operation Prohibited");
                alert.setContentText("Transaction combined with this delivery is not finished");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Operation Prohibited");
            alert.setContentText("Completed delivery cannot be finished");
            alert.showAndWait();
        }
        refreshAll();
    }

    public void switchAddDelivery() {
        try {
            App.setRoot("CreateDelivery");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshDeliveries() {
        this.allDeliveries = FXCollections.observableList(storageService.getAllDeliveries());
        deliveryTable.setItems(allDeliveries);
    }

    @FXML
    public void deleteDelivery() {
        Delivery delivery = deliveryTable.getSelectionModel().getSelectedItem();
        if (delivery != null) {
            if (storageService.removeDeliveryWithTransaction(delivery)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Success");
                alert.setContentText("Delivery has been deleted");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Operation prohibited");
                alert.setContentText("Delivery cannot be deleted");
                alert.showAndWait();
            }
            refreshAll();
        }
    }

    @FXML
    public void showDeliveryDetails() {
        Delivery delivery = this.deliveryTable.getSelectionModel().getSelectedItem();
        if (delivery != null) {
            this.deliveryProductLineDetails = FXCollections.observableArrayList(delivery.getProducts());
            initDeliveryProductLineDetailsTable();
        }
    }

    private void initDeliveryProductLineDetailsTable() {
        this.deliveryProductDetail.setCellValueFactory(productLineStringCellDataFeatures ->
                new SimpleStringProperty(productLineStringCellDataFeatures.getValue().getProduct().getName())
        );

        this.deliveryQuantityDetail.setCellValueFactory(productLineStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(productLineStringCellDataFeatures.getValue().getQuantity()))
        );
        this.deliveryTableDetails.setItems(this.deliveryProductLineDetails);
    }

    //endregion

    //region [order Management]
    @FXML
    private void switchCreateOrder() {
        try {
            App.setRoot("CreateOrder");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initOrderTable() {
        orderId.setCellValueFactory(orderStringCellDataFeatures ->
                new SimpleStringProperty(orderStringCellDataFeatures.getValue().getId())
        );

        clientOrderName.setCellValueFactory(orderStringCellDataFeatures ->
                new SimpleStringProperty(orderStringCellDataFeatures.getValue().getClient().getName())
        );

        totalPrice.setCellValueFactory(orderStringCellDataFeatures -> {
                    FinanceService financeService = new FinanceService();
                    Transaction transaction = financeService.getTransactionByOrderId(orderStringCellDataFeatures.getValue().getId());
                    return new SimpleStringProperty(String.valueOf(transaction.getPrice()));
                }
        );

        orderCompleted.setCellValueFactory(orderStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(orderStringCellDataFeatures.getValue().getCompleted()))
        );

        orderTable.setItems(allOrders);
    }

    @FXML
    private void refreshOrders() {
        this.allOrders = FXCollections.observableList(orderService.getAllOrder());
        orderTable.setItems(allOrders);
    }

    @FXML
    public void deleteOrder() {
        Order order = orderTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            if (orderService.removeOrderWithTransaction(order)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Success");
                alert.setContentText("Order has been deleted");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Operation prohibited");
                alert.setContentText("Order cannot be deleted");
                alert.showAndWait();
            }
        }
        refreshAll();
    }

    @FXML
    public void finishOrder() {
        Order order = this.orderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Nothing checked");
            alert.setContentText("You have to choose some item");
            alert.showAndWait();
        } else if (!order.isCompleted()) {
            if (!this.orderService.finishOrder(order)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Operation Prohibited");
                alert.setContentText("Transaction combined with this order is not finished, check magazine state");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Operation Prohibited");
            alert.setContentText("Completed order cannot be finished");
            alert.showAndWait();
        }
        refreshAll();
    }
    //endregion

    private void refreshProductLine() {
        allProductLines = FXCollections.observableArrayList(productLineService.getAllProductLines());
        productLineTable.setItems(allProductLines);
    }

    public void refreshAll() {
        refreshOrders();
        refreshDeliveries();
        refreshClients();
        refreshComments();
        refreshProducts();
        refreshUsers();
        refreshProductLine();
        refreshTransaction();
        refreshCashFlowTable();
        refreshClientStatus();
        refreshAccountState();
    }

    private void refreshClientStatus() {
        ClientCartService clientCartService = new ClientCartService();
        clientCartService.updateAllClientStatus();
    }

    public void refreshTransaction() {
        this.transactions = FXCollections.observableArrayList(financeService.getAllTransactions());
        this.transactionTable.setItems(this.transactions);
    }

    @FXML
    public void paySalary() {
        boolean flag = financeService.paySalaries(userService.getAllUsers());
        if (flag) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText("Success");
            alert.setContentText("The money has been transferred ");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Operation Prohibited");
            alert.setContentText("Not enough money");
            alert.showAndWait();
        }
        refreshAll();
    }

    @FXML
    public void showOrderDetails() {
        Order order = orderTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            this.orderProductLineDetails = FXCollections.observableArrayList(order.getProducts());
            intiOrderDetailsTable();
        }
    }

    private void intiOrderDetailsTable() {
        this.orderProductDetail.setCellValueFactory(productLineStringCellDataFeatures ->
                new SimpleStringProperty(productLineStringCellDataFeatures.getValue().getProduct().getName())
        );

        this.orderQuantityDetail.setCellValueFactory(productLineStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(productLineStringCellDataFeatures.getValue().getQuantity()))
        );

        this.orderTableDetails.setItems(this.orderProductLineDetails);
    }

    @FXML
    public void createFinaceRaport() {
        financeService.createTransactionRaport(financeService.getAllTransactions());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finance Raport");
        alert.setContentText("The finance report has been created");
        alert.showAndWait();
        refreshAll();
    }

    @FXML
    public void createProductRaport() {
        productService.createProductRaport(productService.getAllProducts());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Raport");
        alert.setContentText("The product report has been created");
        alert.showAndWait();
        refreshAll();
    }
}