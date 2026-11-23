/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


 // Ventana principal que administra las pantallas del videojuego mediante CardLayout.
 
public class VentanaPrincipal extends JFrame {

    private static final String TARJETA_MENU = "MENU";
    private static final String TARJETA_JUEGO = "JUEGO";
    private static final String TARJETA_GAME_OVER = "GAME_OVER";

    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private PanelMenu panelMenu;
    private PanelJuego panelJuego;
    private PanelGameOver panelGameOver;
    private int puntajeMaximo;
    
    // Creamos la ventana 800x6000, centrada y mostrado el menu

    public VentanaPrincipal() {
        super("Defensores de la Galaxia");
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(panelContenedor);
        mostrarMenu();
        pack();
        setLocationRelativeTo(null);
    }
    
    // Crea una partida nueva, mostrando el panel de juego y arrancando un bucle

    public void iniciarPartida() {
        detenerJuegoActual();
        panelContenedor.removeAll();
        panelJuego = new PanelJuego(this);
        panelContenedor.add(panelJuego, TARJETA_JUEGO);
        cardLayout.show(panelContenedor, TARJETA_JUEGO);
        pack();
        setLocationRelativeTo(null);
        panelJuego.iniciarBucle();
    }
    
    // Detiene el juego en curso y muestra el menu principal con el puntaje maximo

    public void mostrarMenu() {
        detenerJuegoActual();
        panelContenedor.removeAll();
        panelMenu = new PanelMenu(this);
        panelMenu.actualizarPuntajeMaximo(puntajeMaximo);
        panelContenedor.add(panelMenu, TARJETA_MENU);
        cardLayout.show(panelContenedor, TARJETA_MENU);
        pack();
        setLocationRelativeTo(null);
    }
    
    // Muestra la pantalla final con el resultado de la partida
    // muetra el puntaje obtenido
   

    public void mostrarGameOver(int puntajeFinal, boolean victoria) {
        detenerJuegoActual();
        actualizarPuntajeMaximo(puntajeFinal);
        panelContenedor.removeAll();
        panelGameOver = new PanelGameOver(this);
        panelGameOver.mostrarResultado(puntajeFinal, victoria);
        panelContenedor.add(panelGameOver, TARJETA_GAME_OVER);
        cardLayout.show(panelContenedor, TARJETA_GAME_OVER);
        pack();
        setLocationRelativeTo(null);
    }
    
    // Actualiza el puntaje maximo si el nuevo puntaja lo supera
    // comparamos el puntaje

    public void actualizarPuntajeMaximo(int puntaje) {
        if (puntaje > puntajeMaximo) {
            puntajeMaximo = puntaje;
        }
    }
    
    // Empeiza una partida nueva 

    public void reiniciarPartida() {
        iniciarPartida();
    }
    
    // retorna el mayor puntaje obtenido ene sta sesion

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }
    
    // Detienbe el temporizador del juego actual, si es que existe, para evitar que siga corriendo 

    private void detenerJuegoActual() {
        if (panelJuego != null) {
            panelJuego.detenerBucle();
            panelJuego = null;
        }
    }
}
