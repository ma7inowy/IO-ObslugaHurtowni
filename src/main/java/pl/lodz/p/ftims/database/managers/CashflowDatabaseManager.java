package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.ICashflowDatabase;
import pl.lodz.p.ftims.model.finance.Model.Cashflow;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CashflowDatabaseManager implements ICashflowDatabase {

    private Database database = DatabaseSingleton.getInstance();

    @Override
    public boolean addCashflow(Cashflow cashflow) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "INSERT INTO cashflows (date, amount, name) VALUES (?, ?, ?);");
            prepStmt.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cashflow.getDate().getTime()));
            prepStmt.setDouble(2, cashflow.getAmount());
            prepStmt.setString(3, cashflow.getName());
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while adding cashflow to database: " + cashflow.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyCashflow(Cashflow cashflow) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "update cashflows " +
                            "set date=?, amount=?, name=?" +
                            "where cashflowID=?;");
            prepStmt.setDate(1, Date.valueOf(cashflow.getDate().toString()));
            prepStmt.setDouble(2, cashflow.getAmount());
            prepStmt.setString(3, cashflow.getName());
            prepStmt.setInt(4, Integer.parseInt(cashflow.getId()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while modifying cashflows: " + cashflow.toString());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeCashflow(int id) {
        try {
            if (getCashflow(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("delete FROM cashflows WHERE cashflowID=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing cashflow with id=" + id);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Cashflow> getCashflows() {
        List<Cashflow> cashflows = new ArrayList<>(1);
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM cashflows");
            while (result.next()) {
                int id = result.getInt("cashflowID");
                String date = result.getString("date");
                double amount = result.getDouble("amount");
                String name = result.getString("name");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
                cashflows.add(new Cashflow(id, calendar, amount, name));
            }
            result.close();
            stat.close();
            database.closeConnection();
        } catch (SQLException | ParseException e) {
            System.err.println("Error while getting cashflows from database");
            e.printStackTrace();
            return null;
        }
        return cashflows;
    }

    @Override
    public Cashflow getCashflow(int id) {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM cashflows where cashflowID=" + id);
            if (result.next()) {
                String date = result.getString("date");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
                double amount = result.getDouble("amount");
                String name = result.getString("name");
                result.close();
                stat.close();
                database.closeConnection();
                return new Cashflow(id, calendar, amount, name);
            }
            result.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException | ParseException e) {
            System.err.println("Error while getting cashflow with cashflowID=" + id);
            e.printStackTrace();
            return null;
        }
    }
}
