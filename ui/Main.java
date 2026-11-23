/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Punto de entrada de Defensores de la Galaxia. 
public class Main {
    
    // inicia el codigo

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
              
                // si falla, se usa el aspecto por defecto de Swing
            }
            new VentanaPrincipal().setVisible(true);
        });
    }
}

