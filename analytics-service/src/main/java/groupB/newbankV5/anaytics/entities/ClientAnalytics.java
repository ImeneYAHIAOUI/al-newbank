package groupB.newbankV5.anaytics.entities;

import java.math.BigDecimal;
import java.util.List;

public class ClientAnalytics {


    private BigDecimal monthlyBalance;

    private List<Income> income;

    private List<Expense> expense;

    public BigDecimal getMonthlyBalance() {
        return monthlyBalance;
    }

    public void setMonthlyBalance(BigDecimal monthlyBalance) {
        this.monthlyBalance = monthlyBalance;
    }

    public List<Income> getIncome() {
        return income;
    }

    public void setIncome(List<Income> income) {
        this.income = income;
    }

    public List<Expense> getExpense() {
        return expense;
    }

    public void setExpense(List<Expense> expense) {
        this.expense = expense;
    }
}
