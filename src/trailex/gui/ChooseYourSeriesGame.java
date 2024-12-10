package trailex.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import trailex.domain.Serie;
import trailex.persistence.GestorBD;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class ChooseYourSeriesGame extends JDialog {
    private static final long serialVersionUID = 1L;

    private List<String> series;
    private JButton[] buttons = new JButton[3];
    private JLabel resultLabel = new JLabel("Elige una caja para descubrir tu próxima serie", JLabel.CENTER);
    private JButton restartButton = new JButton("Reiniciar");

    public ChooseYourSeriesGame() {
    	GestorBD gestorBD = new GestorBD(); // Crear instancia de GestorBD
        series = gestorBD.cargarTitulosSeries(); // Cargar rutas de fotos desde la base de datos

        if (series.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron series en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Cierra la ventana si no hay datos
            return;
        }
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
    
    public void mostrarInfoSerie(Serie serie) {
    	JButton btnFavoritos = new JButton("Añadir a Favoritos");
	    btnFavoritos.setBackground(Trailex_Principal.turquesa);
	    

	    ImageIcon originalIcon = new ImageIcon(serie.getRutaFoto());
	    int imageWidth = originalIcon.getIconWidth();
	    int imageHeight = originalIcon.getIconHeight();
	    double aspectRatio = (double) imageWidth / imageHeight;

	    int windowWidth = 400;
	    int windowHeight = (int) (windowWidth / aspectRatio);

	    JFrame ventanaInfoSerie = new JFrame("Información de la Serie");
	    ventanaInfoSerie.setSize(windowWidth, windowHeight);
	    ventanaInfoSerie.setLayout(new BorderLayout());

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = screenSize.width - windowWidth - 50;
	    int y = (screenSize.height - windowHeight) / 2;
	    ventanaInfoSerie.setLocation(x, y);

	    Image image = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
	    JLabel background = new JLabel(new ImageIcon(image));
	    background.setLayout(new BorderLayout());
	    ventanaInfoSerie.setContentPane(background);

	    // Panel oscuro para el texto
	    
	    JPanel panelDetalles = new JPanel();
	    
	    JPanel textPanel = new JPanel();
	    textPanel.setLayout(new BorderLayout());
	    textPanel.setBackground(new Color(0, 0, 0, 150)); // Fondo negro semi-transparente
	    textPanel.setOpaque(true);

	    JLabel labelTitulo = new JLabel(serie.getTitulo(), SwingConstants.CENTER);
	    labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
	    labelTitulo.setForeground(Color.WHITE);
	    labelTitulo.setBorder(new EmptyBorder(10, 10, 10, 10));

	    JLabel labelEdadRecomendada = new JLabel("Edad recomendada: " + serie.getEdadRecomendada() + " años", SwingConstants.CENTER);
	    labelEdadRecomendada.setFont(new Font("Arial", Font.PLAIN, 18));
	    labelEdadRecomendada.setForeground(Color.WHITE);
	    panelDetalles.setOpaque(false);
	    panelDetalles.setLayout(new GridLayout(5, 1, 5, 5));
	    JLabel labelAnio = new JLabel("Año: " + serie.getAnio());
	    JLabel labelGenero = new JLabel("Género: " + serie.getGenero());
	    JLabel labelProtagonista = new JLabel("Protagonista: " + serie.getProtagonista());
	    JLabel labelTemporadas = new JLabel("Temporadas: " + serie.getNumeroTemporadas());
	    labelAnio.setFont(new Font("Arial", Font.PLAIN, 16));
	    labelGenero.setFont(new Font("Arial", Font.PLAIN, 16));
	    labelProtagonista.setFont(new Font("Arial", Font.PLAIN, 16));
	    labelTemporadas.setFont(new Font("Arial", Font.PLAIN, 16));
	    labelAnio.setForeground(Color.WHITE);
	    labelGenero.setForeground(Color.WHITE);
	    labelProtagonista.setForeground(Color.WHITE);
	    labelTemporadas.setForeground(Color.WHITE);
	    panelDetalles.add(labelAnio);
	    panelDetalles.add(labelGenero);
	    panelDetalles.add(labelProtagonista);
	    panelDetalles.add(labelTemporadas);
	    

	    // Añadir los componentes al panel oscuro
	    textPanel.add(labelTitulo, BorderLayout.NORTH);
	    textPanel.add(labelEdadRecomendada, BorderLayout.CENTER);
	    textPanel.add(panelDetalles, BorderLayout.SOUTH);
	

	    // Añadir el panel oscuro a la ventana
	    ventanaInfoSerie.add(textPanel, BorderLayout.SOUTH);
	    ventanaInfoSerie.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    ventanaInfoSerie.setVisible(true);
    	
    	
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
            GestorBD gestorBD = new GestorBD();
            Serie chosenSerie = gestorBD.getSeriePorTitulo(chosenSeries);
            mostrarInfoSerie(chosenSerie);

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



