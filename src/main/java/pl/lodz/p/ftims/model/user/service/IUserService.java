package pl.lodz.p.ftims.model.user.service;

import pl.lodz.p.ftims.model.user.model.User;

import java.util.List;

public interface IUserService {

    void addUser(String firstName,
                 String secondName,
                 String address,
                 int typeId,
                 double salary,
                 String login,
                 String password);

    void updateUser(String id,
                    String firstName,
                    String secondName,
                    String address,
                    int typeId,
                    double salary,
                    String login,
                    String password);

    void removeUser(String id);

    User getUser(String id);

    List<User> getAllUsers();
}

