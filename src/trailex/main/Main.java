package trailex.main;
import java.awt.Color;

import javax.swing.UIManager;

import trailex.domain.Videoclub;
import trailex.gui.Trailex_Principal;

public class Main {
	public static void main(String args[]) {
		//Para cambiar los colores de todos los joptionpane
		UIManager.put("OptionPane.background", Color.BLACK);
        UIManager.put("Panel.background", Color.BLACK);
        UIManager.put("OptionPane.messageForeground", Trailex_Principal.turquesa);
        UIManager.put("Button.background", Trailex_Principal.turquesa);
        UIManager.put("Button.foreground", Color.DARK_GRAY);
        UIManager.put("ProgressBar.selectionForeground", Color.WHITE); 
        UIManager.put("ProgressBar.selectionBackground", Color.white);
        
		Videoclub.cargarSeries();
		Trailex_Principal t1 = new Trailex_Principal();

	}
	
}
