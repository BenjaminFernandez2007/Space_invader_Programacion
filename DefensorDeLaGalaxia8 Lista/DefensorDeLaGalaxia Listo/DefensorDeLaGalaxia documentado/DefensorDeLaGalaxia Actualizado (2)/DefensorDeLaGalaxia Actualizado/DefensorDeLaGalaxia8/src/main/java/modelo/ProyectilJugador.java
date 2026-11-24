/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import contratos.Colisionable;


 // Proyectil disparado por la nave del jugador hacia arriba.

public class ProyectilJugador extends Proyectil {

    private final Color color;
    
    // Crea un proyectil cian

    public ProyectilJugador(int x, int y) {
        this(x, y, Color.CYAN);
    }

    // Constructor sobrecargado para personalizar el color del disparo
    // Crea un proyectil con el color indicado 
    
    public ProyectilJugador(int x, int y, Color color) {
        super(x, y, 4, 14, -1, 9, 1);
        this.color = color;
    }
    
    // Dibuja el proyectil como un rectangulo del color indicado

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(color);
        g.fillRect(getX(), getY(), getAncho(), getAlto());
    }
    
    // Doble depsacho: avisa a Colisionable otro que choco con un proyectil del jugador

    @Override
    public void alColisionar(Colisionable otro) {
        otro.notificarColisionA(this);
    }
    
    // Doble despacho: le iforma al receptor que choco con este proyectil

    @Override
    public void notificarColisionA(EntidadJuego receptor) {
        receptor.chocarConProyectilJugador(this);
    }
    
    // Al impactar un enemigo, el proyectil desaparece

    @Override
    public void chocarConEnemigo(modelo.enemigos.Enemigo enemigo) {
        destruir();
    }
    
    // Al impactar una barrera, el proyectil desparece

    @Override
    public void chocarConBarrera(Barrera barrera) {
        destruir();
    }
}
