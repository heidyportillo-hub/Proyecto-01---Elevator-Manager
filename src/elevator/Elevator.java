package elevator;
import java.io.IOException;
import java.util.logging.*;
import java.util.LinkedList;
import java.util.Queue;

public class Elevator extends Thread {
    // Direccion

    public final static int arriba = 1;
    public final static int abajo = 2;
    public final static int neutro = 0;

    // Atributos
    private int totalNiveles;
    private int codigo;
    private int direccion;
    private int actual;
    private int tiempoNiveles;
    private int tiempoEspera;
    private LinkedList<Integer> niveles;
    private LinkedList<Integer> solicitudesArriba;
    private LinkedList<Integer> solicitudesAbajo;
    private boolean estado;
    private boolean reiniciador;
    private Logger log;

    // Constructor
    public Elevator(int codigo, int totalNiveles, int tiempoNiveles, int tiempoEspera) {

        actual = 1;
        direccion = arriba;
        estado = true;
        reiniciador = false;

        this.codigo = codigo;
        this.totalNiveles = totalNiveles;
        this.tiempoNiveles = tiempoNiveles;
        this.tiempoEspera = tiempoEspera;

        niveles = new LinkedList<>();
        solicitudesArriba = new LinkedList<>();
        solicitudesAbajo = new LinkedList<>();

        logs();
        log("Elevador creado");
    }

    // Getters
    public synchronized int getCodigo() {
        return codigo;
    }

    public synchronized int getActual() {
        return actual;
    }

    public synchronized int getDireccion() {
        return direccion;
    }

    public synchronized boolean getEncendido(int nivel) {
        return(niveles.contains(nivel));
    }

    public synchronized boolean getexisteSolicitudArriba(int nivel) {
        return solicitudesArriba.contains(nivel);
    }

    public synchronized boolean getexisteSolicitudAbajo(int nivel) {
        return solicitudesAbajo.contains(nivel);
    }

    public synchronized int getCantidadSolicitudes() {
        return niveles.size() + solicitudesArriba.size() + solicitudesAbajo.size();
    }

    public synchronized boolean estaVacio() {
        return !existenSolicitudes() && !reiniciador;
    }

    // Logs
    private void logs() {
        log = Logger.getLogger("Elevador - " + codigo);
        try {
            FileHandler archivo = new FileHandler("elevador" + codigo + ".log", false);
            archivo.setFormatter(new SimpleFormatter());
            log.addHandler(archivo);
        } catch (IOException e) {
            System.out.println("Error, el archivo no se pudo crear: " + e.getMessage());
        }
    }

    private void log(String texto) {
        log.info("E" + codigo + ": " + texto);
    }

    // Metodos
    public synchronized void presionarNivel(int nivel) {
        if (nivel < 1 || nivel > totalNiveles) {
            return;
        }
        if (!niveles.contains(nivel)) {
            niveles.add(nivel);
            log("Boton presionado: " + nivel);
        }
    }

    public synchronized void ingresarNivel(int nivel, int sentido) {
        if (nivel < 1 || nivel > totalNiveles) {
            return;
        }
        if (sentido == arriba && !solicitudesArriba.contains(nivel)) {
            solicitudesArriba.add(nivel);
            log("Direccion hacia arriba en el nivel: " + nivel);
        } else if (sentido == abajo && !solicitudesAbajo.contains(nivel)) {
            solicitudesAbajo.add(nivel);
            log("Direccion hacia abajo en el nivel: " + nivel);
        }
    }

    private synchronized boolean existenSolicitudes() {
        if (!niveles.isEmpty()) {
            return true;
        }
        if (!solicitudesArriba.isEmpty()) {
            return true;
        }
        if (!solicitudesAbajo.isEmpty()) {
            return true;
        }
        return false;
    }

