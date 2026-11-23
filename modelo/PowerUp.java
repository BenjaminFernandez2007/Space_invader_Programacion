/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import contratos.Colisionable;
import contratos.EfectoPowerUp;


 // Mejora que cae al destruir un enemigo y otorga beneficios temporales o permanentes al jugador.
 
public class PowerUp extends EntidadJuego {

    private static final int VELOCIDAD_CAIDA = 3;
    private static final int DURACION_MEJORA = 360; // ~6 segundos a 60 FPS

    private final EfectoPowerUp efecto;
    private final int duracion;
    private final Color color;
    private final String etiqueta;
    
    // Constructor privado: se usa solo desde los metodos de fabrica

    private PowerUp(int x, int y, EfectoPowerUp efecto, int duracion, Color color, String etiqueta) {
        super(x, y, 24, 24);
        this.efecto = efecto;
        this.duracion = duracion;
        this.color = color;
        this.etiqueta = etiqueta;
    }
    // Mejorda de escudo color cian, etiqueta "E"
    
    // return mejora que activa el escudo por unos 6 segundos

    public static PowerUp escudo(int x, int y) {
        return new PowerUp(x, y, j -> j.activarEscudo(DURACION_MEJORA),
                DURACION_MEJORA, Color.CYAN, "E");
    }
    
    // Mejora de disparo doble, naraja, etiqueta "d)

    public static PowerUp disparoDoble(int x, int y) {
        return new PowerUp(x, y, j -> j.activarDisparoDoble(DURACION_MEJORA),
                DURACION_MEJORA, Color.ORANGE, "D");
    }
    
    // Fabrica de la mejora de vida extra, rosada, etiqueta "+1"
    
    // retorna la mejora que suma una vida al jugador

    public static PowerUp vidaExtra(int x, int y) {
        return new PowerUp(x, y, Jugador::ganarVida, DURACION_MEJORA, Color.PINK, "+1");
    }
    
    // Aplica el efecto de esta mejora al jugador
    
    /// Jugador jugador, que la recoge

    public void aplicar(Jugador jugador) {
        if (jugador != null && efecto != null) {
            efecto.aplicar(jugador);
        }
    }
    
    // Hace caer la mejora: se destruye si sale por la parte inferior de la pantalla

    @Override
    public void actualizar() {
        setY(getY() + VELOCIDAD_CAIDA);
        if (getY() > ALTO_PANTALLA) {
            destruir();
        }
    }
    
    // Dibuja un circulo de color con su etiqueta

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(color);
        g.fillOval(getX(), getY(), getAncho(), getAlto());
        g.setColor(Color.BLACK);
        g.drawString(etiqueta, getX() + 6, getY() + 16);
    }
    
    // Doble disparo: avisa que choco con una mejora

    @Override
    public void alColisionar(Colisionable otro) {
        otro.notificarColisionA(this);
    }
    
    // Doble despacho: avisa que choco con una mejora

    @Override
    public void notificarColisionA(EntidadJuego receptor) {
        receptor.chocarConPowerUp(this);
    }
    
    // Al tocar al jugador, aplica el efecto y desaparece

    @Override
    public void chocarConJugador(Jugador jugador) {
        aplicar(jugador);
        destruir();
    }
    // retorna la duracion del efecto en fotogramas

    public int getDuracion() {
        return duracion;
    }
}
