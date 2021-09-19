package general;

import clases.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 *
 * @author Juan Carlos Choc Xol Ing.Sistemas 202041390
 */
public class Inicio extends javax.swing.JFrame {

    public static int NumeroDeFilas = 16;
    public static int NumeroDeColumnas = 16;
    public static int NumeroDeMinas = 40;
    public static String Nombre;

    JButton[][] botonesDelTablero;
    Tablero tablero;
    DatosDelJugador datos;

    /**
     * Creates new form Inicio
     */
    public Inicio() {
        //Generealizar el programa
        initComponents();
        generarTablero();
        inicializar();
        instrucciones();
        //Cambiar de icono al JFrame
        setIconImage(new ImageIcon(getClass().getResource("/img/icono.png")).getImage());
    }

    //El usuario Digite su nombre o usuario para el juego
    private void inicializar() {
        Nombre = JOptionPane.showInputDialog(rootPane, "INGRESE SU NOMBRE");
        datos = new DatosDelJugador(Nombre);
        txtNombreJugador.setText("Jugador " + datos.getNombre());
        JOptionPane.showMessageDialog(rootPane, "Bienvenido " + datos.getNombre());
    }

    private void instrucciones() {
        JOptionPane.showMessageDialog(rootPane, "PARA INCIALIZAR EL JUEGO USTED DEBERA \n"
                + "INGRESAR AL MENU E INICAR EL JUEGO \n"
                + "DE LO CONTRORIO LA TABLA QUE CONTIENE LOS BOTONES ESTARA BLOQUEADO \n"
                + "Y PARA CADA MODO DE JUEGO USTED VOLVERA A INICIAR LA PARTIDA DE NUEVO \n");
    }

    //Se modifica la cantidad de botones del tablero para diferentes niveles
    private void modificarBotones() {
        if (botonesDelTablero != null) {
            for (int i = 0; i < botonesDelTablero.length; i++) {
                for (int j = 0; j < botonesDelTablero[i].length; j++) {
                    if (botonesDelTablero[i][j] != null) {
                        getContentPane().remove(botonesDelTablero[i][j]);
                    }
                }
            }
        }
    }

    //CARGA EL JUEGO
    private void juegoNuevo() {
        modificarBotones();
        generarBotones();
        crearTableroDeBuscaminas();
        repaint();
        datos = new DatosDelJugador(Nombre);
    }

    //Genera los evenentos del Juego
    //Evento Perdido, Evento Ganado, Evento de seguir jugando(Posicion Abierta)
    private void crearTableroDeBuscaminas() {
        //Inicializa el tablero
        tablero = new Tablero(NumeroDeFilas, NumeroDeColumnas, NumeroDeMinas);
        
        //Evento de las posiciones Abiertas
        tablero.setEventoPosicionAbierta(new Consumer<Posicion>() {
            @Override
            public void accept(Posicion t) {
                // botonesTablero[t.getFila()][t.getColumna()].setEnabled(false);
                botonesDelTablero[t.getFila()][t.getColumna()]
                        .setText(t.getNumeroDeMinasAlrededor() == 0 ? "" : t.getNumeroDeMinasAlrededor() + "");
                botonesDelTablero[t.getFila()][t.getColumna()].setBackground(Color.green);
                botonesDelTablero[t.getFila()][t.getColumna()].setBorder(BorderFactory.createLineBorder(Color.green, 3));

            }
        });

        //Evento Perdido Bloquea y Muestra botones con las minas
        tablero.setEventoJuegoPerdido(new Consumer<List<Posicion>>() {
            @Override
            public void accept(List<Posicion> t) {
                datos.setResolucion(" Juego Perdido");
                for (Posicion posicionConMina : t) {
                    botonesDelTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setText("✖");
                    botonesDelTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setForeground(Color.red);
                    //ImageIcon mina = new ImageIcon("mina.png");
                    //botonesTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setIcon(new ImageIcon(mina.getImage().getScaledInstance(botonesTablero[posicionConMina.getFila()][posicionConMina.getColumna()].getWidth(), botonesTablero[posicionConMina.getFila()][posicionConMina.getColumna()].getHeight(), Image.SCALE_SMOOTH)));
                    //botonesTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setEnabled(false);
                    botonesDelTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setBorder(BorderFactory.createLineBorder(Color.red, 3));
                }
            }
        });

        //Evento Botones rotos, bloquea todos los botones 
        tablero.setEventoBotonesRotos(new Consumer<List<Posicion>>() {
            @Override
            public void accept(List<Posicion> t) {
                for (Posicion posicionSinMinas : t) {
                    botonesDelTablero[posicionSinMinas.getFila()][posicionSinMinas.getColumna()].setEnabled(false);
                }
                JOptionPane.showMessageDialog(rootPane, "JUEGO PERDIDO");
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Quiere volver a intentarlo?");
                if (confirmacion == JOptionPane.YES_OPTION) {
                    juegoNuevo();
                } else {

                }

            }
        });

        //Evento Ganado
        tablero.setEventoJuegoGanado(new Consumer<List<Posicion>>() {
            @Override
            public void accept(List<Posicion> t) {
                datos.setResolucion("Juego Ganado");
                for (Posicion posicionConMina : t) {
                    botonesDelTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setText("🏴");
                    botonesDelTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setBorder(BorderFactory.createLineBorder(Color.red, 3));
                    botonesDelTablero[posicionConMina.getFila()][posicionConMina.getColumna()].setEnabled(false);
                }
                JOptionPane.showMessageDialog(rootPane, "Tú Acabas De Ganar");
            }
        });

        tablero.imprimirTablero();
    }

