package trailex.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import trailex.elementos.Videoclub;


public class Trailex_Principal extends JFrame{
	private JPanel panel_central, panel_principal, panel_arriba;
	private JButton boton_menu, boton_prueba, boton_prueba2;
	
	private static final long serialVersionUID = 1L;

	public Trailex_Principal() {
		// Llamamos a la función que crea la ventana Básica de nuestro programa
		Iniciar_Trailex();
		
	}
	
	private void Iniciar_Trailex() {
		// Creamos el panel con su disposición (BorderLayout) donde se mostrará la app
				panel_principal = new JPanel(new BorderLayout());
				
				// Creamos el panel que se encontrará en la parte superior del programa
				panel_arriba = new JPanel(new FlowLayout(50, 50, 50));
				
				// Creamos y añadimos el botón de Menú al panel de arriba:
				boton_menu = new JButton("MENU");
				boton_prueba = new JButton("Prueba");
				boton_prueba2 = new JButton("Prueba2");
				panel_arriba.add(boton_menu);
				panel_arriba.add(boton_prueba);
				panel_arriba.add(boton_prueba2);
				
				
				// Creamos y añadimos un Desplegable a la parte de arriba
				String[] generos = {"Comedia", "Terror", "Romantica", "Ciencia Ficción"};
				JComboBox<String> lista_desplegable = new JComboBox<String>(generos);	
				panel_arriba.add(lista_desplegable);
				
				// Creamos y añadimos una Barra de Búsqueda NO FUNCIONA, a la parte de arriba
				JTextField barra_buscar = new JTextField();
				panel_arriba.add(barra_buscar);
				
				
				// Añadimos a la pantalla principal la parte de arriba y le decimos que queremos que esté arriba
				panel_principal.add(panel_arriba, BorderLayout.NORTH);
				

				
				// Añadimos a la pantalla principal la parte central (CON UN BOTÓN) y le decimos que queremos que esté en el centro
				panel_central= new JPanel(new GridLayout(0,1));
				cargarPeliculas();
				JScrollPane scroll= new JScrollPane(panel_central);
				panel_principal.add(scroll, BorderLayout.CENTER);


				
				// Configuramos el panel principal
				this.add(panel_principal);
				this.setTitle("Trailex");
				this.setSize(1200, 900);
				this.setLocationRelativeTo(null);
				this.setVisible(true);
	}
	
	public void cargarPeliculas() {
		for(Pelicula p: Videoclub.getAlPeliculas()) { //Por cada Película que hay en la lista de la clase Videoclub
			
			ImageIcon im = new ImageIcon(p.getRutaFoto());
			Image im_tamaño= im.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
			ImageIcon imageicon_tamano= new ImageIcon(im_tamaño);
			imageicon_tamano.setDescription(p.getRutaFoto());
			JLabel lblFoto = new JLabel(imageicon_tamano);
			
			Border border=BorderFactory.createLineBorder(Color.WHITE,2);
			lblFoto.setBorder(border);
			panel_central.add(lblFoto);
		}
		
	}
	

}
