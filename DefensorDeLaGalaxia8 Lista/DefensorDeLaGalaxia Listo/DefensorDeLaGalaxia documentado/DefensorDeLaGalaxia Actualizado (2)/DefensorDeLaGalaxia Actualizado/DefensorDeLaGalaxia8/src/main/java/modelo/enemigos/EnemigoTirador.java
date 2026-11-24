/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enemigos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import contratos.Colisionable;
import contratos.EstrategiaMovimiento;
import modelo.Proyectil;
import modelo.ProyectilEnemigo;

/**
 * Enemigo con mayor resistencia que dispara proyectiles periódicamente.
 */
public class EnemigoTirador extends Enemigo {

    private static final Random ALEATORIO = new Random();

    private final int frecuenciaDisparo;
    private int destello;
    
    // Crea un tirador con velocidad 3 y fecuencia de disparo 600

    public EnemigoTirador(int x, int y) {
        this(x, y, 3, 600);
    }
    
    // Crea un tirador con la velocidad y la frecuncia personalizada

    public EnemigoTirador(int x, int y, int velocidad, int frecuenciaDisparo) {
        super(x, y, 38, 28, 200, 2, velocidad, EstrategiaMovimiento.horizontal());
        this.frecuenciaDisparo = Math.max(60, frecuenciaDisparo);
    }
    
    // Se mueve y reduce el tiempo de destello visual al cañon

    @Override
    public void actualizar() {
        super.actualizar();
        if (destello > 0) {
            destello--;
        }
    }
    
    // Dispara al azar segun su precuencia
    // retorna un proyectil enemigo, o lista vacia si no disparo

    @Override
    public List<Proyectil> disparar() {
        List<Proyectil> disparos = new ArrayList<>();
        if (ALEATORIO.nextInt(frecuenciaDisparo) == 0) {
            int centroX = getX() + getAncho() / 2 - 2;
            disparos.add(new ProyectilEnemigo(centroX, getY() + getAlto(), Color.RED, 5));
            destello = 8;
        }
        return disparos;
    }
    
   

    @Override
    public void alColisionar(Colisionable otro) {
        super.alColisionar(otro);
    }
    
    // Dibuja la nave roja: miestra un desatello blanco en el cañon justo despues de disparar

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(230, 80, 80));
        g.fillRect(getX() + 2, getY() + 4, getAncho() - 4, getAlto() - 8);
        g.fillRect(getX() + 14, getY() + getAlto() - 6, 10, 6);
        g.setColor(Color.YELLOW);
        g.fillRect(getX() + 8, getY() + 10, 6, 5);
        g.fillRect(getX() + getAncho() - 14, getY() + 10, 6, 5);
        if (destello > 0) {
            g.setColor(Color.WHITE);
            g.fillRect(getX() + getAncho() / 2 - 2, getY() + getAlto(), 4, 7);
        }
    }
    
    // retorna el valor de frencia de disparo,  si es mayor disparara menos

    public int getFrecuenciaDisparo() {
        return frecuenciaDisparo;
    }
}

