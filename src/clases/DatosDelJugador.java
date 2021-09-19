package clases;

public class DatosDelJugador {
    private final int intento;
    private String Nombre;
    private String Resolucion;
    private static int contador;
   
    
    public DatosDelJugador(){
        this.intento = DatosDelJugador.contador++;
    }
    public DatosDelJugador(String Nombre){
        this();
        this.Nombre = Nombre;
        
    }
    
    public void juegoNuevo(){
        contador++;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getResolucion() {
        return Resolucion;
    }

    public void setResolucion(String Resolucion) {
        this.Resolucion = Resolucion;
    }

    public int getIntento() {
        return intento;
    }

    

    @Override
    public String toString() {
        return "DatosDelJugador {" + "intento = " + intento + ", Nombre = " + Nombre + ", Resolucion = " + Resolucion + '}';
    }
    
    
}
