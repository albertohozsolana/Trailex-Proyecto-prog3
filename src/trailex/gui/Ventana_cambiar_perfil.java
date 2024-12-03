package trailex.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Ventana_cambiar_perfil extends JFrame {
	
	private JPanel panel, pNorte, pCentro, pSur;
	private JButton b_aceptar, b_cancelar;
	private JLabel contador;
	private int n_contador = 5;
	private HiloCuentaAtras hiloCuentaAtras;
	
	public Ventana_cambiar_perfil() {
		setBounds(500,200,400,200);	
		panel= new JPanel(new BorderLayout());
		
		pNorte = new JPanel();
		JLabel lbl_frase = new JLabel("¿Seguro que deseas cambiar la foto de perfil?");
		lbl_frase.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		pNorte.add(lbl_frase);
		
		//Parte thread
		pCentro = new JPanel();
		contador = new JLabel(String.valueOf(n_contador));
		contador.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		pCentro.add(contador);
		
		pSur = new JPanel(new FlowLayout());
		b_aceptar = new JButton("Aceptar");
		b_cancelar = new JButton("Cancelar");
		pSur.add(b_aceptar);
		pSur.add(b_cancelar);
		
		b_aceptar.addActionListener((e)->{
			cambiar_foto_perfil();
			cerrar_ventana();
        });
		
		b_cancelar.addActionListener((e)->{
			cerrar_ventana();
        });
		
		
		hiloCuentaAtras = new HiloCuentaAtras();
		hiloCuentaAtras.start();
		
		panel.add(pNorte, BorderLayout.NORTH);
		panel.add(pCentro, BorderLayout.CENTER);
		panel.add(pSur, BorderLayout.SOUTH);
		getContentPane().add(panel);
		setVisible(true);
	}
	
	private class HiloCuentaAtras extends Thread  {

		@Override
		public void run() {

			while (n_contador >=0) {
				if (Thread.currentThread().isInterrupted()) {
					cerrar_ventana();
					break;
				}
				
				SwingUtilities.invokeLater(() -> {
                    contador.setText(String.valueOf(n_contador));
                });
				
				try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                	this.interrupt();
                	break;
                }
				n_contador--;
			}
			cambiar_foto_perfil(); //se acaba el contador asi que queremos llamar al metodo para cambiar la foto de perfil
		}
	}
	
	public void cambiar_foto_perfil() {
		//codigo para cambiar la foto de perfil
	}
	
	public void cerrar_ventana() {
		this.dispose();
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ventana_cambiar_perfil()); 
	}
}