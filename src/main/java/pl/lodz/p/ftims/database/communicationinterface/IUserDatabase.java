package pl.lodz.p.ftims.database.communicationinterface;

import pl.lodz.p.ftims.model.user.model.User;

import java.util.List;

public interface IUserDatabase {
    boolean addUser(User employee);

    boolean modifyUser(User employee);

    boolean removeUser(String id);

    List<User> getUsers();

    User getUser(String id);

    User getUser(String login, String password);
}