    //Genera Tablero de Inicio
    private void generarTablero() {
        int PosicionEnReferenciaX = 25;
        int PosicionEnReferenciaY = 40;
        int AnchoDelBoton = 30;
        int AltoDelBoton = 30;
//        JButton boton = new JButton();
//        boton.setBounds(100, 100, 100, 40);
//        boton.setBackground(Color.red);
//        ImageIcon mina = new ImageIcon("/img/mina.png");
//        boton.setIcon(new ImageIcon(mina.getImage().getScaledInstance(boton.getWidth(), boton.getHeight(), Image.SCALE_SMOOTH)));
//        getContentPane().add(boton);

        botonesDelTablero = new JButton[NumeroDeFilas][NumeroDeColumnas];

        for (int i = 0; i < botonesDelTablero.length; i++) {
            for (int j = 0; j < botonesDelTablero[i].length; j++) {
                botonesDelTablero[i][j] = new JButton();
                botonesDelTablero[i][j].setName("" + i + "," + j + "");
                botonesDelTablero[i][j].setBorder(new LineBorder(Color.BLACK));
                botonesDelTablero[i][j].setEnabled(false);
                if (i == 0 && j == 0) {
                    botonesDelTablero[i][j].setBounds(PosicionEnReferenciaX,
                            PosicionEnReferenciaY, AnchoDelBoton, AltoDelBoton);
                } else if (i == 0 && j != 0) {
                    botonesDelTablero[i][j].setBounds(
                            botonesDelTablero[i][j - 1].getX() + botonesDelTablero[i][j - 1].getWidth(),
                            PosicionEnReferenciaY, AnchoDelBoton, AltoDelBoton);
                } else {
                    botonesDelTablero[i][j].setBounds(
                            botonesDelTablero[i - 1][j].getX(),
                            botonesDelTablero[i - 1][j].getY() + botonesDelTablero[i - 1][j].getHeight(),
                            AnchoDelBoton, AltoDelBoton);
                }

                getContentPane().add(botonesDelTablero[i][j]);
            }
        }
        this.setSize(botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getX()
                + botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getWidth() + 30,
                botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getY()
                + botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getHeight() + 70
        );
    }

