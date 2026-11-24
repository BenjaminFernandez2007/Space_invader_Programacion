/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import contratos.Colisionable;
import contratos.Disparador;


 //Nave controlada por el jugador en la parte inferior de la pantalla.
 

public class Jugador extends EntidadJuego implements Disparador {

    private static final int VIDAS_INICIALES = 3;
    private static final int VIDAS_MAXIMAS = 5;
    private static final int VELOCIDAD_BASE = 6;
    private static final int COOLDOWN_DISPARO = 18; // fotogramas entre disparos

    private int vidas;
    private int velocidad;
    private int recarga;
    private boolean escudoActivo;
    private boolean disparoDoble;
    private int ciclosEscudo;
    private int ciclosDisparoDoble;
    

    public Jugador(int x, int y) {
        super(x, y, 44, 28);
        this.vidas = VIDAS_INICIALES;
        this.velocidad = VELOCIDAD_BASE;
    }
    
    // Mueve la nave a la Izquierda sin salirse de la pantalla

    public void moverIzquierda() {
        setX(Math.max(10, getX() - velocidad));
    }
    
    // Mueve la nave a la derecha sin salirse de la pantalla

    public void moverDerecha() {
        setX(Math.min(ANCHO_PANTALLA - getAncho() - 10, getX() + velocidad));
    }
    
    // Dispara si la recarga termino
    // un proyectil centrado o dos laterales si el disparo doble esta activo
    
    // return proyectiles creados si la lista vacia aun esta recarando

    @Override
    public List<Proyectil> disparar() {
        List<Proyectil> disparos = new ArrayList<>();
        if (recarga > 0) {
            return disparos;
        }
        recarga = COOLDOWN_DISPARO;
        int centroX = getX() + getAncho() / 2;
        if (disparoDoble) {
            disparos.add(new ProyectilJugador(getX() + 6, getY() - 10));
            disparos.add(new ProyectilJugador(getX() + getAncho() - 10, getY() - 10));
        } else {
            disparos.add(new ProyectilJugador(centroX - 2, getY() - 12));
        }
        return disparos;
    }
    
    // Resta una vida, a no ser que el escudo este activo
    // Si las vidas llegan a 0, la nave se destruye

    public void perderVida() {
        if (escudoActivo) {
            return;
        }
        if (vidas > 0) {
            vidas--;
        }
        if (vidas == 0) {
            destruir();
        }
    }
    
    // Suma una vida, sin superar el maximo de 5

    public void ganarVida() {
        if (vidas < VIDAS_MAXIMAS) {
            vidas++;
        }
    }
    
    // Activa el escudo durante cierta cantidad de fotogramas, Si ya esta activo
    // Se conserva la duracion mayor
    
    // ciclos representa la duracion en fotogramas, se ignora si es 0 o negativo

    public void activarEscudo(int ciclos) {
        if (ciclos <= 0) {
            return;
        }
        this.ciclosEscudo = Math.max(this.ciclosEscudo, ciclos);
        this.escudoActivo = true;
    }
    
    // Ativa el disparo doble durante cierta cantidad de fotogramas
    // Si ya esta activo, se conserva la duracion mayor
    

    public void activarDisparoDoble(int ciclos) {
        if (ciclos <= 0) {
            return;
        }
        this.ciclosDisparoDoble = Math.max(this.ciclosDisparoDoble, ciclos);
        this.disparoDoble = true;
    }
    
    // Avanza un fotograma: se reduce la rrecarga y el tiempo restante de los efectos

    @Override
    public void actualizar() {
        if (recarga > 0) {
            recarga--;
        }
        if (escudoActivo) {
            ciclosEscudo--;
            if (ciclosEscudo <= 0) {
                ciclosEscudo = 0;
                escudoActivo = false;
            }
        }
        if (disparoDoble) {
            ciclosDisparoDoble--;
            if (ciclosDisparoDoble <= 0) {
                ciclosDisparoDoble = 0;
                disparoDoble = false;
            }
        }
    }
    
    // Esto dibuja la nave en verde y, si hay escudo, un ovalo color cian alrededor

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.fillRect(getX(), getY() + 10, getAncho(), getAlto() - 10);
        g.fillRect(getX() + 16, getY(), 12, 12);

        if (isEscudoActivo()) {
            g.setColor(Color.CYAN);
            g.drawOval(getX() - 6, getY() - 6, getAncho() + 12, getAlto() + 12);
        }
    }
    
    // avisa que choco un jugador

    @Override
    public void alColisionar(Colisionable otro) {
        otro.notificarColisionA(this);
    }
    
    // infroma al receptor que choco con este jugador

    @Override
    public void notificarColisionA(EntidadJuego receptor) {
        receptor.chocarConJugador(this);
    }
    
    // un proyectil enemigo le quita una vida al jugador

    @Override
    public void chocarConProyectilEnemigo(ProyectilEnemigo proyectil) {
        perderVida();
    }
    
    // Chocar con un eemigo le quita una vida al jugador

    @Override
    public void chocarConEnemigo(modelo.enemigos.Enemigo enemigo) {
        perderVida();
    }
    
    // retorna las vidas restantes

    public int getVidas() {
        return vidas;
    }
    
    // retorna la velocidad de movimiento en pixeles por fotograma

    public int getVelocidad() {
        return velocidad;
    }
    // retorna los fotogramas que faltan para poder disparar denuevo

    public int getRecarga() {
        return recarga;
    }
    
    // retunr si el escudo esta activo

    public boolean isEscudoActivo() {
        return escudoActivo;
    }
    
    // retunr si el disparo doble esta activo

    public boolean isDisparoDoble() {
        return disparoDoble;
    }
}

