package clases;

// @author Juan Carlos Choc Xol Ing.Sistemas 202041390
public class Posicion {
    private int Fila; 
    private int Columna;
    private int NumeroDeMinasAlrededor;
    private boolean Mina;
    private boolean Abierta;
    
    
    public Posicion(){
        
    }

    public Posicion(int Fila, int Columna) {
        this.Fila = Fila;
        this.Columna = Columna;
    }
    

    public int getFila() {
        return Fila;
    }

    public void setFila(int Fila) {
        this.Fila = Fila;
    }

    public int getColumna() {
        return Columna;
    }

    public void setColumna(int Columna) {
        this.Columna = Columna;
    }

    public boolean isMina() {
        return Mina;
    }

    public void setMina(boolean Mina) {
        this.Mina = Mina;
    }

    public int getNumeroDeMinasAlrededor() {
        return NumeroDeMinasAlrededor;
    }

    public void setNumeroDeMinasAlrededor(int NumeroDeMinasAlrededor) {
        this.NumeroDeMinasAlrededor = NumeroDeMinasAlrededor;
    }
    
    public void incrementarNumeroDeMinasAlrededor(){
        this.NumeroDeMinasAlrededor++;
    }

    public boolean isAbierta() {
        return Abierta;
    }

    public void setAbierta(boolean Abierta) {
        this.Abierta = Abierta;
    }
    
}
