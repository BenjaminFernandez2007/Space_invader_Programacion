/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import contratos.EstrategiaMovimiento;
import modelo.Barrera;
import modelo.EntidadJuego;
import modelo.Proyectil;
import modelo.ProyectilEnemigo;
import modelo.enemigos.Enemigo;
import modelo.enemigos.EnemigoBasico;
import modelo.enemigos.EnemigoKamikaze;
import modelo.enemigos.EnemigoTirador;



 // Construye las oleadas de enemigos de cada nivel, las barreras y el jefe final
 
public class GestorNiveles {

    
    public static final int NIVEL_FINAL = 3;
    
    // Ultimo nivel de oleadas; despues de aparecer el jefe

    private static final int PASO_X = 52;
    private static final int PASO_Y = 40;
    private static final int COLUMNAS = 8;
    private static final int ORIGEN_Y = 60;
    private static final int ORIGEN_X =
            (EntidadJuego.ANCHO_PANTALLA - (COLUMNAS * PASO_X - 20)) / 2;

    private static final int PUNTOS_JEFE = 500;
    private static final int RESISTENCIA_JEFE = 40;
    private static final int VELOCIDAD_JEFE = 3;

   // Datos de un nivel: Velodiad de los enemigos 
   // frecuencia de disparo
   // Filas de la formacion
    
    private record ConfiguracionNivel(int velocidad, int frecuenciaTirador, List<String> filas) { }

    private static final List<ConfiguracionNivel> NIVELES = List.of(
            new ConfiguracionNivel(2, 700,
                    List.of("TTTTTTTT", "BBBBBBBB", "BBBBBBBB")),
            new ConfiguracionNivel(3, 500,
                    List.of("TTTTTTTT", "BKBKBKBK", "BBBBBBBB", "BBBBBBBB")),
            new ConfiguracionNivel(4, 450,
                    List.of("TTTTTTTT", "KBKBKBKB", "TBTBTBTB", "BBBBBBBB", "BBBBBBBB")));

    private final Partida partida;
    private int nivelActual;
    private boolean jefeCreado;
    
    
    // Crea el gestor asociados a una partida
   
    public GestorNiveles(Partida partida) {
        this.partida = Objects.requireNonNull(partida, "La partida no puede ser nula");
    }

    
    // Carga la formacion del nivel indicado y devuele sus enemigos
    // En el nivel 1 tambien crea las barreras
    
    // el @throws idica si el nivel no existe 
    
    // Retorna la lista no modificable con los enemigos creados
    
    public List<Enemigo> cargarNivel(int nivel) {
        if (nivel < 1 || nivel > NIVEL_FINAL) {
            throw new IllegalArgumentException("Nivel inexistente: " + nivel);
        }
        nivelActual = nivel;
        partida.setNivel(nivel);
        if (nivel == 1) {
            crearBarreras();
        }
        ConfiguracionNivel config = NIVELES.get(nivel - 1);
        Map<Character, Supplier<Enemigo>> fabricas = crearFabricas(config);
        List<Enemigo> creados = new ArrayList<>();
        List<String> filas = config.filas();
        for (int f = 0; f < filas.size(); f++) {
            String fila = filas.get(f);
            for (int c = 0; c < fila.length(); c++) {
                Enemigo enemigo = fabricas.get(fila.charAt(c)).get();
                enemigo.setX(ORIGEN_X + c * PASO_X);
                enemigo.setY(ORIGEN_Y + f * PASO_Y);
                partida.agregarEnemigo(enemigo);
                creados.add(enemigo);
            }
        }
        return Collections.unmodifiableList(creados);
    }

   
    // Carga el siguiente niel: tras el ultimo, 
    // crea al jefe ya cuando fue derrotado, termina la partida con una victoria
    public void siguienteNivel() {
        if (nivelActual < NIVEL_FINAL) {
            cargarNivel(nivelActual + 1);
        } else if (!jefeCreado) {
            jefeCreado = true;
            partida.agregarEnemigo(crearJefe());
        } else {
            partida.terminar(true);
        }
    }

   
    // Crea el jefe final mediante una clase anónima que extiende Enemigo
    
    // Rebota desde los bordes, tiene 3 fases segun si vida restante
    // cada vez que baja su vida, se mueve mas rapido y dispara con mayor frecuencia
    // lanza mas proyectils, ademas dibuja su propia barra de vida 
    
