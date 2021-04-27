/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

import java.math.BigDecimal;

/**
 *
 * @author Gab
 */
public class UberEatsTotals {
    
    public BigDecimal WeeklyTotal;
    public BigDecimal MonthlyTotal;
    public BigDecimal YearToDateTotal;
    
    public UberEatsTotals(BigDecimal weeklyTotal, BigDecimal monthlyTotal, BigDecimal ytdTotal) {
        this.WeeklyTotal = weeklyTotal;
        this.MonthlyTotal = monthlyTotal;
        this.YearToDateTotal = ytdTotal;
    }

    public void AddToAllTotals(BigDecimal amount) {
        this.WeeklyTotal = this.WeeklyTotal.add(amount);
        this.MonthlyTotal = this.MonthlyTotal.add(amount);
        this.YearToDateTotal = this.YearToDateTotal.add(amount);
    }
    
    public void resetWeeklyTotal() {
        this.WeeklyTotal = BigDecimal.ZERO;
    }
    
    public void resetMonthlyTotal() {
        this.MonthlyTotal = BigDecimal.ZERO;
    }
    
    public void resetYearToDateTotal() {
        this.YearToDateTotal = BigDecimal.ZERO;
    }
}
