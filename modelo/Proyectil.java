/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import contratos.Colisionable;


 // Clase abstracta que representa cualquier disparo en el juego.
 
public abstract class Proyectil extends EntidadJuego {

    private final int direccion; // -1 hacia arriba (jugador), 1 hacia abajo (enemigo)
    private final double velocidad;
    private final int danio;
    
    // Creamos un proyectil

    protected Proyectil(int x, int y, int ancho, int alto, int direccion,
            double velocidad, int danio) {
        super(x, y, ancho, alto);
        this.direccion = direccion;
        this.velocidad = velocidad;
        this.danio = danio;
    }
    
    // Avanza seguin su direccion y velocidad: se destruye al salir de la pantalla

    @Override
    public void actualizar() {
        setY(getY() + (int) Math.round(direccion * velocidad));
        if (getY() + getAlto() < 0 || getY() > ALTO_PANTALLA) {
            destruir();
        }
    }
    
    // Cada tipo de proyectil implementa su propio primer paso de doble despacho

    @Override
    public abstract void alColisionar(Colisionable otro);
    
    // retorna -1 si va hacia arriba, 1 si va hacia abajo

    public int getDireccion() {
        return direccion;
    }
    
    // retorna la velocidad en pixeles por fotograma

    public double getVelocidad() {
        return velocidad;
    }
    
    // retorna daño que causa al impactar

    public int getDanio() {
        return danio;
    }
}
