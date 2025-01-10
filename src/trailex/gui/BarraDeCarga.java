package trailex.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class BarraDeCarga extends JFrame {
    private JProgressBar progressBar;
    private final Trailex_Principal ventanaPrincipal;
    private Runnable onLoadingComplete;
    private static final int MAX_VALUE = 100;

    public BarraDeCarga(Trailex_Principal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        inicializarVentana();
    }

    public void addLoadingCompleteListener(Runnable listener) {
        this.onLoadingComplete = listener;
    }

    public void startLoading() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= MAX_VALUE; i++) {
                    final int progress = i;
                    
                    Thread.sleep(45);
                  
                    
                    SwingUtilities.invokeLater(() -> updateProgressBar(progress));
                    
                    if (progress == 2) {
                    	//cuando el progress vaya en 2 inicializaremos trailex, pero como es algo que requiere mucha carga
                    	//necesitamos hacer un hilo separado para que no bloquee mi progressbar al querer cargar
                    	inicializarTrailex();
                    }
                }
                
                // Una vez completada la carga, notificar y cerrar
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    if (onLoadingComplete != null) {
                        onLoadingComplete.run();
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    private void inicializarTrailex() {
        // Inicializar la ventana en segundo plano
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                ventanaPrincipal.Iniciar_Trailex();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void updateProgressBar(final int value) {
        progressBar.setValue(value);
        progressBar.setString(value + "%");
    }

    private void inicializarVentana() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 90);
        setTitle("Cargando Trailex");
        setLocationRelativeTo(null);
        setUndecorated(true);

       System.out.println("Cargando...");

        progressBar = new JProgressBar(0, MAX_VALUE);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Trailex_Principal.turquesa); // Turquesa
        progressBar.setBackground(Color.BLACK);

        add(progressBar, BorderLayout.CENTER);

    }
}