    private synchronized boolean existeAdelante() {
        if (direccion == arriba) {
            for (int nivel = actual + 1; nivel <= totalNiveles; nivel = nivel + 1) {
                if (niveles.contains(nivel)) {
                    return true;
                }
                if (solicitudesArriba.contains(nivel)) {
                    return true;
                }
                if (solicitudesAbajo.contains(nivel)) {
                    return true;
                }
            }
        } else if (direccion == abajo) {
            for (int nivel = 1; nivel < actual; nivel = nivel + 1) {
                if (niveles.contains(nivel)) {
                    return true;
                }
                if (solicitudesArriba.contains(nivel)) {
                    return true;
                }
                if (solicitudesAbajo.contains(nivel)) {
                    return true;
                }
            }
        }
        return false;
    }

    private synchronized boolean debeDetenerse() {
        if (niveles.contains(actual)) {
            return true;
        }
        if (direccion == arriba) {
            return solicitudesArriba.contains(actual);
        }
        if (direccion == abajo) {
            return solicitudesAbajo.contains(actual);
        }
        return false;
    }

    private synchronized void apagarActual() {
        niveles.remove(Integer.valueOf(actual));
        if (direccion == arriba) {
            solicitudesArriba.remove(Integer.valueOf(actual));
        } else if (direccion == abajo) {
            solicitudesAbajo.remove(Integer.valueOf(actual));
        }
    }

    private synchronized boolean estaActivo() {
        return estado;
    }

    private synchronized boolean estaReiniciando() {
        return reiniciador;
    }

    // -------------------------------------------------------------------------
    private synchronized void cambiarNivel() {
        if (direccion == arriba) {
            actual = actual + 1;
        } else if (direccion == abajo){
            actual = actual - 1;
        }
    }

    private void espera(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            log("< Espera del hilo interrumpida > ");
        }
    }

    private synchronized void cambiarDireccion(int cambio) {
        if (direccion != cambio) {
            direccion = cambio;
            String texto = "neutro";

            if (cambio == arriba) {
                texto = "arriba";
            } else if (cambio == abajo) {
                texto = "abajo";
            }
            log("Cambiando de direccion >>> " + texto);
        }
    }

    private synchronized void terminarReinicio() {
        direccion = arriba;
        reiniciador = false;
        log("Reinicio terminado >>> Nivel 1 - Direccion arriba");
    }

    private void reinicio() {
        if (actual > 1) {
            cambiarDireccion(abajo);
            log("Reiniciando: Bajando del nivel " + actual + " al nivel " + (actual - 1));

            espera(tiempoNiveles);
            cambiarNivel();
            log("El elevador llego al nivel " + actual);
        } else {
            terminarReinicio();
        }
    }

    private void pausar() {
        espera(100);
    }

    private void detenerse() {
        log("Espera en el nivel " + actual);
        espera(tiempoEspera);
        apagarActual();
    }

    private void continuar() {
        if (direccion == abajo) {
            log("Elevador bajando hacia el nivel " + (actual - 1));
        } else {
            log("Elevador subiendo hacia el nivel " + (actual + 1));
        }
        espera(tiempoNiveles);
        cambiarNivel();
        log("El elevador llego al nivel " + actual);
    }

    private void cambiarSentido() {
        if (direccion == arriba) {
            cambiarDireccion(abajo);
        } else {
            cambiarDireccion(arriba);
        }
    }

    private void procesar() {
        if (direccion == neutro ){
            cambiarDireccion(arriba);
        }
        if (debeDetenerse()) {
            detenerse();
        }
        if (existeAdelante()) {
            continuar();
        } else if (existenSolicitudes())
            cambiarSentido();

    }

    public synchronized void reiniciar() {
        niveles.clear();
        solicitudesArriba.clear();
        solicitudesAbajo.clear();

        reiniciador = true;

        log("Reinicio, se eliminaron las solicitudes");
    }

    public synchronized void apagar() {
        estado = false;
        log("Elevador detenido");
    }
    @Override
    public void run() {
        while (estaActivo()) {
            if (estaReiniciando()) {
                reinicio();
            } else if (!existenSolicitudes()) {
                pausar();
            } else {
                procesar();
            }
        }
    }
}
