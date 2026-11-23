/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import contratos.Colisionable;


 // Proyectil disparado por las naves enemigas hacia abajo.

public class ProyectilEnemigo extends Proyectil {

    private final Color color;
    
    // Crea un proyectil rojojo de velocidad 5

    public ProyectilEnemigo(int x, int y) {
        this(x, y, Color.RED, 5);
    }
    
    // Crea un proyectil con color y velocidad personalziados

    public ProyectilEnemigo(int x, int y, Color color) {
        this(x, y, color, 5);
    }

    public ProyectilEnemigo(int x, int y, Color color, double velocidad) {
        super(x, y, 5, 14, 1, velocidad, 1);
        this.color = color;
    }
    
    // Dibuja el proyectil como un rectangulo con el color que indicamos

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(color);
        g.fillRect(getX(), getY(), getAncho(), getAlto());
    }
    
    // Doble despacho: avisa a Colisiobale otro que choco con un proyectil

    @Override
    public void alColisionar(Colisionable otro) {
        otro.notificarColisionA(this);
    }
    
    // Doble despacho: Le informa al receptor que choco un este proyectil

    @Override
    public void notificarColisionA(EntidadJuego receptor) {
        receptor.chocarConProyectilEnemigo(this);
    }
    
    // Al impactar al jugador, el proyectil desparece

    @Override
    public void chocarConJugador(Jugador jugador) {
        destruir();
    }
    
    // Al impactar una barrera, el proyectil desparece

    @Override
    public void chocarConBarrera(Barrera barrera) {
        destruir();
    }
}

