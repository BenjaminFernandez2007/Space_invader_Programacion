# Defensores de la Galaxia
 Este es un videojuego de arcade que desarrollamos en java utilizando interfaz grafica Swing,
En donde el jugador debe controlar una nave en la parte inferior de la pantalla, donde
su principal objetivo es disparar para poder eliminar a los enemigos que se encuentran en la parte superior de la
pantalla. Al eliminar a los enemigos, estos soltaran PowerUp que ayudan al jugador a eliminar a los enemigos más rápido,
ya que, al comenzar el nivel, los enemigos van bajando, y si tocan al jugador, este perderá.
El juego consiste de 3 niveles, que al terminar el 3 nivel, aparece el jefe final,  
cada nivel que avance el jugador, mas complicado y rápido caerán las naves.

Integrantes: Benjamin Fernandez, Jacob Perez, joaquin Gonzalez, Matias Trincado

# Requisitos
- Tener JDK 22
- Maven 3.9 o superior para que pueda complicar

# Como ejecutar el código

# Con el ejecutable (.jar)

    java -jar DefensorDeLaGalaxia-1.0-SNAPSHOT.jar

# Desde el código fuente con Maven

    mvn clean package
    java -jar target/DefensorDeLaGalaxia-1.0-SNAPSHOT.jar

Para poder ejecutar el código por NetBeans 

- En el menú principal de NetBeans debe seleccionar "Open Proyect"
- Seleccionar el Proyecto"DefensoresDeLaGalaxia8" y abrir


# Controles – jugabilidad
- Pausa: p
- Moverse izquierda: ←
- Moverse Derecha: →
- Espacio para disparar
- ESQ para salir


# Reglas del juego

- El jugador inicia con 3 Vidas
- Hay 3 niveles con dificultad progresiva, al terminar el tercer nivel aparece el jefe final
- Al destruir un enemigo hay un 20% de probabilidad de que caiga una mejora
- Las mejoras son escudo temporal, disparo doble y vida extra
- Las barreras se desgastan por cada disparo
- Se gana al derrotar al jefe del nivel 3
- Se pierde al quedarse sin vidas o si un enemigo llega a la línea inferior
- El puntaje Maximo de la sesion se muestra en el menu

# Estructura del proyecto

- Contratos: interfaces y contratos del juego
- Modelo: entidades, jugador, proyectiles, mejoras y barreras
- Modelo. Enemigo: enemigos y jefe final
- Lógica: partida y gestor niveles
- Ui: ventana y paneles Swing
- Main: punto donde ejecutaremos el código

