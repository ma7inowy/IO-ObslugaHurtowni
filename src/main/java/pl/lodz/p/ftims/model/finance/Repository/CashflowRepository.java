package pl.lodz.p.ftims.model.finance.Repository;

import pl.lodz.p.ftims.database.communicationinterface.ICashflowDatabase;
import pl.lodz.p.ftims.database.managers.CashflowDatabaseManager;
import pl.lodz.p.ftims.model.finance.Model.Cashflow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CashflowRepository {
    List<Cashflow> cashflows;
    private ICashflowDatabase database;

    public CashflowRepository() {
        this.cashflows = new ArrayList<>();
        database = new CashflowDatabaseManager();
    }

    public Cashflow getCashflow(String id) {
        return database.getCashflow(Integer.parseInt(id));
    }

    public List<Cashflow> getAllCashflows() {
        cashflows = database.getCashflows();
        return cashflows;
    }

    public List<Cashflow> getCashflowsBetweenDate(Calendar from, Calendar to) {
        getAllCashflows();
        List<Cashflow> tmp = new ArrayList<>();

        for (Cashflow t : cashflows)
            if (t.getDate().after(from) && t.getDate().before(to))
                tmp.add(t);

        return tmp;
    }

    public List<Cashflow> getMonthlyCashflow() {
        getAllCashflows();
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.MONTH, -1);
        return getCashflowsBetweenDate(tmp, Calendar.getInstance());
    }

    public List<Cashflow> getWeeklyCashflow() {
        getAllCashflows();
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.DAY_OF_MONTH, -7);
        return getCashflowsBetweenDate(tmp, Calendar.getInstance());
    }

    public List<Cashflow> getDailyCashflow() {
        getAllCashflows();
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.DAY_OF_MONTH, -1);
        return getCashflowsBetweenDate(tmp, Calendar.getInstance());
    }

    public void addCashflow(Cashflow cashflow) {
        cashflows.add(cashflow);
        database.addCashflow(cashflow);
    }
}
