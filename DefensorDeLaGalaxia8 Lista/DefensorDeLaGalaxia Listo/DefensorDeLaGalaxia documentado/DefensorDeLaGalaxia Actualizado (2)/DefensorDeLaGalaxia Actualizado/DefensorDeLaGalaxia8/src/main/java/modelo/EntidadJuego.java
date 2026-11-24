/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import contratos.Colisionable;


 // Clase base abstracta para todas las entidades visibles y colisionables del juego
 
public abstract class EntidadJuego implements Colisionable {
    
    // Ancho de la pantalla en pixeles
    public static final int ANCHO_PANTALLA = 800;
    
    // alto de la pantalla en pixeles
    public static final int ALTO_PANTALLA = 600;

    private int x;
    private int y;
    private int ancho;
    private int alto;
    private boolean activa;

    protected EntidadJuego(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.activa = true;
    }

    // Actualiza el estado lógico de la entidad en cada fotograma
    public abstract void actualizar();

    // Dibuja la representación visual de la entidad en pantalla
    public abstract void dibujar(Graphics2D g);

    // Rectangulo de la colicion de la entidad segun su posicion y tamaño
    @Override
    public Rectangle getLimites() {
        return new Rectangle(x, y, ancho, alto);
    }
    
    // Marca la endtidad como inactiva, y la elimina al final de fotograma

    public void destruir() {
        this.activa = false;
    }

    public boolean isActiva() {
        return activa;
    }

    // Métodos de Double Dispatch para colisiones sin instanceof 
    
    // Reaccion al chococar con el jugador

    public void chocarConJugador(Jugador jugador) { }
    
    // Reaccion al chocar con un enemigo

    public void chocarConEnemigo(modelo.enemigos.Enemigo enemigo) { }
    
    // Reaccion al chocoar con un proyectil del jugador

    public void chocarConProyectilJugador(ProyectilJugador proyectil) { }
    
    // Reaccion al chocar con un proyectil enemigo

    public void chocarConProyectilEnemigo(ProyectilEnemigo proyectil) { }
    
    // Reaccion al chocar con una mejora

    public void chocarConPowerUp(PowerUp powerUp) { }
    
    // Reaccion al chocar con una abrrera

    public void chocarConBarrera(Barrera barrera) { }

    // Getters y Setters encapsulados 

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
}