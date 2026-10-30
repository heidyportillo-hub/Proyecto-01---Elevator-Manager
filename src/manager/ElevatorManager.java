package manager;
import java.util.ArrayList;
import elevator.Elevator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ElevatorManager extends Thread{
  private ArrayList<Elevator> elevadores = new ArrayList<>(); 
  
  private int totalNiveles;
  private int tiempoNiveles;
  private int tiempoEspera;
  private int cantElevadores;

  public ElevatorManager(int totalNiveles, int tiempoNiveles, int tiempoEspera, int cantElevadores){
    this.totalNiveles = totalNiveles;
    this.tiempoNiveles = tiempoNiveles;
    this.tiempoEspera = tiempoEspera;
    this.cantElevadores = cantElevadores;
    
    for(int codigo=1; codigo<cantElevadores+1; codigo++){
      Elevator elevador = new Elevator(codigo, totalNiveles, tiempoNiveles, tiempoEspera);
      elevador.start();
      elevadores.add(elevador);
    } 
  }
  private ConcurrentLinkedQueue<Peticion> peticiones = new ConcurrentLinkedQueue<>();
  public void recibirPeticion(int piso, int sentido){
    if((1<piso && piso <totalNiveles) && (sentido == 1 || sentido == 2)){
      Peticion peticion = new Peticion(piso, sentido);
      peticiones.add(peticion);
    }
  }
  boolean activo = true;
  public void cambioActivo(){
    if(peticiones.isEmpty()){
      activo = false;
    }
  }
  while(activo){
    Peticion activa = peticion.poll(); //la peticion que se va a seguir
    if(activa == null){
      Thread.Sleep(100);
    }else{
      System.out.println("Piso: " + piso + " Sentido: " + sentido);
  }
  
}
