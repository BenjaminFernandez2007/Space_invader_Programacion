/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enemigos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import contratos.Colisionable;
import contratos.EstrategiaMovimiento;
import modelo.Jugador;
import modelo.Proyectil;


 // Enemigo que rompe la formación para descender persiguiendo al jugador.
 
public class EnemigoKamikaze extends Enemigo {

    private final double velocidadCaida;
    private final Jugador objetivo;
    private boolean enPicada;
    private int contador;
    
   // Crea un enemigo Kamikaze con velociada 2 y velocidad 3.0

    public EnemigoKamikaze(int x, int y, Jugador objetivo) {
        this(x, y, objetivo, 2, 3.0);
    }
    
    // Esto Crea un kamikaze con parametros personalizados

    public EnemigoKamikaze(int x, int y, Jugador objetivo, int velocidad, double velocidadCaida) {
        super(x, y, 34, 28, 250, 1, velocidad, EstrategiaMovimiento.horizontal());
        this.objetivo = objetivo;
        this.velocidadCaida = velocidadCaida;
    }
    
    // Cuenta los fotogramas y, cuando se cumplen las codiciones, esto iicia la picada
    // Se destruye si sale por la parte inferior de la patalla

    @Override
    public void actualizar() {
        contador++;
        if (!enPicada && contador > 180 && objetivo != null
                && Math.abs(getX() - objetivo.getX()) < 140) {
            enPicada = true;
            setVelocidad((int) Math.max(3, Math.round(velocidadCaida)));
            setEstrategiaMovimiento(EstrategiaMovimiento.descenso()
                    .andThen(EstrategiaMovimiento.persecucion(objetivo)));
        }
        super.actualizar();
        if (getY() > ALTO_PANTALLA) {
            destruir();
        }
    }
    
    // Retorna true si todavia no inicio la picada
    
    @Override
    public boolean isEnFormacion() {
        return !enPicada;
    }
    
    // No dispara: devuleve siempre la lista vacia

    @Override
    public List<Proyectil> disparar() {
        return List.of();
    }
    
    // Delega el primer paso del doble despacho 

    @Override
    public void alColisionar(Colisionable otro) {
        super.alColisionar(otro);
    }
    
    // Dibuja un triangulo naranja apuntando ahcia abajo con un ojo blanco

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(255, 165, 0));
        int[] xs = {getX() + getAncho() / 2, getX(), getX() + getAncho()};
        int[] ys = {getY() + getAlto(), getY(), getY()};
        g.fillPolygon(xs, ys, 3);
        g.setColor(Color.WHITE);
        g.fillOval(getX() + getAncho() / 2 - 4, getY() + 6, 8, 8);
    }
    
    // Retorna true si ya inicio la picada

    public boolean isEnPicada() {
        return enPicada;
    }
    
    // Rerona la velocidad configurada para la picada

    public double getVelocidadCaida() {
        return velocidadCaida;
    }
}

