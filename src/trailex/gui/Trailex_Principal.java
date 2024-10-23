package trailex.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Trailex_Principal extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public Trailex_Principal() {
		// Llamamos a la función que crea la ventana Básica de nuestro programa
		Iniciar_Trailex();
		
	}
	
	private void Iniciar_Trailex() {
		// Creamos el panel con su disposición (BorderLayout) donde se mostrará la app
				JPanel panel_principal = new JPanel(new BorderLayout());
				
				// Creamos el panel que se encontrará en la parte superior del programa
				JPanel panel_arriba = new JPanel(new FlowLayout(50, 50, 50));
				
				// Creamos y añadimos el botón de Menú al panel de arriba:
				JButton boton_menu = new JButton("MENU");
				JButton boton_prueba = new JButton("Prueba");
				JButton boton_prueba2 = new JButton("Prueba2");
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
				panel_principal.add(new JButton("QUITAR BOTON Y AÑADIR SCROLL Y PANEL"), BorderLayout.CENTER);
				
				
				// Configuramos el panel principal
				this.add(panel_principal);
				this.setTitle("Trailex");
				this.setSize(1200, 900);
				this.setLocationRelativeTo(null);
				this.setVisible(true);
	}
	
	public static void main(String args[]) {
		Trailex_Principal t1 = new Trailex_Principal();
	}
	
}
