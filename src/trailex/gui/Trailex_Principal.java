package trailex.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Trailex_Principal extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public Trailex_Principal() {
		// Creamos el panel con su disposición (GridLayout) donde se mostrará la app
		JPanel panel_principal = new JPanel(new BorderLayout()) {};
		
		JPanel panel_arriba = new JPanel(new FlowLayout(50, 50, 50)) {};
		String[] generos = {"Humor", "Miedo", "Romantica"};
		JComboBox<String> lista_desplegable = new JComboBox<String>(generos);	
		panel_arriba.add(lista_desplegable);
		
		
		panel_principal.add(panel_arriba, BorderLayout.NORTH);
		
		
		
		
		
		panel_principal.add(new JButton("QUITAR BOTON Y AÑADIR SCROLL Y PANEL"), BorderLayout.CENTER);
		
		
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
