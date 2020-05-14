package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.IUserDatabase;
import pl.lodz.p.ftims.model.user.model.Staff;
import pl.lodz.p.ftims.model.user.model.Storekeeper;
import pl.lodz.p.ftims.model.user.model.User;
import pl.lodz.p.ftims.model.user.model.WorkhouseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseManager implements IUserDatabase {

    private static final int WORKHOUSE_MANAGER = 0;
    private static final int STAFF = 1;
    private static final int STOREKEEPER = 2;
    private Database database = DatabaseSingleton.getInstance();

    public UserDatabaseManager() {

    }

    @Override
    public boolean addUser(User user) {
        try {
            PreparedStatement preparedStatement = database.getConn().prepareStatement(
                    "INSERT INTO employees (firstName, secondName, address, typeId, salary, login, password) " +
                            "VALUES (?,?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getSecondName());
            preparedStatement.setString(3, user.getAddress());
            String className = user.getClass().getSimpleName();
            if (className.equals("WorkhouseManager")) {
                preparedStatement.setInt(4, WORKHOUSE_MANAGER);
            } else if (className.equals("Staff")) {
                preparedStatement.setInt(4, STAFF);
            } else {
                preparedStatement.setInt(4, STOREKEEPER);
            }
            preparedStatement.setDouble(5, user.getSalary());
            preparedStatement.setString(6, user.getLogin());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.execute();
            preparedStatement.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while adding user to database");
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public boolean modifyUser(User e) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "update employees " +
                            "set firstName=?, secondName=?, address=?, typeId=?, salary=?, login=?,password=?" +
                            "where employeeID=?;");
            prepStmt.setString(1, e.getFirstName());
            prepStmt.setString(2, e.getSecondName());
            prepStmt.setString(3, e.getAddress());
            prepStmt.setInt(4, e.getTypeId());
            prepStmt.setDouble(5, e.getSalary());
            prepStmt.setString(6, e.getLogin());
            prepStmt.setString(7, e.getPassword());
            prepStmt.setInt(8, Integer.parseInt(e.getId().toString()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return getUser(e.getId()).getId().equals(e.getId());
        } catch (SQLException ex) {
            System.err.println("Error while modifying user");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUser(String id) {
        try {
            if (getUser(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("DELETE FROM employees WHERE employeeID=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing user with id=" + id + "from database");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUser(String id) {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM employees where employeeID=" + id);
            String userId = String.valueOf(result.getInt("employeeID"));
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            String address = result.getString("address");
            int typeId = result.getInt("typeId");
            double salary = result.getInt("salary");
            String login = result.getString("login");
            String password = result.getString("password");
            result.close();
            stat.close();
            database.closeConnection();
            if (typeId == WORKHOUSE_MANAGER) {
                return new WorkhouseManager(userId, firstName, secondName, address, typeId, salary, login, password);
            } else if (typeId == STAFF) {
                return new Staff(id, firstName, secondName, address, typeId, salary, login, password);
            } else {
                return new Storekeeper(id, firstName, secondName, address, typeId, salary, login, password);
            }
        } catch (SQLException e) {
            System.err.println("Error while getting client with id=" + id + " from database");
            e.printStackTrace();
            return null;
        }
    }

    // TODO type
    @Override
    public User getUser(String login, String password) {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM employees WHERE login='"+ login + "' AND password='"+password +"'");
            if (result.next()) {
                String userId = String.valueOf(result.getInt("employeeID"));
                String firstName = result.getString("firstName");
                String secondName = result.getString("secondName");
                String address = result.getString("address");
                int typeId = result.getInt("typeId");
                double salary = result.getInt("salary");
                result.close();
                stat.close();
                database.closeConnection();
                if (typeId == WORKHOUSE_MANAGER) {
                    return new WorkhouseManager(userId, firstName, secondName, address, typeId, salary, login, password);
                } else if (typeId == STAFF) {
                    return new Staff(userId, firstName, secondName, address, typeId, salary, login, password);
                } else {
                    return new Storekeeper(userId, firstName, secondName, address, typeId, salary, login, password);
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting user by login/password from database");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = database.getConn().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM employees");
            while (result.next()) {
                String userId = String.valueOf(result.getInt("employeeID"));
                String firstName = result.getString("firstName");
                String secondName = result.getString("secondName");
                String address = result.getString("address");
                int typeId = result.getInt("typeId");
                double salary = result.getInt("salary");
                String login = result.getString("login");
                String password = result.getString("password");
                if (typeId == WORKHOUSE_MANAGER) {
                    users.add(new WorkhouseManager(userId, firstName, secondName, address, typeId, salary, login, password));
                } else if (typeId == STAFF) {
                    users.add(new Staff(userId, firstName, secondName, address, typeId, salary, login, password));
                } else {
                    users.add(new Storekeeper(userId, firstName, secondName, address, typeId, salary, login, password));
                }
            }
            result.close();
            statement.close();
            database.closeConnection();
            return users;
        } catch (SQLException e) {
            System.err.println("Error while getting users from database");
            e.printStackTrace();
            return null;
        }
    }

}

