package manager;
import java.util.ArrayList;

public class ElevadorManager extends Thread{
  ArrayList<Integer> peticiones = new ArrayList<>(); 
  
  private int totalNiveles;
  private int codigo;
  private int tiempoNiveles;
  private int tiempoEspera;

  public ElevadorManager(int totalNiveles, int codigo, int tiempoNiveles, int tiempoEspera){
    this.totalNiveles = totalNiveles;
    this.codigo = codigo;
    this.tiempoNiveles = tiempoNiveles;
    this.tiempoEspera = tiempoEspera;
    
    Elevador(codigo, totalNiveles, tiempoNiveles, tiempoEspera);
    
    
  }
}
