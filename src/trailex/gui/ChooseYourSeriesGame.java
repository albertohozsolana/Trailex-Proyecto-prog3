package trailex.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class ChooseYourSeriesGame extends JDialog {
    private static final long serialVersionUID = 1L;

    private List<String> series = List.of("Breaking Bad", "Game of Thrones", "Stranger Things",
            "The Crown", "The Office", "Friends");
    private JButton[] buttons = new JButton[3];
    private JLabel resultLabel = new JLabel("Elige una caja para descubrir tu próxima serie", JLabel.CENTER);
    private JButton restartButton = new JButton("Reiniciar");

    public ChooseYourSeriesGame() {
        this.setTitle("Escoge tu próxima serie");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(600, 250); // Ventana apaisada
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        Random random = new Random();
        // Crear los 3 botones grandes
        for (int i = 0; i < 3; i++) {
            JButton button = new JButton("Caja " + (i + 1));
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))); // Color aleatorio
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.addActionListener(new ButtonClickListener());
            buttons[i] = button;
            buttonPanel.add(button);
        }

        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        this.add(resultLabel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);

        // Configurar botón de reiniciar
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setEnabled(false); // Inactivo hasta que se elija una caja
        restartButton.addActionListener(e -> resetGame());
        JPanel restartPanel = new JPanel();
        restartPanel.add(restartButton);
        this.add(restartPanel, BorderLayout.SOUTH);
    }

    // Clase para manejar el clic en los botones
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();

            // Seleccionar una serie al azar
            Random random = new Random();
            String chosenSeries = series.get(random.nextInt(series.size()));

            // Mostrar el nombre de la serie
            resultLabel.setText("¡Te toca ver: " + chosenSeries + "!");

            // Deshabilitar los botones después de la elección
            for (JButton button : buttons) {
                button.setEnabled(false);
            }

            // Activar el botón de reiniciar
            restartButton.setEnabled(true);
        }
    }

    // Reiniciar el juego
    private void resetGame() {
        Random random = new Random();
        resultLabel.setText("Elige una caja para descubrir tu próxima serie");

        // Restaurar los botones
        for (JButton button : buttons) {
            button.setEnabled(true);
            button.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))); // Nuevo color aleatorio
        }

        // Desactivar el botón de reiniciar
        restartButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChooseYourSeriesGame game = new ChooseYourSeriesGame();
            game.setVisible(true);
        });
    }
}



