/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contratos;

import modelo.Jugador;
import modelo.enemigos.Enemigo;


 // Estrategia funcional que define cómo se mueve un enemigo en cada fotograma.
 
@FunctionalInterface
public interface EstrategiaMovimiento {

    // Aplica el movimiento al enemigo indicado
    void mover(Enemigo e);

    // Compone esta estrategia con otra que se ejecuta a continuación
    default EstrategiaMovimiento andThen(EstrategiaMovimiento despues) {
        return e -> {
            this.mover(e);
            despues.mover(e);
        };
    }

    // Movimiento horizontal en formación
    static EstrategiaMovimiento horizontal() {
        return e -> e.setX(e.getX() + e.getDireccionHorizontal() * e.getVelocidad());
    }

    // Movimiento en zigzag con rebote en los bordes
    static EstrategiaMovimiento zigzag() {
        return e -> {
            e.rebotarEnBordes();
            e.setX(e.getX() + e.getDireccionHorizontal() * e.getVelocidad());
            e.setY(e.getY() + 1);
        };
    }

    // Descenso vertical continuo. 
    static EstrategiaMovimiento descenso() {
        return e -> e.setY(e.getY() + e.getVelocidad());
    }

    // Persecución horizontal del jugador.
    static EstrategiaMovimiento persecucion(Jugador objetivo) {
        return e -> {
            int sentido = Integer.signum(objetivo.getX() - e.getX());
            e.setX(e.getX() + sentido * e.getVelocidad());
        };
    }
}