    // retorna el jefe final
    public Enemigo crearJefe() {
        return new Enemigo(EntidadJuego.ANCHO_PANTALLA / 2 - 60, 50, 120, 60,
                PUNTOS_JEFE, RESISTENCIA_JEFE, VELOCIDAD_JEFE, EstrategiaMovimiento.horizontal()) {

            private int ciclos;
            private int fase = 1;
            
            // Se nyeve, rebota en los bordes y sube de fase y velocidad al perder una vida

            @Override
            public void actualizar() {
                super.actualizar();
                rebotarEnBordes();
                ciclos++;
                int nuevaFase = calcularFase();
                if (nuevaFase != fase) {
                    fase = nuevaFase;
                    setVelocidad(VELOCIDAD_JEFE + fase - 1);
                }
            }
            
    // Fase 1 con mas de 2/3 de vida, fase 2 con mas de 1/3 y fase 3 con menos

            private int calcularFase() {
                double proporcion = (double) getResistencia() / getResistenciaInicial();
                if (proporcion > 2.0 / 3) {
                    return 1;
                }
                return proporcion > 1.0 / 3 ? 2 : 3;
            }
            
            // Dispara 1, 3 o 5 Proyectiles, cada vez mas seguido segun la fase

            @Override
            public List<Proyectil> disparar() {
                List<Proyectil> disparos = new ArrayList<>();
                int intervalo = fase == 1 ? 70 : (fase == 2 ? 45 : 28);
                if (ciclos % intervalo != 0) {
                    return disparos;
                }
                int centro = getX() + getAncho() / 2;
                int y = getY() + getAlto();
                disparos.add(new ProyectilEnemigo(centro - 3, y, Color.MAGENTA, 5));
                if (fase >= 2) {
                    disparos.add(new ProyectilEnemigo(centro - 43, y, Color.MAGENTA, 5));
                    disparos.add(new ProyectilEnemigo(centro + 37, y, Color.MAGENTA, 5));
                }
                if (fase == 3) {
                    disparos.add(new ProyectilEnemigo(centro - 73, y, Color.MAGENTA, 5));
                    disparos.add(new ProyectilEnemigo(centro + 67, y, Color.MAGENTA, 5));
                }
                return disparos;
            }

            // El jefe no sigue la grilla: rebota por su cuenta en los bordes
            @Override
            public boolean isEnFormacion() {
                return false;
            }
            // Dibuja al jefe, su color cambia con la fase y su barra de vida
            @Override
            public void dibujar(Graphics2D g) {
                Color[] colores = {new Color(150, 0, 200), Color.ORANGE, Color.RED};
                g.setColor(colores[fase - 1]);
                g.fillRect(getX(), getY() + 10, getAncho(), getAlto() - 10);
                g.fillRect(getX() + 40, getY(), getAncho() - 80, 14);
                g.setColor(Color.WHITE);
                g.fillRect(getX() + 25, getY() + 25, 12, 12);
                g.fillRect(getX() + getAncho() - 37, getY() + 25, 12, 12);
                // Barra de vida
                g.setColor(Color.DARK_GRAY);
                g.fillRect(getX(), getY() - 10, getAncho(), 6);
                g.setColor(Color.GREEN);
                g.fillRect(getX(), getY() - 10,
                        getAncho() * getResistencia() / getResistenciaInicial(), 6);
            }
        };
    }
    
    // retorna el numero del nivel cargado actualmente

    public int getNivelActual() {
        return nivelActual;
    }

    // Fábricas de enemigos del nivel: cada letra de la configuración produce un tipo.
    private Map<Character, Supplier<Enemigo>> crearFabricas(ConfiguracionNivel config) {
        Map<Character, Supplier<Enemigo>> fabricas = new HashMap<>();
        fabricas.put('B', () -> new EnemigoBasico(0, 0, config.velocidad()));
        fabricas.put('T', () -> new EnemigoTirador(0, 0, config.velocidad(), config.frecuenciaTirador()));
        fabricas.put('K', () -> new EnemigoKamikaze(0, 0, partida.getJugador(), config.velocidad(), 3.0));
        return fabricas;
    }
    
    // Crea las 4 barreras defensivas sobre el jugador 
    // Solo en el nivel 1

    private void crearBarreras() {
        int y = EntidadJuego.ALTO_PANTALLA - 180;
        for (int i = 0; i < 4; i++) {
            partida.agregarEntidad(new Barrera(i * 200 + 60, y));
        }
    }
}

