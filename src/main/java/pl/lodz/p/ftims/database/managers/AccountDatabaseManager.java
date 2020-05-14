package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.IAccountDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDatabaseManager implements IAccountDatabase {

    private Database database = DatabaseSingleton.getInstance();

    @Override
    public boolean addMoney(Double money) {
        try {
            double moneyToSet = getMoney() + money;
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "UPDATE account " +
                            "SET money=?;");
            prepStmt.setDouble(1, moneyToSet);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while adding money to account.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean modifyMoney(Double money) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "UPDATE account " +
                            "SET money=?;");
            prepStmt.setDouble(1, money);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while modifying money (Account table).");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean subMoney(Double money) {
        if (getMoney() < money)
            return false;
        try {
            double moneyToSet = getMoney() - money;
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "UPDATE account " +
                            "SET money=?;");
            prepStmt.setDouble(1, moneyToSet);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while subtracting money from account.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Double getMoney() {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM account");
            if (result.next()) {
                double money = result.getDouble("money");
                result.close();
                stat.close();
                ;
                database.closeConnection();
                return money;
            }
            result.close();
            stat.close();
            ;
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting money from account.");
            e.printStackTrace();
            return null;
        }
    }
}
