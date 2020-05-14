package pl.lodz.p.ftims.database.communicationinterface;

import pl.lodz.p.ftims.model.finance.Model.Cashflow;

import java.util.List;

public interface ICashflowDatabase {
    boolean addCashflow(Cashflow cashflow);

    boolean modifyCashflow(Cashflow cashflow);

    boolean removeCashflow(int id);

    List<Cashflow> getCashflows();

    Cashflow getCashflow(int id);
}
