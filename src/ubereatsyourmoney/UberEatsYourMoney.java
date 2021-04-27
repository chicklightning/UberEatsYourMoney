/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
        
/**
 *
 * @author Gabbo
 */
public class UberEatsYourMoney {
    
    private static final String TOTALS_FILE_PATH = "./totals.txt";
    
    private static UberEatsTotals getUberEatsTotals(Gmail service) throws IOException {
        UberEatsFileHandler fileHandler = new UberEatsFileHandler(TOTALS_FILE_PATH);
        UberEatsTotals totals = fileHandler.fetchTotals();
        
        // Add new Uber Eats totals from email
        List<Message> uberEatsMessages = GmailHandler.getUberEatsMessages(service);
        double sum = GmailHandler.calculateTotalsFromEmails(uberEatsMessages);
        totals.AddToAllTotals(sum);
        return totals;
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        GUI gui = new GUI();
        
        // Build a new authorized API client service.
        Gmail service = GmailHandler.buildService();

        // Get new UberEats emails from the user's account and add to existing totals for display:
        UberEatsTotals totals = getUberEatsTotals(service);
    }
    
}
