package trailex.gui;

import trailex.domain.Serie;
import trailex.persistence.GestorBD;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SlotMachineSeriesGame extends JDialog {
    private static final long serialVersionUID = 1L;

    private JLabel[] slots;
    private JButton startButton = new JButton("Iniciar");
    private JButton stopButton = new JButton("Parar");
    private JComboBox<String> genreSelector;

    private Thread hilo;
    private List<Serie> series;
    private List<Serie> seriesFiltradas;

    public SlotMachineSeriesGame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.5); // 50% del ancho de la pantalla
        int height = (int) (screenSize.height * 0.5); // 50% del alto de la pantalla
        this.setSize(width, height);

        GestorBD gestorBD = new GestorBD(); // Crear instancia de GestorBD
        series = gestorBD.cargarSeries(); // Cargar series desde la base de datos

        if (series.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron series en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Cierra la ventana si no hay datos
            return;
        }

        seriesFiltradas = series; // Inicialmente, todas las series están disponibles

        JPanel slotPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        slots = new JLabel[3];

        for (int i = 0; i < 3; i++) {
            slots[i] = new JLabel();
            Random random = new Random();
            actualizarLabel(slots[i], seriesFiltradas.get(random.nextInt(seriesFiltradas.size())).getRutaFoto());

            int width1 = 90; // Ancho base
            int height1 = 160; // Alto calculado (proporción 9:16)
            slots[i].setPreferredSize(new Dimension(width1, height1));

            slots[i].setHorizontalAlignment(SwingConstants.CENTER);
            slots[i].setVerticalAlignment(SwingConstants.CENTER);

            slotPanel.add(slots[i]);
        }

        genreSelector = new JComboBox<>(new String[]{"Todos", "Comedia", "Romance", "Aventura", "Drama", "Ciencia Ficción", "Terror"});
        genreSelector.addActionListener(e -> filtrarSeriesPorGenero((String) genreSelector.getSelectedItem()));

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
        buttonPanel.add(genreSelector);

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
        if (hilo != null && hilo.isAlive()) {
            hilo.interrupt(); // Interrumpe cualquier hilo en ejecución por seguridad
        }

        // Crear un nuevo hilo para la animación
        hilo = new Thread(() -> {
            Random random = new Random();
            while (!Thread.currentThread().isInterrupted()) {
                for (JLabel label : slots) {
                    String rutaFoto = seriesFiltradas.get(random.nextInt(seriesFiltradas.size())).getRutaFoto();
                    actualizarLabel(label, rutaFoto);
                }
                try {
                    Thread.sleep(100); // Control de velocidad de animación
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // Asegura la interrupción del hilo
                }
            }
        });
        hilo.start(); // Iniciar el hilo
    }

    private void stopGame() {
        if (hilo != null) {
            hilo.interrupt(); // Detiene el hilo actual
            try {
                hilo.join(); // Espera a que el hilo termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Revisar si hay coincidencias en los slots
        if (checkWin()) {
            Serie serieGanadora = seriesFiltradas.stream()
                    .filter(serie -> slots[0].getIcon().toString().contains(serie.getRutaFoto()))
                    .findFirst()
                    .orElse(null);

            if (serieGanadora != null) {
                Trailex_Principal.mostrarInfoSerie(serieGanadora); // Muestra la información de la serie ganadora
            } else {
                JOptionPane.showMessageDialog(this, "¡OLE! Ya sabes qué serie ver ;)", "Disfruta", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "¡Inténtalo de nuevo para elegir una serie!", "Sigue intentando", JOptionPane.INFORMATION_MESSAGE);
        }

        // Reactivar botones para permitir nuevas interacciones
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
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

    private void filtrarSeriesPorGenero(String genero) {
        if ("Todos".equals(genero)) {
            seriesFiltradas = series;
        } else {
            seriesFiltradas = series.stream()
                    .filter(serie -> genero.equals(serie.getGenero()))
                    .collect(Collectors.toList());
        }

        if (seriesFiltradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay series disponibles para este género.", "Error", JOptionPane.ERROR_MESSAGE);
            genreSelector.setSelectedItem("Todos");
            seriesFiltradas = series;
        }
    }
}



