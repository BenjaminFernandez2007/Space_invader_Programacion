/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


// Pantalla final que muestra victoria o derrota y permite volver a jugar

public class PanelGameOver extends JPanel {

    private JLabel lblResultado;
    private JLabel lblPuntajeFinal;
    private JButton btnReiniciar;
    private JButton btnMenu;
    private VentanaPrincipal ventana;
    
    // Construimos la ventana final

    public PanelGameOver(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(BorderFactory.createEmptyBorder(165, 40, 70, 40));

        lblResultado = new JLabel("FIN DE LA PARTIDA");
        lblResultado.setFont(new Font("Monospaced", Font.BOLD, 34));
        lblResultado.setForeground(new Color(255, 90, 90));
        lblResultado.setAlignmentX(CENTER_ALIGNMENT);

        lblPuntajeFinal = new JLabel("PUNTAJE FINAL: 0");
        lblPuntajeFinal.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblPuntajeFinal.setForeground(Color.YELLOW);
        lblPuntajeFinal.setAlignmentX(CENTER_ALIGNMENT);

        btnReiniciar = crearBoton("VOLVER A JUGAR");
        btnReiniciar.addActionListener(e -> this.ventana.reiniciarPartida());

        btnMenu = crearBoton("VOLVER AL MENÚ");
        btnMenu.addActionListener(e -> this.ventana.mostrarMenu());

        contenido.add(lblResultado);
        contenido.add(Box.createVerticalStrut(35));
        contenido.add(lblPuntajeFinal);
        contenido.add(Box.createVerticalStrut(65));
        contenido.add(btnReiniciar);
        contenido.add(Box.createVerticalStrut(16));
        contenido.add(btnMenu);
        add(contenido, BorderLayout.CENTER);
    }
    
    // Actualiza los textos segun como termino la partida

    public void mostrarResultado(int puntaje, boolean victoria) {
        lblResultado.setText(victoria ? "GALAXIA SALVADA!" : "FIN DE LA PARTIDA");
        lblResultado.setForeground(victoria ? new Color(80, 255, 160) : new Color(255, 90, 90));
        lblPuntajeFinal.setText("PUNTAJE FINAL: " + puntaje);
    }
    
    // Crea unn boton verde centrado

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Monospaced", Font.BOLD, 15));
        boton.setForeground(Color.BLACK);
        boton.setBackground(new Color(80, 255, 170));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
        boton.setAlignmentX(CENTER_ALIGNMENT);
        return boton;
    }
    
    // Dibuja el fondo: Degradado azul-rojizo y las estrellas

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(0, 0, new Color(5, 8, 30), 0, getHeight(), new Color(45, 5, 35)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(160, 190, 255));
        for (int i = 0; i < 90; i++) {
            int x = (i * 89 + 13) % Math.max(1, getWidth());
            int y = (i * 47 + 29) % Math.max(1, getHeight());
            g2.fillOval(x, y, 2, 2);
        }
        g2.dispose();
    }
}