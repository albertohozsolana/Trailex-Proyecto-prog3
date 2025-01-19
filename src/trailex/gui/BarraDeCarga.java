
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
import javax.swing.Timer;

//IAG
//ADAPTADO (hubo que hacer un montón de cambios y probar distintas cosas hasta que funcionase)

public class BarraDeCarga extends JFrame {
    private JProgressBar progressBar;
    private final Trailex_Principal ventanaPrincipal;
    private Runnable onLoadingComplete;
    private static final int MAX_VALUE = 100;
    private volatile boolean initializationComplete = false;

    public BarraDeCarga(Trailex_Principal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        inicializarVentana();
    }

    public void addLoadingCompleteListener(Runnable listener) {
        this.onLoadingComplete = listener;
    }

    private void inicializarTrailex() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                ventanaPrincipal.Iniciar_Trailex();
                initializationComplete = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public void startLoading() {
        // Iniciar la inicialización en segundo plano
        inicializarTrailex();
        
        // Hilo para la animación de la barra
        Thread progressThread = new Thread(() -> {
            try {
                int progress = 0;
                while (progress < MAX_VALUE) {
                    Thread.sleep(45);
                    
                    // Si estamos entre 20 y 50, empezaremos a inicializar y a cargar los datos, aquí se trabará un poco
                    if (progress >= 20 && progress <= 50) {
                        if (!initializationComplete) {
                            // avanzamos lentamente mientras se inicializa
                            if (Math.random() < 0.3) { //Ponemos para que avance a veces y así van cargando los datos aunque se trabe un poco
                                progress++;
                            }
                        } else {
                            //cuando hayamos acabado de cargar, vamos aún más rápido
                            progress += 2;
                        }
                    } else {
                        // sino avanzamos normal de 1 en 1
                        progress++;
                    }
                    
                    final int currentProgress = Math.min(progress, MAX_VALUE);
                    SwingUtilities.invokeLater(() -> updateProgressBar(currentProgress));
                    
                    // Si llegamos al 60% y la inicialización no está completa, esperar
                    if (progress >= 60 && !initializationComplete) {
                        while (!initializationComplete) {
                            Thread.sleep(100);
                        }
                    }
                }
                
                // Completar la carga
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    if (onLoadingComplete != null) {
                        onLoadingComplete.run();
                    }
                    ventanaPrincipal.mostrarVentana();
                });
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        progressThread.start();
    }

    private void updateProgressBar(final int value) {
        String message = value < 20 ? "Iniciando..." :
                        value < 50 ? "Cargando datos..." :
                        "Mostrando Trailex...";
        progressBar.setValue(value);
        progressBar.setString(value + "% " + message);
    }

    private void inicializarVentana() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 90);
        setTitle("Cargando Trailex");
        setLocationRelativeTo(null);
        setUndecorated(true);

        progressBar = new JProgressBar(0, MAX_VALUE);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Trailex_Principal.turquesa);
        progressBar.setBackground(Color.BLACK);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        progressPanel.setBackground(Color.BLACK);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        add(progressPanel, BorderLayout.CENTER);
    }
}