package trailex.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class SlotMachineSeriesGame extends JDialog {
    private static final long serialVersionUID = 1L;

    private JLabel[] slots;
    private JButton startButton = new JButton("Iniciar");
    private JButton stopButton = new JButton("Parar");

    private List<String> series = List.of("BreakingBad", "GameOfThrones", "StrangerThings", 
                                          "TheCrown", "TheOffice", "Friends");

    private Thread hilo;

    public SlotMachineSeriesGame() {
        JPanel slotPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        slots = new JLabel[3];

        for (int i = 0; i < 3; i++) {
            slots[i] = new JLabel();
            Random random = new Random();
            actualizarLabel(slots[i], series.get(random.nextInt(series.size())));
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
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
    }

    private void actualizarLabel(JLabel label, String serie) {
        String imagePath = "resources/images/" + serie.toLowerCase() + ".png";
        label.setIcon(new ImageIcon(imagePath));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
    }

    private void startGame() {
        hilo = new Thread(() -> {
            Random random = new Random();
            while (!Thread.currentThread().isInterrupted()) {
                for (JLabel label : slots) {
                    String serie = series.get(random.nextInt(series.size()));
                    actualizarLabel(label, serie);
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
            JOptionPane.showMessageDialog(this, "¡Felicidades! Disfruta tu nueva serie.", "Ganaste", JOptionPane.INFORMATION_MESSAGE);
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

