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
import modelo.Proyectil;


 // Enemigo estándar que avanza en formación horizontal.

public class EnemigoBasico extends Enemigo {

    private int cuadroAnimacion;
    
    // Crea un enemigo basico oon velocidad 2

    public EnemigoBasico(int x, int y) {
        this(x, y, 2);
    }
    
    // Crea un enemigo basico con velocidad personalizada 

    public EnemigoBasico(int x, int y, int velocidad) {
        super(x, y, 36, 26, 100, 1, velocidad, EstrategiaMovimiento.horizontal());
    }
    
    // Avanza el contdador de aim,acion y aplica el movimiento horizontal 

    @Override
    public void actualizar() {
        cuadroAnimacion++;
        super.actualizar();
    }
    
    // No dispara
    // devuelve siempre una lista vacia

    @Override
    public List<Proyectil> disparar() {
        return List.of();
    }
    
    // Delega el primer paso del doble despacho

    @Override
    public void alColisionar(Colisionable otro) {
        super.alColisionar(otro);
    }
    
    // Esto dibuja el enemigo en verde con ojos rojos que cambian de posicion cada cierto tiempo

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(80, 220, 120));
        g.fillRect(getX() + 4, getY(), getAncho() - 8, getAlto() - 6);
        g.fillRect(getX(), getY() + 8, getAncho(), getAlto() - 12);
        g.setColor(Color.BLACK);
        int desplazamiento = (cuadroAnimacion / 25) % 3;
        g.fillRect(getX() + 9 + desplazamiento, getY() + 8, 5, 5);
        g.fillRect(getX() + getAncho() - 14 - desplazamiento, getY() + 8, 5, 5);
    }
}
