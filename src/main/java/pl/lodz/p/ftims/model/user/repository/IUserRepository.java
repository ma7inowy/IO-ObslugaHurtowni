package pl.lodz.p.ftims.model.user.repository;

import pl.lodz.p.ftims.model.user.model.User;

import java.util.List;

public interface IUserRepository {
    boolean addUser(User user);

    boolean updateUser(User user);

    boolean removeUser(String id);

    User getUser(String id);

    List<User> getAll();
}
