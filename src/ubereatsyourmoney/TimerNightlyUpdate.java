/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

import com.google.api.services.gmail.Gmail;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */
public class TimerNightlyUpdate extends TimerTask{
    Gmail service;
    GUI gui;
    public TimerNightlyUpdate(GUI gui, Gmail service){
        this.service = service;
        this.gui = gui;
    }
    @Override
    public void run() {
        try {
            // Get new UberEats emails from the user's account and add to existing totals for display:
            UberEatsTotals totals = UberEatsYourMoney.getUberEatsTotals(service);
            gui.updateUI(totals);
        } catch (IOException ex) {
            Logger.getLogger(TimerNightlyUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    private static Date getMidnight(){
        
        Calendar calEnd = new GregorianCalendar();
        calEnd.setTime(new Date());
        calEnd.set(Calendar.DAY_OF_YEAR, calEnd.get(Calendar.DAY_OF_YEAR)+1);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);
        Date midnightTonight = calEnd.getTime();
        return midnightTonight;
      }
    //call this method from your servlet init method
    public static void startTask(GUI gui, Gmail service){
        TimerNightlyUpdate task = new TimerNightlyUpdate(gui, service);
        Timer timer = new Timer();  
        timer.schedule(task,getMidnight(),1000*60*60*24); //ms in a day
    }
}
