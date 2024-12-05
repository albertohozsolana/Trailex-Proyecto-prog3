package trailex.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class BarraDeCarga extends JFrame {

	    private static final long serialVersionUID = 1L;
	    
	    // Valor máximo a contar
	    private static final long MAX_VALUE = 10_00;
	    
	    // Progress Bar
	    private JProgressBar progressBar = new JProgressBar(0, 100);
	    
	    // Clase que implementa el hilo contador
	    private Contador contador;
	    
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
	        
	        contador = new Contador();
	        contador.start();
	        
	        setVisible(true);
	    }
	    
	    private class Contador extends Thread {
	    	@Override
	    	public void run() {
	    		int progreso;
	    		
	    		for (int i=0; i <= MAX_VALUE; i++) {
	    			// Comprobar si hay que parar el hilo
					if (Thread.currentThread().isInterrupted()) {
						updateProgressBar(100);
						break;
					}
					
					//
	    			
	    			// Valor de progreso
	    			progreso = (int) ((i * 100) / MAX_VALUE);
	    			// Imprimir en consola
	    			System.out.println(String.format("- Hilo '%s' -> %d (%d%%)", Thread.currentThread().getName(), i, progreso));    			
	    			// Actualizar la Progress Bar
	    			updateProgressBar(progreso);
	    			
	    			try {
	                    // Simular trabajo
	                    Thread.sleep(1); // Ajusta el tiempo si quieres que avance más lento o rápido
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
	                    break;
	                }
	            }
	        }
	    }

	    // Actualización de la Progress Bar usando SwingUtilities
	    private void updateProgressBar(final int value) {
	        SwingUtilities.invokeLater(() -> progressBar.setValue(value));
	    }
	    
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new BarraDeCarga()); 
	    }
	    
	    
	
}
