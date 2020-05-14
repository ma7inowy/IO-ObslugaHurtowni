package pl.lodz.p.ftims;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.product.service.ProductService;
import pl.lodz.p.ftims.model.product.validators.ProductValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    //region [Fields]
    private ProductService productService;

    private Product product;

    @FXML
    private TextField productName, productPurchasePrice, productSellPrice, productDiscount;
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.productService = new ProductService();
    }

    public void submit() {
        try {
            ProductValidator.validate(this.productName.getText(), this.productPurchasePrice.getText(),
                    this.productSellPrice.getText(), this.productDiscount.getText());

            this.productService.addProduct(this.productName.getText(),
                    Double.parseDouble(this.productPurchasePrice.getText()),
                    Double.parseDouble(this.productSellPrice.getText()),
                    Double.parseDouble(this.productDiscount.getText()));

            this.switchMainWindow();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void switchMainWindow() {
        try {
            App.setRoot("MainWindow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            ProductValidator.validate(this.productName.getText(), this.productPurchasePrice.getText(),
                    this.productSellPrice.getText(), this.productDiscount.getText());

            productService.updateProduct(
                    this.product.getProductID(),
                    this.productName.getText(),
                    Double.parseDouble(this.productPurchasePrice.getText()),
                    Double.parseDouble(this.productSellPrice.getText()),
                    Double.parseDouble(this.productDiscount.getText())
            );

            this.switchMainWindow();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productName.setText(product.getName());
        this.productPurchasePrice.setText(product.getPurchasePrice().toString());
        this.productSellPrice.setText(product.getSellPrice().toString());
        this.productDiscount.setText(product.getDiscount().toString());
    }
}
