package pl.lodz.p.ftims;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.client.service.ClientCartService;
import pl.lodz.p.ftims.model.client.service.ClientService;
import pl.lodz.p.ftims.model.client.service.IClientService;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.product.service.IProductService;
import pl.lodz.p.ftims.model.product.service.ProductService;
import pl.lodz.p.ftims.model.storage.model.Delivery;
import pl.lodz.p.ftims.model.storage.model.ProductLine;
import pl.lodz.p.ftims.model.storage.service.IOrderService;
import pl.lodz.p.ftims.model.storage.service.IStorageService;
import pl.lodz.p.ftims.model.storage.service.OrderService;
import pl.lodz.p.ftims.model.storage.service.StorageService;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CreateOrderController implements Initializable {

    //region [private]
    private IClientService clientService = new ClientService();
    private IProductService productService = new ProductService();
    private IOrderService orderService = new OrderService();
    private ClientCartService clientCartService = new ClientCartService();
    private IStorageService storageService = new StorageService();

    @FXML
    private ComboBox<String> clientComboBox;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private TextField textQuantity;

    @FXML
    private TableView<ProductLine> orderTableView;

    @FXML
    private TableColumn<ProductLine, String> columnProductName, columnQuantity;
    private ObservableList<ProductLine> productLines = FXCollections.observableArrayList(clientCartService.getProductsCart());

    @FXML
    private Label totalPrice;
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (clientComboBox != null)
            clientComboBox.getItems().setAll(clientService.getAllClientsName());
        productComboBox.getItems().setAll(productService.getAllProductsName());
        initTable();
    }

    public void addProductToCard() {
        Product product = productService.getProductByName(productComboBox.getValue());
        int quantity = -1;
        String quantityText = textQuantity.getText();
        boolean isNumber = Pattern.matches("[0-9]+", quantityText);
        if (isNumber) {
            quantity = Integer.parseInt(quantityText);
        }
        if (quantity > 0 && product != null) {
            this.clientCartService.addProductToCart(product.getProductID(), quantity);
            if (clientComboBox != null)
                this.totalPrice.setText(clientCartService.getTotalPrice().toString());
            else this.totalPrice.setText(clientCartService.calculateDeliveryPrice().toString());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Wrong data");
            alert.setContentText("Product doesn't exist or wrong number format!");
            alert.showAndWait();
        }
        refreshTable();
    }

    public void createOrder() {
        Client client = clientService.getClientByName(clientComboBox.getValue());
        if (client != null && this.productLines != null) {
            if (!client.getIsBanned())
                orderService.createOrderWithTransaction(clientCartService.createOrder(client));
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Operation prohibited!");
                alert.setContentText("Client is banned");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Wrong data");
            alert.setContentText("Client or products is empty");
            alert.showAndWait();
        }
    }

    public void deleteProductFromCard() {
        ProductLine productLine = this.orderTableView.getSelectionModel().getSelectedItem();
        if (productLine != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm delete product");
            alert.setTitle("Are you sure to delete this product?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                clientCartService.removeProductFromCart(productLine.getProduct().getName());
                this.totalPrice.setText(clientCartService.getTotalPrice().toString());
                refreshTable();
            }
        }
    }

    private void initTable() {
        columnProductName.setCellValueFactory(productLineStringCellDataFeatures ->
                new SimpleStringProperty(productLineStringCellDataFeatures.getValue().getProduct().getName())
        );
        columnQuantity.setCellValueFactory(productLineStringCellDataFeatures ->
                new SimpleStringProperty(String.valueOf(productLineStringCellDataFeatures.getValue().getQuantity()))
        );
        orderTableView.setItems(productLines);
    }

    private void refreshTable() {
        this.productLines = FXCollections.observableArrayList(clientCartService.getProductsCart());
        orderTableView.setItems(this.productLines);
    }


    public void switchMainWindow() {
        try {
            App.setRoot("MainWindow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createDelivery() {
        if (this.productLines != null) {
            Delivery delivery = clientCartService.createDelivery();
            storageService.createDeliveryWithTransaction(delivery);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Wrong data");
            alert.setContentText("Products list is empty");
            alert.showAndWait();
        }
    }
}
