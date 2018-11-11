/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othellogame;

/**
 *
 * @author ThangHQ
 */
public class StaticVariables {
    public static String movePosition;
    public static String message_surrender = "surrender";
    public static String message_quit = "quit";
    
    public static int[] ConvertMovePos(String movePos){
        try{
            int xPos = Integer.parseInt(movePos.charAt(0)+"");
            int yPos = Integer.parseInt(movePos.charAt(1)+"");
            return new int[]{xPos, yPos};
        }catch(NumberFormatException e){
            System.out.println(e.toString());
            return null;
        }
    }
}
