/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package qltt;

import javax.swing.SwingUtilities;
import qltt.GUI.HomeGUI;

/**
 *
 * @author admin
 */
public class QLTT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> new HomeGUI().setVisible(true));
    }
    
}
