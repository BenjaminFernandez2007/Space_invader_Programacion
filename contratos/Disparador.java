/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contratos;

import java.util.List;
import modelo.Proyectil;


  //Contrato para las entidades capaces de generar disparos
 
public interface Disparador {

    // Genera la lista de proyectiles del disparo actual
    List<Proyectil> disparar();
}
