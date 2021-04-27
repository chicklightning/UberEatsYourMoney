/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubereatsyourmoney;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Kevin
 */
public class GUI implements ActionListener{
    
    private JFrame frame;
    private ResizeLabelFont total;
    private JPanel top;
    private JPanel bottom;
    private JButton weekly;
    private JButton monthly;
    private JButton ytd;
    private Font buttonfont = new Font("default", Font.PLAIN,24);
    private Font selectedfont = new Font("default", Font.BOLD, 24);
    private UberEatsTotals displaytotals;
    private String state = "weekly";
    private static final String TOTALS_FILE_PATH = "./totals.txt";
    
       
    public GUI() throws IOException {
        UberEatsFileHandler fh = new UberEatsFileHandler(TOTALS_FILE_PATH);      
        displaytotals = fh.fetchTotals();
        
        frame = new JFrame();                 
        top = new JPanel();       
        top.setLayout(new BorderLayout());
        
        //CALL GET DOLLAR FUNC (WEEKLY)
        total = new ResizeLabelFont("$"+displaytotals.WeeklyTotal);
        top.add(total);
        
        //initialize button panel and buttons
        Dimension buttondimension = new Dimension(150,50);       
        bottom = new JPanel();
        weekly = new JButton("Weekly");
        weekly.setPreferredSize(buttondimension);
        weekly.addActionListener(this);
        weekly.setFont(selectedfont);
        monthly = new JButton("Monthly");
        monthly.addActionListener(this);
        monthly.setPreferredSize(buttondimension);
        monthly.setFont(buttonfont);
        ytd = new JButton("YTD");
        ytd.addActionListener(this);
        ytd.setPreferredSize(buttondimension);
        ytd.setFont(buttonfont);
        bottom.add(weekly);
        bottom.add(monthly);
        bottom.add(ytd);
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(top, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.PAGE_END);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("UEYM");
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
        
        
    }
   
    @Override
    public void actionPerformed(ActionEvent e){
        //highlight button, change total
        if(e.getSource() == weekly){
            state = "weekly";
            resetButtonFonts();
            total.setText("$"+displaytotals.WeeklyTotal);
            total.adaptLabelFont(total);
            weekly.setFont(selectedfont);
        }
        if(e.getSource() == monthly){
            state = "monthly";
            resetButtonFonts();
            total.setText("$"+displaytotals.MonthlyTotal);
            total.adaptLabelFont(total);
            monthly.setFont(selectedfont);
        }
        if(e.getSource() == ytd){
            state = "ytd";
            resetButtonFonts();
            total.setText("$"+displaytotals.YearToDateTotal);
            total.adaptLabelFont(total);
            ytd.setFont(selectedfont);
        }
    }
    
    //resets buttons to unselected mode
    public void resetButtonFonts(){
        weekly.setFont(buttonfont);
        monthly.setFont(buttonfont);
        ytd.setFont(buttonfont);
    }
    public void updateUI(UberEatsTotals newtotals){
        displaytotals = newtotals;
        switch(state){
            case "weekly":
                total.setText("$"+displaytotals.WeeklyTotal);
                total.adaptLabelFont(total);
                
            case "monthly":
                total.setText("$"+displaytotals.MonthlyTotal);
                total.adaptLabelFont(total);
                
            case "ytd":
                total.setText("$"+displaytotals.YearToDateTotal);
                total.adaptLabelFont(total);
        }
    }
    
    
}
