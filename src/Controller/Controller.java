/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Mark Allen
 */
public class Controller implements ActionListener {
    
    private int BUTTONSIZE = 40;
    private int broadSize = 8;
    private Component[] listComponents;
    private boolean isStarted = false;
    
    private void setProperty(int i, int j, JButton btn, int state) {
        btn.setName("btn"+ i + "" + j);
        btn.setPreferredSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
        if (state == 0) { // is empty
            btn.setBackground(new Color(168, 168, 168, 1));
            btn.setForeground(Color.GRAY);
        } else if (state == 1) { // is black
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.BLACK);
        } else if (state == 2) { // is white
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.WHITE);
        }
        btn.addActionListener(this);
        btn.setVisible(true);
    }
    
    public void initialButton(JPanel myPanel) {
        //remove all Component in this panel
        myPanel.removeAll();
        //set layout to grid
        myPanel.setLayout(new GridLayout(broadSize, broadSize));
        JButton btn;
        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                btn = new JButton();
                myPanel.add(btn);
                if (i==3 && j == 3) {
                    setProperty(i, j, btn, 2);
                } else
                if (i==3 && j == 4) {
                    setProperty(i, j, btn, 1);
                } else
                if (i==4 && j == 3) {
                    setProperty(i, j, btn, 1);
                } else
                if (i==4 && j == 4) {
                    setProperty(i, j, btn, 2);
                } else {
                    setProperty(i, j, btn, 0);
                }
            }
        }
        listComponents = myPanel.getComponents();
        myPanel.revalidate();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // fake started game
        isStarted = true;
        
        if (isStarted) {
            JButton btn = (JButton) e.getSource();
            // get index of button click
            int xClicked = Integer.parseInt(btn.getName().substring(btn.getName().length() - 2, btn.getName().length() - 1));
            int yClicked = Integer.parseInt(btn.getName().substring(btn.getName().length() - 1));
            System.out.println(xClicked + "-" + yClicked);
            // có hàm check xem người chơi có được đánh ở ô này không trả về true false
            // ví dụ tra ve boolean isValiable = true;
            boolean isValiable = true;
            if (isValiable) {
                // xét xem đó là người chơi "Client - Rival" hay "Server - you"
                // nếu client thì cho 1 tham số int là 2, còn là server thì là 1
                // ví dụ đây là client
                // xu ly o day
                int who = 2;
                if (who==2) {
                    btn.setBackground(Color.WHITE);
                } else if (who==1) {
                    btn.setBackground(Color.BLACK);
                }
            }
        }
    }
    
}