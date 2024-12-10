package trailex.gui;

import trailex.persistence.GestorBD;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlotMachineSeriesGame extends JDialog {
    private static final long serialVersionUID = 1L;


    private JLabel[] slots;
    private JButton startButton = new JButton("Iniciar");
    private JButton stopButton = new JButton("Parar");

    private Thread hilo;
    private List<String> rutasFotos;

    public SlotMachineSeriesGame() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int width = (int) (screenSize.width * 0.5);  // 50% del ancho de la pantalla
    	int height = (int) (screenSize.height * 0.5); // 50% del alto de la pantalla
    	this.setSize(width, height);

    	GestorBD gestorBD = new GestorBD(); // Crear instancia de GestorBD
        rutasFotos = gestorBD.cargarRutasFotos(); // Cargar rutas de fotos desde la base de datos

        if (rutasFotos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron series en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Cierra la ventana si no hay datos
            return;
        }

    
        JPanel slotPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        slots = new JLabel[3];

        for (int i = 0; i < 3; i++) {
            slots[i] = new JLabel();
            Random random = new Random();
            actualizarLabel(slots[i], rutasFotos.get(random.nextInt(rutasFotos.size())));
            slotPanel.add(slots[i]);
            
         // Ajustar la relación de aspecto 9:16
            int width1 = 90; // Ancho base
            int height1 = 160; // Alto calculado (proporción 9:16)
            slots[i].setPreferredSize(new Dimension(width1, height1));

            // Centramos la imagen dentro del JLabel
            slots[i].setHorizontalAlignment(SwingConstants.CENTER);
            slots[i].setVerticalAlignment(SwingConstants.CENTER);

            // Añadir al panel
            slotPanel.add(slots[i]);
        }

        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            startGame();
        });

        stopButton.addActionListener(e -> {
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            stopGame();
        });

        stopButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        this.setLayout(new BorderLayout());
        this.add(slotPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setTitle("Elige tu próxima serie");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    private void actualizarLabel(JLabel label, String rutaFoto) {
    	ImageIcon icon = new ImageIcon(rutaFoto);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(235, 380, Image.SCALE_SMOOTH); // Escalar a 9:16
        label.setIcon(new ImageIcon(scaledImg));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
    }

    private void startGame() {
        hilo = new Thread(() -> {
            Random random = new Random();
            while (!Thread.currentThread().isInterrupted()) {
                for (JLabel label : slots) {
					String rutaFoto = rutasFotos.get(random.nextInt(rutasFotos.size())); // Seleccionar ruta aleatoria
                    actualizarLabel(label, rutaFoto);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        hilo.start();
    }

    private void stopGame() {
        if (hilo != null) {
            hilo.interrupt();
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (checkWin()) {
            JOptionPane.showMessageDialog(this, "¡OLE! Ya sabes que serie ver ;)", "Disfruta!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "¡Inténtalo de nuevo para elegir una serie!", "Sigue intentando", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean checkWin() {
        Icon firstIcon = slots[0].getIcon();
        for (int i = 1; i < slots.length; i++) {
            if (!slots[i].getIcon().equals(firstIcon)) {
                return false;
            }
        }
        return true;
    }

}

