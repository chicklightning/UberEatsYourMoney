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
       
    public GUI() {
        
        frame = new JFrame();                 
        top = new JPanel();       
        top.setLayout(new BorderLayout());
        
        //CALL GET DOLLAR FUNC (WEEKLY)
        total = new ResizeLabelFont("$50.32");
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
            //CALL GET DOLLAR FUNC (WEEKLY)
            resetButtonFonts();
            total.setText("$90.32");
            total.adaptLabelFont(total);
            weekly.setFont(selectedfont);
        }
        if(e.getSource() == monthly){
            //CALL GET DOLLAR FUNC (MONTHLY)
            resetButtonFonts();
            total.setText("$900.32");
            total.adaptLabelFont(total);
            monthly.setFont(selectedfont);
        }
        if(e.getSource() == ytd){
            //CALL GET DOLLAR FUNC (YEARLY)
            resetButtonFonts();
            total.setText("$9000.32");
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
    
    
}
