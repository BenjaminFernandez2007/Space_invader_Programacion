/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contratos;

import modelo.Jugador;


 // Efecto que aplica una mejora sobre el jugador
 
@FunctionalInterface
public interface EfectoPowerUp {

    // Aplica el efecto de la mejora al jugador indicado
    void aplicar(Jugador j);
}
