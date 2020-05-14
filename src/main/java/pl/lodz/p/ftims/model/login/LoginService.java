package pl.lodz.p.ftims.model.login;

import pl.lodz.p.ftims.database.communicationinterface.IUserDatabase;
import pl.lodz.p.ftims.database.managers.UserDatabaseManager;
import pl.lodz.p.ftims.model.user.model.User;

public class LoginService implements ILoginService {

    public static User user;
    private IUserDatabase userDatabase;

    public LoginService() {
        this.userDatabase = new UserDatabaseManager();
    }

    @Override
    public boolean login(String login, String password) {
        User userIn = userDatabase.getUser(login, password);
        if (userIn != null) {
            user = userIn;
            return true;
        }
        return false;
    }

    @Override
    public boolean logout() {
        if (user != null) {
            user = null;
            return true;
        } else {
            return false;
        }
    }
}
