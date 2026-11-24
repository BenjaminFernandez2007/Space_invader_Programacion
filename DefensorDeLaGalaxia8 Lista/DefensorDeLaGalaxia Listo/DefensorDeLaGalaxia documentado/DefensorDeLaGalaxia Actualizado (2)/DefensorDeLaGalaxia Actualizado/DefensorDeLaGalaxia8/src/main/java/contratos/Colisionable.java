/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contratos;

import java.awt.Rectangle;
import modelo.EntidadJuego;


 // Contrato para toda entidad que puede colisionar con otra
 
public interface Colisionable {

    // Devuelve el área rectangular ocupada por la entidad
    Rectangle getLimites();

    // Notifica que esta entidad colisionó con otra
    void alColisionar(Colisionable otro);

    // Envía la colisión al receptor mediante doble despacho
    void notificarColisionA(EntidadJuego receptor);
}
