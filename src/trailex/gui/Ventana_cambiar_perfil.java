package trailex.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
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
	private boolean b_aceptar_presionado = false;
	private ImageIcon icono_perfil;
	
	public Ventana_cambiar_perfil(JFrame vAnt, ImageIcon iconoEscalado) {
		setSize(400,200);	
		setBackground(Color.black);
		
		icono_perfil = iconoEscalado; //guardo el icono que quiero poner nuevo
		
		panel= new JPanel(new BorderLayout());
		
		pNorte = new JPanel();
		pNorte.setBackground(Color.black);
		JLabel lbl_frase = new JLabel("¿Seguro que deseas cambiar la foto de perfil?");
		lbl_frase.setForeground(Trailex_Principal.turquesa);
		lbl_frase.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
		pNorte.add(lbl_frase);
		
		//Parte thread
		pCentro = new JPanel();
		pCentro.setBackground(Color.black);
		contador = new JLabel(String.valueOf(n_contador));
		contador.setForeground(Trailex_Principal.turquesa);
		contador.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		pCentro.add(contador);
		
		pSur = new JPanel(new FlowLayout());
		pSur.setBackground(Color.black);
		b_aceptar = new JButton("Aceptar");
		b_cancelar = new JButton("Cancelar");
		b_aceptar.setBackground(Trailex_Principal.turquesa);
		b_cancelar.setBackground(Trailex_Principal.turquesa);
		pSur.add(b_aceptar);
		pSur.add(b_cancelar);
		
		b_aceptar.addActionListener((e)->{
			b_aceptar_presionado = true;
			hiloCuentaAtras.interrupt();
			cambiar_foto_perfil(icono_perfil);
			cerrar_ventana();
        });
		
		b_cancelar.addActionListener((e)->{
			hiloCuentaAtras.interrupt();
			cerrar_ventana();
        });
		
		
		hiloCuentaAtras = new HiloCuentaAtras();
		hiloCuentaAtras.start();
		
		pNorte.setOpaque(true);
		pCentro.setOpaque(true);
		pSur.setOpaque(true);
		panel.add(pNorte, BorderLayout.NORTH);
		panel.add(pCentro, BorderLayout.CENTER);
		panel.add(pSur, BorderLayout.SOUTH);
		
		
		panel.setOpaque(true);
		getContentPane().add(panel);
		
		setLocationRelativeTo(null);
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
			cambiar_foto_perfil(icono_perfil); //se acaba el contador asi que queremos llamar al metodo para cambiar la foto de perfil
			cerrar_ventana();
		}
	}
	
	public void cambiar_foto_perfil(ImageIcon icono) {
		//codigo para cambiar la foto de perfil
		if (b_aceptar_presionado==true || n_contador<=0) {  //si he pulsado el boton aceptar o mi contador ha llegado a 0 o menos --> cambio la foto de perfil
			Trailex_Principal.iconoPerfilActual = icono; // Actualizar el icono de perfil actual
			Trailex_Principal.getF_perfil().setIcon(Trailex_Principal.iconoPerfilActual); // Cambiar el icono del botón principal
			Trailex_Principal.selectorDialog.dispose(); // Cerrar el selector de imagen
		}
		
	}
	
	public void cerrar_ventana() {
		if (this.isVisible()) {
	        this.dispose();
	    }
	}
	
}