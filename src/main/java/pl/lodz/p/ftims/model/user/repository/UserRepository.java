package pl.lodz.p.ftims.model.user.repository;

import pl.lodz.p.ftims.database.communicationinterface.IUserDatabase;
import pl.lodz.p.ftims.database.managers.UserDatabaseManager;
import pl.lodz.p.ftims.model.user.model.User;

import java.util.List;

public class UserRepository implements IUserRepository {
    private IUserDatabase dataBaseService;

    public UserRepository() {
        this.dataBaseService = new UserDatabaseManager();
    }

    @Override
    public boolean addUser(User user) {
        return dataBaseService.addUser(user);
    }

    @Override
    public boolean updateUser(User user) {
        return dataBaseService.modifyUser(user);
    }

    @Override
    public boolean removeUser(String id) {
        return dataBaseService.removeUser(id);
    }

    @Override
    public User getUser(String id) {
        return dataBaseService.getUser(id);
    }

    @Override
    public List<User> getAll() {
        return dataBaseService.getUsers();
    }
}
