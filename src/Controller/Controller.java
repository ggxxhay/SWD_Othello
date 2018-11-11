/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    // number of each row
    private int broadSize = 8;
    // store all components in panel
    private Component[] listComponents;
    private boolean isStarted = false;
    private int playerTurn = 1;
    private Integer[][] buttonState = new Integer[broadSize][broadSize];
    private int invalidMoveCount = 0;
    private int blackCount = 2;
    private int whiteCount = 2;
    
    private void setProperty(int i, int j, JButton btn, int state) {
        btn.setName("btn"+ i + "" + j);
        btn.setPreferredSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
        switch (state) {
            case -1:
                // is white = -1
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.WHITE);
                break;
            case 0:
                // is empty = 0
                btn.setBackground(new Color(168, 168, 168, 1));
                btn.setForeground(new Color(168, 168, 168, 1));
                break;
            case 1:
                // is black = 1
                btn.setBackground(Color.BLACK);
                btn.setForeground(Color.BLACK);
                break;
            default:
                break;
        }
        btn.addActionListener(this);
        btn.setVisible(true);
    }
    
    
    public void initialChangeTurn(JLabel label) {
        if (playerTurn == 1) {
            label.setText("Your Turn");
        } else {
            label.setText("Rival's Turn");
        }
    }
    
    public void initialPlayerPoint(JLabel label, JLabel label1) {
        label.setText(blackCount+"");
        label1.setText(whiteCount+"");
    }
    
    private void checkAllValidMove() {
        int count = 0;
        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                if (isValidMove(playerTurn, i, j)) {
                    buttonState[i][j] = 2;
                    count++;
                }
            }
        }
    }
    
    private boolean isValidMove(int color, int row, int col) {
        if (buttonState[row][col] != 0) {
            return false;
        }
        
        int dr, dc;
        for (dr = -1; dr <= 1; dr++) {            
            for (dc = -1; dc <= 1; dc++) {
                if (!(dr == 0 && dc == 0) && IsOutflanking(color, row, col, dr, dc))
                    return true;
            }
        }
        return false;
    }
    
    private boolean IsOutflanking(int color, int row, int col, int dr, int dc)
    {
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < broadSize && c >= 0 && c < broadSize && buttonState[r][c] == -color)
        {
            r += dr;
            c += dc;
        }
        if (r < 0 || r >= broadSize || c < 0 || c >= broadSize || (r - dr == row && c - dc == col) || buttonState[r][c] != color)
            return false;
        return true;
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
                    setProperty(i, j, btn, -1);
                    buttonState[i][j] = -1;
                    btn.removeActionListener(this);
                } else
                if (i==3 && j == 4) {
                    setProperty(i, j, btn, 1);
                    buttonState[i][j] = 1;
                    btn.removeActionListener(this);
                } else
                if (i==4 && j == 3) {
                    setProperty(i, j, btn, 1);
                    buttonState[i][j] = 1;
                    btn.removeActionListener(this);
                } else
                if (i==4 && j == 4) {
                    setProperty(i, j, btn, -1);
                    buttonState[i][j] = -1;
                    btn.removeActionListener(this);
                } else {
                    setProperty(i, j, btn, 0);
                    buttonState[i][j] = 0;
                }
            }
        }
        
        // check all valid move
        checkAllValidMove();
        
        listComponents = myPanel.getComponents();
        myPanel.revalidate();
        
        // remove action listener of invalid button
        // can enable button = false because disable button's background not working
        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                if (buttonState[i][j] == 0) {
                    JButton b = new JButton();
                    b = (JButton) listComponents[i * 8 + j];
                    b.removeActionListener(this);
                }
            }
        }
    }
    
//    void UpdateButtons(boolean isMoved)
//    {
//        if (isMoved)
//        {
//            // Update buttons' states after moving.
//            for (int i = 0; i < broadSize; i++)
//            {
//                for (int j = 0; j < broadSize; j++)
//                {
//                    switch (buttonState[i][j])
//                    {
//                        case 2:
//                            buttonState[i][j] = 0;
//                            break;
//                    }
//                }
//            }
//        }
//        else
//        {
//            // Enable canMoved buttons.
//            for (int i = 0; i < broadSize; i++)
//            {
//                for (int j = 0; j < broadSize; j++)
//                {
//                    if (buttonState[i][j] == 2)
//                    {
////                        btn.addActionListener(this);
//                    }
//                    else
//                    {
////                        btn.removeActionListener(this);
//                    }
//                }
//            }
//        }
//    }
//
//    public void MakeMove(int color, int row, int col)
//    {
//        // Set array's value
//        buttonState[row][col] = color;
//        if (playerTurn == 1)
//        {
//            blackCount++;
//        }
//        else
//        {
//            whiteCount++;
//        }
//
//        // Flip outflanking position
//        int dr, dc;
//        int r, c;
//        for (dr = -1; dr <= 1; dr++)
//        {
//            for (dc = -1; dc <= 1; dc++)
//            {
//                if (!(dr == 0 && dc == 0) && IsOutflanking(color, row, col, dr, dc))
//                {
//                    r = row + dr;
//                    c = col + dc;
//                    while (buttonState[r][c] == -color)
//                    {
//                        buttonState[r][c] = color;
//                        blackCount += playerTurn;
//                        whiteCount -= playerTurn;
//                        r += dr;
//                        c += dc;
//                    }
//                }
//            }
//        }
//
////        UpdateButtons(true);
//    }
    
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
        }
    }
    
}