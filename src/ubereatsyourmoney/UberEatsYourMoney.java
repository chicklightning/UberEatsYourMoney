/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

        
/**
 *
 * @author Gabbo
 */
public class UberEatsYourMoney {
    
    private static final String TOTALS_FILE_PATH = "./totals.txt";

    public static UberEatsTotals getUberEatsTotals(GmailHandler handler) throws IOException {
        UberEatsFileHandler fileHandler = new UberEatsFileHandler(TOTALS_FILE_PATH);
        UberEatsTotals totals = fileHandler.fetchTotals();
        
        Duration duration = Duration.between(fileHandler.LastModified, LocalDateTime.now());
        if(duration.toDays() == 0){
            return totals;
        }       
        // Add new Uber Eats totals from email
        List<Message> uberEatsMessages = handler.getUberEatsMessages(duration.toDays());      
        BigDecimal sum = handler.calculateTotalsFromEmails(uberEatsMessages);
        totals.AddToAllTotals(sum);
        fileHandler.saveTotalsToFile(totals);
        return totals;
    }
    
    public static void main(String... args) throws IOException, GeneralSecurityException {
        
        // Build a new authorized API client service.
        Gmail service = GmailHandler.buildService();
        GmailHandler gmailHandler = new GmailHandler(service);
        
        GUI gui = new GUI();
        TimerNightlyUpdate.startTask(gui, gmailHandler);
        
    }
    
}
