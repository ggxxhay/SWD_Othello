/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othellogame;

import UI.PlayGround;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Mark Allen
 */
public class Controller implements ActionListener {

    public final int BUTTONSIZE = 40;
    DataOutputStream ou;
    // number of each row
    public final int broadSize = 8;
    // store all components in panel
    public Component[] listComponents;
    public boolean isStarted = false;
    public int playerTurn;

    public final Integer[][] buttonState = new Integer[broadSize][broadSize];
    public final int invalidMoveCount = 0;
    public int blackCount = 2;
    public int whiteCount = 2;

    public Controller(int playerTurn) {
        this.playerTurn = playerTurn;
    }
 

    public void setProperty(int i, int j, JButton btn, int state) {
        btn.setName("btn" + i + "" + j);
        btn.setPreferredSize(new Dimension(BUTTONSIZE, BUTTONSIZE));
        switch (state) {
            case -1:
                // is white = -1
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.WHITE);
                break;
            case 0:
                // is empty = 0
                btn.setBackground(Color.GRAY);
                btn.setForeground(Color.GRAY);
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

    public void checkValidMove() {
        System.out.println("aa-" + playerTurn);
        int count = 0;
        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                
                if (isValidMove(playerTurn, i, j)) {
                    buttonState[i][j] = 2;
                    count++;
                }
            }
        }
        
        int x = 0;
        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                System.out.print(buttonState[i][j] + "  ");
                x++;
                if (x == broadSize) {
                    System.out.println();
                    x = 0;
                }
            }
        }
        System.out.println("count" + count);
    }

    public boolean isValidMove(int color, int row, int col) {
        if (buttonState[row][col] != 0) {
            return false;
        }

        int dr, dc;
        for (dr = -1; dr <= 1; dr++) {
            for (dc = -1; dc <= 1; dc++) {
                if (!(dr == 0 && dc == 0) && IsOutflanking(color, row, col, dr, dc)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean IsOutflanking(int playerTurn, int row, int col, int dr, int dc) {
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < broadSize && c >= 0 && c < broadSize && buttonState[r][c] == -playerTurn) {
            r += dr;
            c += dc;
        }
        if (r < 0 || r >= broadSize || c < 0 || c >= broadSize || (r - dr == row && c - dc == col) || buttonState[r][c] != playerTurn) {
            return false;
        }
        return true;
    }

    public void initialButton(JPanel myPanel) {
        //remove all Component in this panel
        myPanel.removeAll();
        //set layout to grid
        myPanel.setLayout(new GridLayout(broadSize, broadSize));
        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                JButton btn = new JButton();
                myPanel.add(btn);
                if (i == 3 && j == 3) {
                    setProperty(i, j, btn, -1);
                    buttonState[i][j] = -1;
                } else if (i == 3 && j == 4) {
                    setProperty(i, j, btn, 1);
                    buttonState[i][j] = 1;
                } else if (i == 4 && j == 3) {
                    setProperty(i, j, btn, 1);
                    buttonState[i][j] = 1;
                } else if (i == 4 && j == 4) {
                    setProperty(i, j, btn, -1);
                    buttonState[i][j] = -1;
                } else {
                    setProperty(i, j, btn, 0);
                    buttonState[i][j] = 0;
                }
            }
        }

        // check all valid move
        if (playerTurn == 1) {
            checkValidMove();
        }

        listComponents = myPanel.getComponents();
        myPanel.revalidate();
    }

    public void updateButton(boolean isMoved) {
        System.out.println("aa-" + playerTurn);
        if (isMoved) {
            for (int i = 0; i < broadSize; i++) {
                for (int j = 0; j < broadSize; j++) {
                    if (buttonState[i][j] == 2) {
                        buttonState[i][j] = 0;
                    }
                }
            }
        }

        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                JButton b = new JButton();
                b = (JButton) listComponents[i * 8 + j];
                if (buttonState[i][j] == 1) {
                    b.setBackground(Color.BLACK);
                    b.setForeground(Color.BLACK);
                } else if (buttonState[i][j] == -1) {
                    b.setBackground(Color.WHITE);
                    b.setForeground(Color.WHITE);
                }
            }
        }
    }

    public void makeMove(String a, int row, int col) {
        // set piece that player clicked
        System.out.println(a);
        if (buttonState[row][col] != 2) {
            return;
        }

        buttonState[row][col] = playerTurn;
        System.out.println(a + ": " + row + "-" + col);
        if (playerTurn == 1) {
            whiteCount++;
        } else {
            blackCount++;
        }

        int dr, dc;
        int r, c;
        for (dr = -1; dr <= 1; dr++) {
            for (dc = -1; dc <= 1; dc++) {
                if (!(dr == 0 && dc == 0) && IsOutflanking(playerTurn, row, col, dr, dc)) {
                    r = row + dr;
                    c = col + dc;
                    while (buttonState[r][c] == -playerTurn) {
                        buttonState[r][c] = playerTurn;
                        blackCount -= playerTurn;
                        whiteCount += playerTurn;
                        r += dr;
                        c += dc;
                    }
                }
            }
        }
        System.out.println(blackCount + "-" + whiteCount);
        updateButton(true);
        StaticVariables.movePosition = row + "" + col;
        System.out.println(StaticVariables.movePosition + "move pos");
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
//            System.out.println(xClicked + "-" + yClicked);
            makeMove("", xClicked, yClicked);
            
            System.out.println(StaticVariables.movePosition + "move");
            if (StaticVariables.movePosition != null) {
                try {
                    ou.writeBytes(StaticVariables.movePosition + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Reset the position.
                StaticVariables.movePosition = "";
            }
        }
    }
    public boolean checkWin() {
        int count = 0;
        for (int i = 0; i < broadSize; i++) {
            for (int j = 0; j < broadSize; j++) {
                if (buttonState[i][j] == 0) {
                    count++;
                }
            }
        }
        if (count > 0) {
            return false;
        }
        return false;
    }

    public void playAgain() {
        
    }
    
    public void gameOver() {
        String message = "";
        if (checkWin()) {
            if (blackCount - whiteCount > 0) {
                message = "Black win";
            } else if (blackCount - whiteCount < 0) {
                message = "White win";
            } else {
                message = "Hoa roi";
            }
            int result = JOptionPane.showConfirmDialog(null, message + "\nB: "+ blackCount + ", W: " + whiteCount, "Game done",
            JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }
        }
    }
}
