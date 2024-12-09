package trailex.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class BarraDeCarga extends JFrame {

	    private static final long serialVersionUID = 1L;
	    
	    // Valor máximo a contar
	    private static final long MAX_VALUE = 1_00;
	    
	    //Booleano que me indica si ha terminado el thread
	    public static boolean hilo_inicio_terminado = false;
	    
	    // Progress Bar
	    public JProgressBar progressBar = new JProgressBar(0, 100);
	    
	    // Clase que implementa el hilo contador
	    public Contador contador;
	    
	    public BarraDeCarga() {        
	
	    	// Visualización del % en la Progress Bar
	    	progressBar.setStringPainted(true);  
	    	progressBar.setForeground(Color.GREEN);
	    	
	        this.setLayout(new BorderLayout());
	        
	        
	        this.add(progressBar, BorderLayout.CENTER);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setSize(500, 100);
	        this.setTitle("Cargando...");
	        this.setLocationRelativeTo(null);
	        
	        JLabel imagenCarga = new JLabel();
	        ImageIcon icon = new ImageIcon("fotocarga.jpg");
	        imagenCarga.setIcon(icon);
	        imagenCarga.setHorizontalAlignment(JLabel.CENTER);
	        this.add(imagenCarga, BorderLayout.NORTH);
	        
	        contador = new Contador();
	        contador.start();
	        
	        setVisible(true);
	    }
	    
	    public boolean isHilo_inicio_terminado() {
			return hilo_inicio_terminado;
		}


		public class Contador extends Thread {
	    	@Override
	    	public void run() {
	    		int progreso;

	    		for (int i=0; i <= MAX_VALUE; i++) {
	    			// Comprobar si hay que parar el hilo
					if (Thread.currentThread().isInterrupted()) {
						updateProgressBar(100);
						break;
					}

	    			// Valor de progreso
	    			progreso = i;
	    			
	    			if (progreso == 100) {
	    				hilo_inicio_terminado=true;
	    			}
	    			// Imprimir en consola
	    			//System.out.println(String.format("- Hilo '%s' -> %d (%d%%)", Thread.currentThread().getName(), i, progreso));    			
	    			// Actualizar la Progress Bar
	    			updateProgressBar(progreso);
	    			
	    			try {
	                    // Simular trabajo
	                    Thread.sleep(5); // Ajusta el tiempo si quieres que avance más lento o rápido
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
	                    break;
	                }
	    			
	            }
	    		
	        }
	    	
	    }
		

	    // Actualización de la Progress Bar usando SwingUtilities
		public void updateProgressBar(final int value) { //pq esta en un invoke later
			SwingUtilities.invokeLater(()->{
				progressBar.setValue(value);
				progressBar.setString(String.valueOf(value)+"%");
				
				
			});
		}
	    
		
	    /*
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new BarraDeCarga()); 
	    }
	    */
	    
	
}
