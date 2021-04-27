/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author Gab
 */
public class UberEatsFileHandler {
    
    private String FilePath;
    public LocalDateTime LastModified;
    
    public UberEatsFileHandler(String filePath) {
        this.FilePath = filePath;
        
        Path path = Paths.get(this.FilePath);
        try {
            FileTime lastModififed = Files.getLastModifiedTime(path);
            this.LastModified = LocalDateTime.ofInstant(lastModififed.toInstant(), ZoneId.systemDefault());
        } catch (IOException ex) {
            System.out.println("Unable to retrieve last-modified time for provided file.");
            this.LastModified = LocalDateTime.now();
        }
    }
    
    public UberEatsTotals fetchTotals() throws IOException {
        double weekly = 0, monthly = 0, ytd = 0;
        
        File file = new File(this.FilePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        int loop = 0;
        while ((line = br.readLine()) != null) { // Hardcoded mess, oh well
            switch (loop) {
                case 0:
                    weekly = Double.parseDouble(line);
                    loop++;
                    break;
                case 1:
                    monthly = Double.parseDouble(line);
                    loop++;
                    break;
                case 2:
                    ytd = Double.parseDouble(line);
                    loop++;
                    break;
                default:
                    break;
            }
        }
        
        UberEatsTotals totals = new UberEatsTotals(weekly, monthly, ytd);
        br.close();
        
        // If new week, then reset weekly value to 0
        // If new month, then reset monthly value to 0
        // If new year, then reset year-to-date value to 0 (which will also happen when monthly reset occurs)
        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            totals.resetWeeklyTotal();
        }
        
        if (now.getDayOfMonth() == 1) {
            totals.resetMonthlyTotal();
        }
        
        if (now.getDayOfYear() == 1) {
            totals.resetYearToDateTotal();
        }
        
        return totals;
    }
    
    public void saveTotalsToFile(UberEatsTotals totals) throws IOException {
        String totalsAsString = totals.WeeklyTotal + "\n" + totals.MonthlyTotal + "\n" + totals.YearToDateTotal;
        File f = new File(this.FilePath);
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));

        bw.write(totalsAsString);
        
        bw.close();
    }
    
    public int getHoursSinceUpdate() {
        return (int) Duration.between(LocalDateTime.now(), this.LastModified).toHours();
    }
}
