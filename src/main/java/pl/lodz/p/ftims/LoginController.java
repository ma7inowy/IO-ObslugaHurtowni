package pl.lodz.p.ftims;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pl.lodz.p.ftims.model.login.ILoginService;
import pl.lodz.p.ftims.model.login.LoginService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private ILoginService loginService;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loginService = new LoginService();
    }

    @FXML
    private void login() throws IOException {
        if (this.loginService.login(this.username.getText(), this.password.getText()))
            this.switchMainWindow();
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Authorization Error");
            alert.setContentText("Wrong login or password");
            alert.showAndWait();
            App.setRoot("LoginWindow");
        }
    }

    @FXML
    private void logout() throws IOException {
        if (this.loginService.logout())
            App.setRoot("LoginWindow");
        else {
            App.setRoot("MainWindow");
        }
    }

    @FXML
    private void switchMainWindow() throws IOException {
        App.setRoot("MainWindow");
    }

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField password) {
        this.password = password;
    }

    public TextField getUsername() {
        return username;
    }

    public void setUsername(TextField username) {
        this.username = username;
    }
}
