package pl.lodz.p.ftims;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.client.service.ClientService;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {

    @FXML
    private TextField clientName;
    @FXML
    private CheckBox isBanned;

    private Client modifyableClient;
    private ClientService clientService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientService = new ClientService();
    }

    public void setClient(Client client) {
        clientName.setText(client.getName());
        isBanned.setSelected(client.getIsBanned());

        modifyableClient = client;
    }

    public void addClient() {
        clientService.addClient(clientName.getText());
        switchMainWindow();
    }

    private void displayConfirmAlert(String info, String info1) {
        Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
        alert.setTitle(info);
        alert.setTitle(info1);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && !clientName.getText().isEmpty()) {
            modifyableClient.setBanned(isBanned.isSelected());
            modifyableClient.setName(clientName.getText());
            clientService.updateClient(modifyableClient);
        }
    }

    private void displayErrorAlert(String info) {
        Alert alert = new Alert((Alert.AlertType.ERROR));
        alert.setTitle(info);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && !clientName.getText().isEmpty()) {
            switchMainWindow();
        }
    }

    public void modifyClientName() {
        if (clientName.getText().isEmpty()) {
            displayErrorAlert("Client name cant be empty!");
            switchMainWindow();
        } else if (modifyableClient.getIsBanned() && !isBanned.isSelected()) {
            displayConfirmAlert("Confirm undo ban client", "Are you sure to undo ban to this client?");
            switchMainWindow();
        } else if (!modifyableClient.getIsBanned() && isBanned.isSelected()) {
            displayConfirmAlert("Confirm ban client", "This client won't order any product");
            switchMainWindow();
        } else if (!clientName.getText().isEmpty()) {
            modifyableClient.setName(clientName.getText());
            clientService.updateClient(modifyableClient);
            switchMainWindow();
        }
    }

    public void switchMainWindow() {
        try {
            App.setRoot("MainWindow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
