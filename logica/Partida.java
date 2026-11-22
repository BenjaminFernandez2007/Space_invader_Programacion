/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import modelo.EntidadJuego;
import modelo.Jugador;
import modelo.PowerUp;
import modelo.enemigos.Enemigo;



 // Lógica central de una partida: administra entidades, colisiones, puntaje,
 // niveles y estado de pausa/victoria/derrota.
 
public class Partida {

    private static final double PROBABILIDAD_MEJORA = 0.20;
    private static final Random ALEATORIO = new Random();
    
    // Fabrica de mejoras: una se leige al azar cuando un enemigo es abatido

    private static final List<BiFunction<Integer, Integer, PowerUp>> FABRICAS_MEJORA = List.of(
            (x, y) -> PowerUp.escudo(x, y),
            (x, y) -> PowerUp.disparoDoble(x, y),
            (x, y) -> PowerUp.vidaExtra(x, y));

    private final List<EntidadJuego> entidades = new ArrayList<>();
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final Jugador jugador;
    private final Function<Enemigo, Integer> bonificacion;
    private int puntaje;
    private int nivel = 1;
    private boolean terminada;
    private boolean victoria;
    private boolean pausada;

    // Crea la partida con el jugador ubicado en la parte inferior
    public Partida() {
        this.jugador = new Jugador(EntidadJuego.ANCHO_PANTALLA / 2 - 20,
                EntidadJuego.ALTO_PANTALLA - 70);
        this.entidades.add(jugador);
        
        // putnos del enemigo multipados por el nivel actual
        
        this.bonificacion = e -> e.getPuntos() * this.nivel;
    }

   
    // Ejecuta un fotograma de la partida 
    public void actualizar() {
        if (terminada || pausada) {
            return;
        }
        Consumer<EntidadJuego> actualizacionPolimorfica = EntidadJuego::actualizar;
        entidades.forEach(actualizacionPolimorfica);
        moverFormacion();
        enemigos.stream()
                .filter(Enemigo::isActiva)
                .map(Enemigo::disparar)
                .forEach(entidades::addAll);
        detectarColisiones();
        enemigos.stream()
                .filter(Enemigo::isAbatido)
                .forEach(this::registrarEnemigoAbatido);
        Predicate<EntidadJuego> entidadInactiva = e -> !e.isActiva();
        entidades.removeIf(entidadInactiva);
        enemigos.removeIf(e -> !e.isActiva());
        verificarFinDePartida();
    }

    // Agrega una entidad visible a la partida.
    public void agregarEntidad(EntidadJuego e) {
        if (e == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        entidades.add(e);
    }

    // Devuelve una vista no modificable de las entidades
    // retorna la entidades actual para dibujarse 
    public List<EntidadJuego> getEntidades() {
        return Collections.unmodifiableList(entidades);
    }

    // Resuelve las colisiones aplicando doble despacho
    public void detectarColisiones() {
        for (int i = 0; i < entidades.size(); i++) {
            EntidadJuego a = entidades.get(i);
            for (int j = i + 1; j < entidades.size(); j++) {
                EntidadJuego b = entidades.get(j);
                if (a.isActiva() && b.isActiva() && a.getLimites().intersects(b.getLimites())) {
                    a.alColisionar(b);
                    b.alColisionar(a);
                }
            }
        }
    }

    // Suma puntos validando que el puntaje nunca disminuya
    // los puntos son puntos a sumar
    public void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("El puntaje no puede disminuir");
        }
        puntaje += puntos;
    }

    // Dispara con el jugador y agrega los proyectiles a la partida
    public void dispararJugador() {
        if (terminada || pausada) {
            return;
        }
        jugador.disparar().forEach(this::agregarEntidad);
    }
    
    // oibe partida en pausa

    public void pausar() {
        pausada = true;
    }
    
    // reaunira la partida

    public void reanudar() {
        pausada = false;
    }

    // Indica si no quedan enemigos activos en el nivel actual 
    // retorna true si se eliminaron todos y la partida sigue en curso
    
    public boolean isNivelCompletado() {
        return !terminada && enemigos.isEmpty();
    }
    
    // retorna el jugadro de esta partida

    public Jugador getJugador() {
        return jugador;
    }
    
    // retorna el punta aculado

    public int getPuntaje() {
        return puntaje;
    }
    
    // retorna el nivel actual del 1 al 3

    public int getNivel() {
        return nivel;
    }
    
    // retorna true si la partida termino ya sea victoria o derrota

    public boolean isTerminada() {
        return terminada;
    }
    
    // retorna true si termino con victoria

    public boolean isVictoria() {
        return victoria;
    }
    
    // reotrna true si esta enpausa

    public boolean isPausada() {
        return pausada;
    }
    
    // Registra un enemigo tanto en la lista de enmigos como en la de entidades
    // Solo accesible desde el paquete GestorNiveles

    
    void agregarEnemigo(Enemigo e) {
        if (e == null) {
            throw new IllegalArgumentException("El enemigo no puede ser nulo");
        }
        enemigos.add(e);
        entidades.add(e);
    }
    
    // Cambia el nivel actual 
    // Afecta el multiplicador de puntos

    void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    // Termina la partida una sola vez
    // true si el jugador gano

   
    void terminar(boolean victoria) {
        if (!terminada) {
            terminada = true;
            this.victoria = victoria;
        }
    }
    
    // Esto mueve la formacion como bloque
    // Si algun enemigo toca el borde, todos los questa en formacion invierten y bajan

    
    private void moverFormacion() {
        List<Enemigo> formacion = enemigos.stream()
                .filter(Enemigo::isActiva)
                .filter(Enemigo::isEnFormacion)
                .toList();
        if (formacion.stream().anyMatch(Enemigo::tocaBorde)) {
            formacion.forEach(e -> {
                e.invertirDireccion();
                e.descender();
            });
        }
    }
    
    // Suna los puntos del enemigo abatido y, con 20% de probabilidad, deja caer una mejora

    private void registrarEnemigoAbatido(Enemigo e) {
        sumarPuntos(bonificacion.apply(e));
        if (ALEATORIO.nextDouble() < PROBABILIDAD_MEJORA) {
            BiFunction<Integer, Integer, PowerUp> fabrica =
                    FABRICAS_MEJORA.get(ALEATORIO.nextInt(FABRICAS_MEJORA.size()));
            agregarEntidad(fabrica.apply(e.getX(), e.getY()));
        }
    }
    
    // La partida termina con derrota si el jugador se queda sin vidas
    // O si la formacion llega a su altura

    private void verificarFinDePartida() {
        if (jugador.getVidas() == 0) {
            terminar(false);
            return;
        }
        boolean llegaronAbajo = enemigos.stream()
                .filter(Enemigo::isEnFormacion)
                .anyMatch(e -> e.getY() + e.getAlto() >= jugador.getY());
        if (llegaronAbajo) {
            terminar(false);
        }
    }
}

    
