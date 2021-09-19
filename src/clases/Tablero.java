package clases;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Juan Carlos Choc Xol Ing. Sistemas 202041390
 */
public class Tablero {
    Posicion[][] posicion;

    private int NumeroFilas;
    private int NumeroColumnas;
    private int NumeroMinas;

    private int NumeroPosicionAbierta;
    private boolean JuegoTerminado;

    private Consumer<List<Posicion>> eventoJuegoPerdido;
    private Consumer<List<Posicion>> eventoJuegoGanado;
    private Consumer<List<Posicion>> eventoBotonesRotos;
    private Consumer<Posicion> eventoPosicionAbierta;

    public Tablero(int NumeroFilas, int NumeroColumnas, int NumeroMinas) {
        this.NumeroFilas = NumeroFilas;
        this.NumeroColumnas = NumeroColumnas;
        this.NumeroMinas = NumeroMinas;
        this.inicializarTablero();
    }

    public void inicializarTablero() {
        posicion = new Posicion[this.NumeroFilas][this.NumeroColumnas];
        for (int i = 0; i < posicion.length; i++) {
            for (int j = 0; j < posicion[i].length; j++) {
                posicion[i][j] = new Posicion(i, j);
            }
        }
        generarMinas();
    }

    private void generarMinas() {
        int minasGeneradas = 0;
        while (minasGeneradas != NumeroMinas) {
            int posTmpFila = (int) (Math.random() * posicion.length);
            int posTmpColumna = (int) (Math.random() * posicion[0].length);
            if (!posicion[posTmpFila][posTmpColumna].isMina()) {
                posicion[posTmpFila][posTmpColumna].setMina(true);
                minasGeneradas++;
            }
        }

        actualizarNumeroDeMinasAlrededor();
    }

    public void imprimirTablero() {
        for (int i = 0; i < posicion.length; i++) {
            for (int j = 0; j < posicion[i].length; j++) {
                System.out.print(posicion[i][j].isMina() ? " | * | " : " | 0 | ");
            }
            System.out.println("");
        }
        System.out.println("");

    }

    private void actualizarNumeroDeMinasAlrededor() {
        for (int i = 0; i < posicion.length; i++) {
            for (int j = 0; j < posicion[i].length; j++) {
                if (posicion[i][j].isMina()) {
                    List<Posicion> posicionAlrededor = obtenerPosicionAlrededor(i, j);
                    posicionAlrededor.forEach((c) -> c.incrementarNumeroDeMinasAlrededor());
                }
            }
        }
    }

    private List<Posicion> obtenerPosicionAlrededor(int Fila, int Columna) {
        List<Posicion> listaPosicion = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            int PosicionTemporalFila = Fila;
            int PosicionTemporalColumna = Columna;
            switch (i) {
                //Arriba
                case 0:
                    PosicionTemporalFila--;
                    break;

                //Arriba Derecha
                case 1:
                    PosicionTemporalFila--;
                    PosicionTemporalColumna++;
                    break;

                //Derecha
                case 2:
                    PosicionTemporalColumna++;
                    break;

                //Derecha Abajo
                case 3:
                    PosicionTemporalColumna++;
                    PosicionTemporalFila++;
                    break;

                //Abajo
                case 4:
                    PosicionTemporalFila++;
                    break;

                //Abajo izquierda
                case 5:
                    PosicionTemporalFila++;
                    PosicionTemporalColumna--;
                    break;

                //Izquierda
                case 6:
                    PosicionTemporalColumna--;
                    break;

                //Izquierda Arriba
                case 7:
                    PosicionTemporalFila--;
                    PosicionTemporalColumna--;
                    break;

            }

            if ((PosicionTemporalFila >= 0) && (PosicionTemporalFila < this.posicion.length)
                    && (PosicionTemporalColumna >= 0) && (PosicionTemporalColumna < this.posicion[0].length)) {
                listaPosicion.add(this.posicion[PosicionTemporalFila][PosicionTemporalColumna]);
            }
        }

        return listaPosicion;
    }

    List<Posicion> obtenerPosicionConMinas() {
        List<Posicion> posicionConMinas = new LinkedList<>();
        for (int i = 0; i < posicion.length; i++) {
            for (int j = 0; j < posicion[i].length; j++) {
                if (posicion[i][j].isMina()) {
                    posicionConMinas.add(posicion[i][j]);
                }
                
            }
        }
        return posicionConMinas;
    }
    List<Posicion> obtenerPosicionSinMinas(){
        List<Posicion> posicionSinMinas = new LinkedList<>();
        for (int i = 0; i < posicion.length; i++) {
            for (int j = 0; j < posicion[i].length; j++) {
                posicionSinMinas.add(posicion[i][j]);
            }
        }
        return posicionSinMinas;
    }

    public void seleccionarPosicion(int Fila, int Columna) {
        eventoPosicionAbierta.accept(this.posicion[Fila][Columna]);
        
        if (this.posicion[Fila][Columna].isMina()) {
            eventoJuegoPerdido.accept(obtenerPosicionConMinas());
            eventoBotonesRotos.accept(obtenerPosicionSinMinas());
            
            
            
        } else if (this.posicion[Fila][Columna].getNumeroDeMinasAlrededor() == 0) {
            
            marcarPosicionAbierta(Fila, Columna);
            
            List<Posicion> posicionAlrededor = obtenerPosicionAlrededor(Fila, Columna);
            
            for (Posicion posicion : posicionAlrededor) {
                if (!posicion.isAbierta()) {
                    seleccionarPosicion(posicion.getFila(), posicion.getColumna());
                }
            }
        } else {
            marcarPosicionAbierta(Fila, Columna);
        }
        if (partidaGanada()) {
            eventoJuegoGanado.accept(obtenerPosicionConMinas());
        }
    }

    void marcarPosicionAbierta(int Fila, int Columna) {
        if (!this.posicion[Fila][Columna].isAbierta()) {
            NumeroPosicionAbierta++;
            this.posicion[Fila][Columna].setAbierta(true);
        }
    }

    boolean partidaGanada() {
        return NumeroPosicionAbierta >= (NumeroFilas * NumeroColumnas) - NumeroMinas;
    }

    public void setEventoJuegoPerdido(Consumer<List<Posicion>> eventoJuegoPerdido) {
        this.eventoJuegoPerdido = eventoJuegoPerdido;
    }

    public void setEventoPosicionAbierta(Consumer<Posicion> eventoPosicionAbierta) {
        this.eventoPosicionAbierta = eventoPosicionAbierta;
    }

    public void setEventoJuegoGanado(Consumer<List<Posicion>> eventoJuegoGanado) {
        this.eventoJuegoGanado = eventoJuegoGanado;
    }

    public void setEventoBotonesRotos(Consumer<List<Posicion>> eventoBotonesRotos) {
        this.eventoBotonesRotos = eventoBotonesRotos;
    }
    
    


}
