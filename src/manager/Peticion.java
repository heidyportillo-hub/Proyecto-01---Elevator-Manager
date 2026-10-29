packege manager;  
public class Peticion{
  private int sentido;
  private int piso; 

  public peticiones(int piso, int sentido){
    this.piso = piso;
    this.sentido = sentido;
  }

  public int getSentido(){
    return this.sentido;
  }
  public int getPiso(){
    return this.piso;
  }
}
