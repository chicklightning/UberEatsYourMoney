/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

/**
 *
 * @author Gab
 */
public class UberEatsTotals {
    
    public double WeeklyTotal;
    public double MonthlyTotal;
    public double YearToDateTotal;
    
    public UberEatsTotals(double weeklyTotal, double monthlyTotal, double ytdTotal) {
        this.WeeklyTotal = weeklyTotal;
        this.MonthlyTotal = monthlyTotal;
        this.YearToDateTotal = ytdTotal;
    }

    public void AddToAllTotals(double amount) {
        this.WeeklyTotal += amount;
        this.MonthlyTotal += amount;
        this.YearToDateTotal += amount;
    }
    
    public void resetWeeklyTotal() {
        this.WeeklyTotal = 0;
    }
    
    public void resetMonthlyTotal() {
        this.MonthlyTotal = 0;
    }
    
    public void resetYearToDateTotal() {
        this.YearToDateTotal = 0;
    }
}
