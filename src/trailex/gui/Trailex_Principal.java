package trailex.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import trailex.elementos.Pelicula;
import trailex.elementos.Serie;
import trailex.elementos.Videoclub;


public class Trailex_Principal extends JFrame{
	private JPanel panel_central;
	private JPanel panel_principal;
	private JPanel panel_arriba;
	private JButton boton_menu, boton_pelis, boton_series;
	private Color turquesa=new Color(0x5FA6AD);
	
	private static final long serialVersionUID = 1L;

	public Trailex_Principal() {
		// Llamamos a la función que crea la ventana Básica de nuestro programa
		Iniciar_Trailex();
		cargarSeries();
	}
	
	private void Iniciar_Trailex() {
		// Creamos el panel con su disposición (BorderLayout) donde se mostrará la app
				panel_principal = new JPanel(new BorderLayout());
				panel_principal.setBackground(Color.black);

				// Creamos el panel que se encontrará en la parte superior del programa
				panel_arriba = new JPanel(new FlowLayout(50, 50, 50));
				panel_arriba.setBackground(Color.black);

				
				// Creamos y añadimos el botón de Menú al panel de arriba:
				boton_menu = new JButton();
				boton_menu.setPreferredSize(new Dimension(30,30));
				
				ImageIcon icon_menu= new ImageIcon("img/menu.png");
				Image img_menu=icon_menu.getImage().getScaledInstance(17, 17, Image.SCALE_SMOOTH);
				boton_menu.setIcon(new ImageIcon(img_menu));
				boton_menu.setBackground(turquesa);
				boton_menu.setToolTipText("Menú");
				
				boton_pelis = new JButton("Películas");
				boton_pelis.setBackground(turquesa);
				
				boton_series = new JButton("Series");
				boton_series.setBackground(turquesa);
				
				panel_arriba.add(boton_menu);
				panel_arriba.add(boton_pelis);
				panel_arriba.add(boton_series);
				
				
				// Creamos y añadimos un Desplegable a la parte de arriba
				String[] generos = {"Comedia", "Terror", "Romantica", "Ciencia Ficción","Aventura","Drama"};
				JComboBox<String> lista_desplegable = new JComboBox<String>(generos);	
				lista_desplegable.setToolTipText("Selecciona un género");
				lista_desplegable.setBackground(new Color(0x5FA6AD));
				panel_arriba.add(lista_desplegable);
				
				// Creamos y añadimos una Barra de Búsqueda NO FUNCIONA, a la parte de arriba
				JTextField barra_buscar = new JTextField();
				panel_arriba.add(barra_buscar);
				
				
				// Añadimos a la pantalla principal la parte de arriba y le decimos que queremos que esté arriba
				panel_principal.add(panel_arriba, BorderLayout.NORTH);
				

				
				// Añadimos a la pantalla principal la parte central (CON UN BOTÓN) y le decimos que queremos que esté en el centro
				panel_central= new JPanel(new GridLayout(0,7));
				panel_central.setBackground(Color.black);

				cargarSeries();
				/*
				JScrollPane scroll= new JScrollPane(panel_central);
				panel_principal.add(scroll, BorderLayout.CENTER);
				*/

				
				// Configuramos el panel principal
				this.add(panel_principal);
				this.setTitle("Trailex");
				this.setExtendedState(MAXIMIZED_BOTH); //para que ocupe toda la pantalla
				this.setLocationRelativeTo(null);
				ImageIcon img= new ImageIcon("img/icono.png");
				Image img_icono= img.getImage();
				this.setIconImage(img_icono);
				
				JScrollPane scroll= new JScrollPane(panel_central);
				panel_principal.add(scroll, BorderLayout.CENTER);
				this.setVisible(true);
	}
	
	private void cargarSeries() {
		Videoclub.cargarSeries();
		for(Serie s: Videoclub.getAlSeries()) { //Por cada Película que hay en la lista de la clase Videoclub
			
			ImageIcon im = new ImageIcon(s.getRutaFoto());
			Image im_tamaño= im.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
			ImageIcon imageicon_tamano= new ImageIcon(im_tamaño);
			imageicon_tamano.setDescription(s.getRutaFoto());
			
			JLabel lblFoto = new JLabel(imageicon_tamano);
			
			Border border=BorderFactory.createLineBorder(turquesa,2);
			lblFoto.setBorder(border);
			
			panel_central.add(lblFoto);
		}
		panel_central.revalidate();
		panel_central.repaint();
		
	}
	

}
