package pl.lodz.p.ftims;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import pl.lodz.p.ftims.model.user.model.User;
import pl.lodz.p.ftims.model.user.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    //region [Fields]
    private static final String
            STAFF = "Staff",
            WORKHOUSE_MANAGER = "WorkhouseManager",
            STOREKEEPER = "Storekeeper";

    private UserService userService;

    private User user;

    @FXML
    private TextField firstName, secondName, address, salary, login, password;

    @FXML
    private ToggleGroup type;
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userService = new UserService();
    }

    public void submit() {
        //TODO
        //Add validator
        this.userService.addUser(this.firstName.getText(),
                this.secondName.getText(),
                this.address.getText(),
                this.getSelectedType(),
                Double.parseDouble(this.salary.getText()),
                this.login.getText(),
                this.password.getText());
        this.switchMainWindow();
    }

    public void update() {
        userService.updateUser(
                user.getId(),
                firstName.getText(),
                secondName.getText(),
                address.getText(),
                getSelectedType(),
                Double.parseDouble(salary.getText()),
                login.getText(),
                password.getText());
        this.switchMainWindow();
    }

    public void switchMainWindow() {
        try {
            App.setRoot("MainWindow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getSelectedType() {
        RadioButton selectedRadioButton = (RadioButton) this.type.getSelectedToggle();
        String type = selectedRadioButton.getText();

        if (type.equals(STAFF))
            return User.STAFF;
        else if (type.equals(STOREKEEPER))
            return User.STOREKEEPER;
        else
            return User.WORKHOUSE_MANAGER;
    }

    public void setUser(User user) {
        this.user = user;
        firstName.setText(user.getFirstName());
        secondName.setText(user.getSecondName());
        address.setText(user.getAddress());
        salary.setText(String.valueOf(user.getSalary()));
        login.setText(user.getLogin());
        password.setText(user.getPassword());
        int userType = user.getTypeId();
        for (int i = 0; i < type.getToggles().size(); i++) {
            Toggle toggle = type.getToggles().get(i);
            RadioButton radioButton = (RadioButton) toggle;
            String text = radioButton.getText();
            if (text.equals(STAFF) && userType == User.STAFF) {
                toggle.setSelected(true);
            } else if (text.equals(WORKHOUSE_MANAGER) && userType == User.WORKHOUSE_MANAGER) {
                toggle.setSelected(true);
            } else if (text.equals(STOREKEEPER) && userType == User.STOREKEEPER) {
                toggle.setSelected(true);
            }
        }
    }
}
