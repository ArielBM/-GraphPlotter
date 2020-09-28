
package proyecto2;

import javax.swing.JOptionPane;

public class Proyecto2 {


    public static void main(String[] args) {
        
        try {
            
            new Main().show();
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
}