    //GENERA LOS BOTONES CON MINAS AL TABLERO
    private void generarBotones() {
        int PosicionEnReferenciaX = 25;
        int PosicionEnReferenciaY = 40;
        int AnchoDelBoton = 30;
        int AltoDelBoton = 30;

        botonesDelTablero = new JButton[NumeroDeFilas][NumeroDeColumnas];

        for (int i = 0; i < botonesDelTablero.length; i++) {
            for (int j = 0; j < botonesDelTablero[i].length; j++) {
                botonesDelTablero[i][j] = new JButton();
                botonesDelTablero[i][j].setName("" + i + "," + j + "");
                botonesDelTablero[i][j].setBorder(new LineBorder(Color.BLACK));

                if (i == 0 && j == 0) {
                    botonesDelTablero[i][j].setBounds(PosicionEnReferenciaX,
                            PosicionEnReferenciaY, AnchoDelBoton, AltoDelBoton);
                } else if (i == 0 && j != 0) {
                    botonesDelTablero[i][j].setBounds(
                            botonesDelTablero[i][j - 1].getX() + botonesDelTablero[i][j - 1].getWidth(),
                            PosicionEnReferenciaY, AnchoDelBoton, AltoDelBoton);
                } else {
                    botonesDelTablero[i][j].setBounds(
                            botonesDelTablero[i - 1][j].getX(),
                            botonesDelTablero[i - 1][j].getY() + botonesDelTablero[i - 1][j].getHeight(),
                            AnchoDelBoton, AltoDelBoton);
                }
                botonesDelTablero[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnClick(e);
                    }

                });
                // jPanel1.add(botonesTablero[i][j]);
                getContentPane().add(botonesDelTablero[i][j]);
            }
        }
        this.setSize(botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getX()
                + botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getWidth() + 30,
                botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getY()
                + botonesDelTablero[NumeroDeFilas - 1][NumeroDeColumnas - 1].getHeight() + 70
        );
    }

    //Evento para cuando se le de un click a un boton del tablero
    private void btnClick(ActionEvent e) {
        JButton boton = (JButton) e.getSource();
        String[] coordenada = boton.getName().split(",");
        int Fila = Integer.parseInt(coordenada[0]);
        int Columna = Integer.parseInt(coordenada[1]);
        tablero.seleccionarPosicion(Fila, Columna);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        txtNombreJugador = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuJuego = new javax.swing.JMenu();
        menuIniciarJuego = new javax.swing.JMenuItem();
        menuPrincipiante = new javax.swing.JMenuItem();
        menuIntermedio = new javax.swing.JMenuItem();
        menuNumMinas = new javax.swing.JMenuItem();
        menuPerfil = new javax.swing.JMenu();
        menuResultado = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BUSCAMINAS");
        setBackground(new java.awt.Color(255, 204, 204));
        setFont(new java.awt.Font("Alien League", 1, 10)); // NOI18N
        setResizable(false);

        txtNombreJugador.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        txtNombreJugador.setText("Jugador");

        menuJuego.setText("Juego");

        menuIniciarJuego.setText("Iniciar Partida ");
        menuIniciarJuego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIniciarJuegoActionPerformed(evt);
            }
        });
        menuJuego.add(menuIniciarJuego);

        menuPrincipiante.setText("Principiante");
        menuPrincipiante.setToolTipText("");
        menuPrincipiante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrincipianteActionPerformed(evt);
            }
        });
        menuJuego.add(menuPrincipiante);

        menuIntermedio.setText("Intermedio");
        menuIntermedio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIntermedioActionPerformed(evt);
            }
        });
        menuJuego.add(menuIntermedio);

        menuNumMinas.setText("Numero De Minas");
        menuNumMinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNumMinasActionPerformed(evt);
            }
        });
        menuJuego.add(menuNumMinas);

        jMenuBar1.add(menuJuego);

        menuPerfil.setText("Perfil");

        menuResultado.setText("Resultado");
        menuResultado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuResultadoActionPerformed(evt);
            }
        });
        menuPerfil.add(menuResultado);

        jMenuBar1.add(menuPerfil);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNombreJugador)
                .addContainerGap(403, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNombreJugador)
                .addContainerGap(728, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuIniciarJuegoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIniciarJuegoActionPerformed
        // TODO add your handling code here:
        juegoNuevo();

    }//GEN-LAST:event_menuIniciarJuegoActionPerformed

    private void menuPrincipianteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrincipianteActionPerformed
        // TODO add your handling code here:
        this.NumeroDeFilas = 8;
        this.NumeroDeColumnas = 8;
        this.NumeroDeMinas = 10;
        //juegoNuevo();
    }//GEN-LAST:event_menuPrincipianteActionPerformed

    private void menuNumMinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNumMinasActionPerformed
        // TODO add your handling code here:
        int NumeroDeNuevasMinas = Integer.parseInt(JOptionPane.showInputDialog("Digite número de Minas"));
        this.NumeroDeMinas = NumeroDeNuevasMinas;
        //juegoNuevo();
    }//GEN-LAST:event_menuNumMinasActionPerformed

    private void menuIntermedioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIntermedioActionPerformed
        // TODO add your handling code here:
        this.NumeroDeFilas = 16;
        this.NumeroDeColumnas = 16;
        this.NumeroDeMinas = 40;
        //juegoNuevo();
    }//GEN-LAST:event_menuIntermedioActionPerformed

    private void menuResultadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuResultadoActionPerformed
        // TODO add your handling code here:

        JOptionPane.showMessageDialog(null, datos.toString());
    }//GEN-LAST:event_menuResultadoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    //javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem menuIniciarJuego;
    private javax.swing.JMenuItem menuIntermedio;
    private javax.swing.JMenu menuJuego;
    private javax.swing.JMenuItem menuNumMinas;
    private javax.swing.JMenu menuPerfil;
    private javax.swing.JMenuItem menuPrincipiante;
    private javax.swing.JMenuItem menuResultado;
    private javax.swing.JLabel txtNombreJugador;
    // End of variables declaration//GEN-END:variables

}
