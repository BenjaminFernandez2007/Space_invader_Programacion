/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import logica.GestorNiveles;
import logica.Partida;
import modelo.EntidadJuego;


 // Panel donde se ejecuta y dibuja la partida a 60 FPS

public class PanelJuego extends JPanel {

    private final Partida partida;
    private final GestorNiveles gestorNivel;
    private final Timer temporizador;
    private final VentanaPrincipal ventana;

    private boolean pausado;
    private boolean izquierda;
    private boolean derecha;
    private boolean disparando;
    private boolean teclaPausa;
    private boolean cambioNivelEnCurso;
    
    // Consutrye el panl, crea la partida y carga el nivel 1

    public PanelJuego(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.partida = new Partida();
        this.gestorNivel = new GestorNiveles(partida);
        this.gestorNivel.cargarNivel(1);

        setPreferredSize(new Dimension(EntidadJuego.ANCHO_PANTALLA, EntidadJuego.ALTO_PANTALLA));
        setBackground(Color.BLACK);
        setFocusable(true);
        configurarTeclado();

        // 16 ms por fotograma equivale a 60 FPS.
        temporizador = new Timer(16, e -> actualizarJuego());
    }
    
    // registra KeyaDapter y clase anonima que sirve para guardar que teclas esta presionadas
    // izquirda y derecha para moverse, espacio para disaprar, P para pausar y ESQ para volver al menu

    private void configurarTeclado() {
        // Clase anónima KeyAdapter para capturar las teclas del jugador.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> izquierda = true;
                    case KeyEvent.VK_RIGHT -> derecha = true;
                    case KeyEvent.VK_SPACE -> disparando = true;
                    case KeyEvent.VK_P -> {
                        if (!teclaPausa) {
                            teclaPausa = true;
                            pausarReanudar();
                        }
                    }
                    case KeyEvent.VK_ESCAPE -> {
                        ventana.actualizarPuntajeMaximo(partida.getPuntaje());
                        detenerBucle();
                        ventana.mostrarMenu();
                    }
                    default -> { }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) izquierda = false;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) derecha = false;
                if (e.getKeyCode() == KeyEvent.VK_SPACE) disparando = false;
                if (e.getKeyCode() == KeyEvent.VK_P) teclaPausa = false;
            }
        });
    }
    
    // Esto Pide el foco del teclado y arranca el temporizador del juego

    public void iniciarBucle() {
        requestFocusInWindow();
        temporizador.start();
    }
    
    // Alterna entre pausa y juego ym redibuja el panel

    public void pausarReanudar() {
        pausado = !pausado;
        if (pausado) {
            partida.pausar();
        } else {
            partida.reanudar();
        }
        repaint();
    }
    
    // Detiene el temporizador del jueo

    public void detenerBucle() {
        temporizador.stop();
    }
    
    // Esto es un fotograma del bucle que aplica las teclas presionadas
    // actualiza la partida, pasa de nivel que le corresponde
    // redibuja y si la aprtida termino, muestra la pantalla final

    private void actualizarJuego() {
        if (!pausado && !partida.isTerminada()) {
            if (izquierda) partida.getJugador().moverIzquierda();
            if (derecha) partida.getJugador().moverDerecha();
            if (disparando) partida.dispararJugador();

            partida.actualizar();

            // Cuando se elimina una oleada, el gestor carga el siguiente nivel o el jefe.
            if (partida.isNivelCompletado() && !cambioNivelEnCurso) {
                cambioNivelEnCurso = true;
                gestorNivel.siguienteNivel();
                cambioNivelEnCurso = false;
            }
        }

        repaint();
        if (partida.isTerminada()) {
            detenerBucle();
            ventana.mostrarGameOver(partida.getPuntaje(), partida.isVictoria());
        }
    }
    
    // Dibuja todas las entidades activas, el HUD, si esta en pausa, la capa se pausa

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibuja todas las entidades desde la colección polimórfica de Partida.
        for (EntidadJuego entidad : partida.getEntidades()) {
            if (entidad.isActiva()) {
                entidad.dibujar(g2);
            }
        }

        dibujarHud(g2);
        if (pausado) {
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRect(0, 0, getWidth(), getHeight());
            dibujarTextoCentrado(g2, "PAUSA", 42, Color.YELLOW);
            dibujarTextoCentrado(g2, "Presiona P para continuar", 16,
                    Color.WHITE, getHeight() / 2 + 38);
        }
        g2.dispose();
    }
    
    // Esto Dibuja la barra superior con puntaje, vidas y nivel

    private void dibujarHud(Graphics2D g2) {
        g2.setColor(new Color(8, 12, 35));
        g2.fillRect(0, 0, getWidth(), 38);
        g2.setFont(new Font("Monospaced", Font.BOLD, 15));
        g2.setColor(Color.WHITE);
        g2.drawString("PUNTAJE: " + partida.getPuntaje(), 18, 25);
        g2.drawString("VIDAS: " + partida.getJugador().getVidas(), 330, 25);
        g2.drawString("NIVEL: " + partida.getNivel(), 650, 25);
    }
    
    // Esto Dibuja un texto centrado horizontalmente y en el medio vertical del panel

    private void dibujarTextoCentrado(Graphics2D g2, String texto, int tamanio, Color color) {
        dibujarTextoCentrado(g2, texto, tamanio, color, getHeight() / 2);
    }
    
    // Esto dibuja un texto centrado horizontalmente en la cordenada vertical indicada

    private void dibujarTextoCentrado(Graphics2D g2, String texto, int tamanio,
            Color color, int y) {
        g2.setFont(new Font("Monospaced", Font.BOLD, tamanio));
        g2.setColor(color);
        int x = (getWidth() - g2.getFontMetrics().stringWidth(texto)) / 2;
        g2.drawString(texto, x, y);
    }
}

