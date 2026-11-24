/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import contratos.Colisionable;

/**
 * Barrera defensiva que absorbe impactos tanto del jugador como de los enemigos.
 */
public class Barrera extends EntidadJuego {
    
    // Ceamos una barrera con la resistencia por defecto 10

    private static final int RESISTENCIA_POR_DEFECTO = 10;

    private int resistencia;
    private int resistenciaMaxima;
    
  

    public Barrera(int x, int y) {
        this(x, y, RESISTENCIA_POR_DEFECTO);
    }
    
    // Crea ua barrera con resistencia personalida con un minimo de 1

    public Barrera(int x, int y, int resistencia) {
        super(x, y, 80, 30);
        this.resistenciaMaxima = Math.max(1, resistencia);
        this.resistencia = this.resistenciaMaxima;
    }
    
    // Resta la resistencia: si llega a 0, la barrera se destruye
    
    // danio es el daño recibido y si ignora si o es positivo

    public void recibirDanio(int danio) {
        if (danio > 0) {
            resistencia = Math.max(0, resistencia - danio);
            if (resistencia == 0) {
                destruir();
            }
        }
    }
    
    // Las barreras son estaticas

    @Override
    public void actualizar() {
        // Las barreras son estáticas
    }
    
    // dibuja la barrera, el color y la altura reflejan la resistencia restante

    @Override
    public void dibujar(Graphics2D g) {
        if (resistencia > resistenciaMaxima * 0.7) {
            g.setColor(Color.GREEN);
        } else if (resistencia > resistenciaMaxima * 0.3) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        int altoVisible = Math.max(6, (getAlto() * resistencia) / resistenciaMaxima);
        int offsetY = getAlto() - altoVisible;
        g.fillRect(getX(), getY() + offsetY, getAncho(), altoVisible);
    }
    
    // avisa a que choco con una barrera

    @Override
    public void alColisionar(Colisionable otro) {
        otro.notificarColisionA(this);
    }
    
    // le informa al receptor que choco con esta barrera

    @Override
    public void notificarColisionA(EntidadJuego receptor) {
        receptor.chocarConBarrera(this);
    }
    
    // Un pryectil del jugador daña la barrera

    @Override
    public void chocarConProyectilJugador(ProyectilJugador proyectil) {
        recibirDanio(proyectil.getDanio());
    }
    
    // Un proyectil enemigo daña la barrera
    @Override
    public void chocarConProyectilEnemigo(ProyectilEnemigo proyectil) {
        recibirDanio(proyectil.getDanio());
    }
    
    // retorna la resistencia restante

    public int getResistencia() {
        return resistencia;
    }
}

