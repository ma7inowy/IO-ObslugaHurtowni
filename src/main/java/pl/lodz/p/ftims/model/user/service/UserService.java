package pl.lodz.p.ftims.model.user.service;

import pl.lodz.p.ftims.model.user.model.Staff;
import pl.lodz.p.ftims.model.user.model.Storekeeper;
import pl.lodz.p.ftims.model.user.model.User;
import pl.lodz.p.ftims.model.user.model.WorkhouseManager;
import pl.lodz.p.ftims.model.user.repository.IUserRepository;
import pl.lodz.p.ftims.model.user.repository.UserRepository;

import java.util.List;

public class UserService implements IUserService {
    private IUserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    @Override
    public void addUser(String firstName,
                        String secondName,
                        String address,
                        int typeId,
                        double salary,
                        String login,
                        String password) {
        if (typeId == User.STAFF) {
            this.userRepository.addUser(new Staff("", firstName, secondName, address, typeId, salary, login, password));
        } else if (typeId == User.STOREKEEPER) {
            this.userRepository.addUser(new Storekeeper("", firstName, secondName, address, typeId, salary, login, password));
        } else {
            this.userRepository.addUser(new WorkhouseManager("", firstName, secondName, address, typeId, salary, login, password));
        }
    }

    @Override
    public void updateUser(String id, String firstName, String secondName, String address, int typeId, double salary, String login, String password) {
        if(typeId == User.STOREKEEPER){
            this.userRepository.updateUser(new Storekeeper(id, firstName, secondName, address, typeId, salary, login, password));
        }else if(typeId == User.WORKHOUSE_MANAGER){
            this.userRepository.updateUser(new WorkhouseManager(id, firstName, secondName, address, typeId, salary, login, password));
        }else if(typeId == User.STAFF){
            this.userRepository.updateUser(new Staff(id, firstName, secondName, address, typeId, salary, login, password));
        }
    }

    @Override
    public void removeUser(String id) {
        this.userRepository.removeUser(id);
    }

    @Override
    public User getUser(String id) {
        return this.userRepository.getUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.getAll();
    }
}
