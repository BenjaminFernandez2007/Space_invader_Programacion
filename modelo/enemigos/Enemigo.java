/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enemigos;

import java.util.List;
import contratos.Colisionable;
import contratos.Disparador;
import contratos.EstrategiaMovimiento;
import contratos.Puntuable;
import modelo.EntidadJuego;
import modelo.Jugador;
import modelo.Proyectil;
import modelo.ProyectilJugador;


 // Clase abstracta base para todos los enemigos y el jefe final.
 
public abstract class Enemigo extends EntidadJuego implements Disparador, Puntuable {

    private final int puntos;
    private final int resistenciaInicial;
    private int resistencia;
    private EstrategiaMovimiento estrategiaMovimiento;
    private int velocidad;
    private int direccionHorizontal = 1;
    private boolean abatido;
    
    // Crea un enemio

    protected Enemigo(int x, int y, int ancho, int alto, int puntos, int resistencia, int velocidad, EstrategiaMovimiento estrategiaMovimiento) {
        super(x, y, ancho, alto);
        this.puntos = puntos;
        this.resistenciaInicial = resistencia;
        this.resistencia = resistencia;
        this.velocidad = velocidad;
        this.estrategiaMovimiento = estrategiaMovimiento;
    }
    
    // Ejecuta un paso de la estrategia del movimientoa actual

    @Override
    public void actualizar() {
        if (estrategiaMovimiento != null) {
            estrategiaMovimiento.mover(this);
        }
    }
    
    // Cada ttupo de enemigo define si dispara y el como 

    @Override
    public abstract List<Proyectil> disparar();
    
    // Esto resto la resistencia, Si llega a 0, el enemigo queda marcado como
    // abatido y se destruye
    
    // el danio es el daño recibido 

    public void recibirDanio(int danio) {
        if (danio > 0) {
            resistencia = Math.max(0, resistencia - danio);
            if (resistencia == 0) {
                abatido = true;
                destruir();
            }
        }
    }
    
    // Esto indica si murio por daño y no por salir de la pantalla
    // solo los abatidos suman puntos

    public boolean isAbatido() {
        return abatido;
    }
    
    // Indica si el enemigo se mueve junto con la formacion. Las subclases que se separan de ella
    // Los kamikaze en picada  y los jefees

    public boolean isEnFormacion() {
        return true;
    }
    
    // Indica si el enemigo llgo al borde lateral hacia el que avanza
    // retorna true si toda el borde en sus direccion actual

    public boolean tocaBorde() {
        return (direccionHorizontal > 0 && getX() + getAncho() >= ANCHO_PANTALLA - 15)
                || (direccionHorizontal < 0 && getX() <= 15);
    }
    
    // Invierte la direccion horizonal

    public void invertirDireccion() {
        direccionHorizontal = -direccionHorizontal;
    }
    
    // Baja al enemigo 18 pixeles cuando la formacion toca un borde

    public void descender() {
        setY(getY() + 18);
    }
    
    // Esto signfica que si toca un borde de la pantalla, lo reposiciona y cambia de direccion

    public void rebotarEnBordes() {
        if (getX() <= 10) {
            setX(10);
            direccionHorizontal = 1;
        } else if (getX() + getAncho() >= ANCHO_PANTALLA - 10) {
            setX(ANCHO_PANTALLA - getAncho() - 10);
            direccionHorizontal = -1;
        }
    }
    
    // avisa Colisionable otro que choco con un enemigo

    @Override
    public void alColisionar(Colisionable otro) {
        otro.notificarColisionA(this);
    }
    
    // le informa al receptor que choco con este enemigo

    @Override
    public void notificarColisionA(EntidadJuego receptor) {
        receptor.chocarConEnemigo(this);
    }
    
    // Un proyectil del juador le causa daño al enemmigo

    @Override
    public void chocarConProyectilJugador(ProyectilJugador proyectil) {
        recibirDanio(proyectil.getDanio());
    }
    
    // Al chocar con el jugador, el enemigo muere y el jugador pierde una avisa

    @Override
    public void chocarConJugador(Jugador jugador) {
        recibirDanio(resistencia);
    }
    
    // retorna los puntos base que otroga al ser abatido

    @Override
    public int getPuntos() {
        return puntos;
    }
    
    // retorna la resistencia actual

    public int getResistencia() {
        return resistencia;
    }
    
    // retorna la resistencia con la que nacio

    public int getResistenciaInicial() {
        return resistenciaInicial;
    }
    
    // retorna la velocidad en pixeles por fotograma

    public int getVelocidad() {
        return velocidad;
    }
    
    // nueva velocidad en pixeles

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    
    // retorna 1 si avanza hacia la derecha
    // -1 hacia la izquierda

    public int getDireccionHorizontal() {
        return direccionHorizontal;
    }
    
    // nueva forma de moverse del enemigo

    public void setEstrategiaMovimiento(EstrategiaMovimiento estrategiaMovimiento) {
        this.estrategiaMovimiento = estrategiaMovimiento;
    }
}

