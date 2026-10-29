package manager;
import java.util.ArrayList;
import elevator.Elevator;

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
}
