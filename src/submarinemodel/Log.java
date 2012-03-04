/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package submarinemodel;

import javax.swing.JTextArea;

/**
 *
 * @author Martin Novak
 */
public class Log {

    boolean toConsole = false;
   // boolean toFile = false;
    JTextArea textArea = null;

    public Log() {
    }

    public void logToConsole(boolean value){
        toConsole = value;
    }
/*
    public void logToFile(boolean value){
        toFile = value;
    }
 * 
 */

    public void logToTextArea(JTextArea tArea) {
        textArea = tArea;
    }

    
    public void write(String message) {
        if(toConsole) System.out.println(message);
        if(textArea != null) textArea.setText(message+"\n"+textArea.getText());
    }
}
