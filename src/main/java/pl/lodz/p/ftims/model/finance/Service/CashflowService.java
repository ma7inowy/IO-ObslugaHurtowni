package pl.lodz.p.ftims.model.finance.Service;

import pl.lodz.p.ftims.model.finance.Model.Cashflow;
import pl.lodz.p.ftims.model.finance.Repository.CashflowRepository;

import java.util.Calendar;
import java.util.List;

public class CashflowService {
    private CashflowRepository cashflowRepository;

    public CashflowService(CashflowRepository cashflowRepository) {
        this.cashflowRepository = cashflowRepository;
    }

    public Cashflow getCashflow(String id) {
        return this.cashflowRepository.getCashflow(id);
    }

    public List<Cashflow> getAllCashflows() {
        return this.cashflowRepository.getAllCashflows();
    }

    public List<Cashflow> getCashflowsBetweenDate(Calendar from, Calendar to) {
        return this.cashflowRepository.getCashflowsBetweenDate(from, to);
    }

    public List<Cashflow> getMonthlyCashflow() {
        return this.cashflowRepository.getMonthlyCashflow();
    }

    public List<Cashflow> getWeeklyCashflow() {
        return this.cashflowRepository.getWeeklyCashflow();
    }

    public List<Cashflow> getDailyCashflow() {
        return this.cashflowRepository.getDailyCashflow();
    }

    public void addCashflow(Cashflow cashflow) {
        this.cashflowRepository.addCashflow(cashflow);
    }
}
