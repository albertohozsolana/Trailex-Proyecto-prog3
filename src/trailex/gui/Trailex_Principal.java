package trailex.gui;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import trailex.elementos.Pelicula;
import trailex.elementos.Serie;
import trailex.elementos.Videoclub;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Trailex_Principal extends JFrame{
	private JPanel panel_central;
	private JPanel panel_principal;
	private JPanel panel_arriba;
	private JPanel panel_comedia, panel_romance, panel_aventura, panel_drama,panel_cf, panel_terror;
	private JButton boton_menu, boton_pelis, boton_series;
	private Color turquesa=new Color(0x5FA6AD);
	private JLabel t_comedia, t_romance, t_aventura, t_drama, t_cf, t_terror;
	private JPanel p_norte_comedia, p_grid_comedia, p_norte_romance, p_grid_romance, p_norte_cf, p_norte_terror, p_norte_drama, p_norte_aventura, p_grid_aventura, p_grid_drama, p_grid_cf, p_grid_terror;
	private GridLayout grid_comedia, grid_romance, grid_cf, grid_terror, grid_drama, grid_aventura;
	private JTextField searchBar;
    private JComboBox<String> genreSelector;
   
	
	private static final long serialVersionUID = 1L;


	public Trailex_Principal() {
		// Llamamos a la función que crea la ventana Básica de nuestro programa
		Iniciar_Trailex();
		
	
	

    // Agregar la barra de búsqueda al panel superior
    panel_arriba.add(searchBar, BorderLayout.NORTH);
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
				
			
				
				// ACCIÓN PULSAR BOTÓN
				boton_menu.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Menu_Trailex(panel_principal);
						
					}
					
				});
			
				
				boton_pelis = new JButton("Películas");
				boton_pelis.setBackground(turquesa);
				
				boton_series = new JButton("Series");
				boton_series.setBackground(turquesa);
				
				panel_arriba.add(boton_menu);
				panel_arriba.add(boton_pelis);
				panel_arriba.add(boton_series);
				
				
				
				
				// Añadimos a la pantalla principal la parte de arriba y le decimos que queremos que esté arriba
				panel_principal.add(panel_arriba, BorderLayout.NORTH);
				

				
				// Añadimos a la pantalla principal la parte central (CON UN BOTÓN) y le decimos que queremos que esté en el centro
				panel_central= new JPanel(new GridLayout(6,1));
				panel_central.setBackground(Color.black);

				

				//ORGANIZAMOS LAS SERIES EN FILAS POR GENERO-----------------------------------------------------
				
				//COMEDIA
				panel_comedia = new JPanel();
				
				panel_comedia.setLayout(new BorderLayout());
				
				grid_comedia= new GridLayout(1,0);
				
				p_grid_comedia= new JPanel();
				p_grid_comedia.setLayout(grid_comedia);
				JScrollPane scroll_comedia= new JScrollPane(p_grid_comedia, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scroll_comedia.getHorizontalScrollBar().setAlignmentX(LEFT_ALIGNMENT);

				
				t_comedia = new JLabel("Comedia");
				t_comedia.setForeground(turquesa);
				p_norte_comedia = new JPanel(new FlowLayout(FlowLayout.LEFT));
				p_norte_comedia.add(t_comedia);
								
				panel_comedia.add(p_norte_comedia, BorderLayout.NORTH);
				//panel_comedia.add(p_grid_comedia, BorderLayout.CENTER);
				panel_comedia.add(scroll_comedia, BorderLayout.CENTER);

				panel_central.add(panel_comedia);
					
				
				// ROMANCE
				panel_romance = new JPanel();
				panel_romance.setLayout(new BorderLayout());

				grid_romance = new GridLayout(1, 0);
				p_grid_romance = new JPanel();
				p_grid_romance.setLayout(grid_romance);

				t_romance = new JLabel("Romance"); // Cambiado a t_romance
				t_romance.setForeground(turquesa);
				p_norte_romance = new JPanel(new FlowLayout(FlowLayout.LEFT));
				p_norte_romance.add(t_romance);

				panel_romance.add(p_norte_romance, BorderLayout.NORTH);
				panel_romance.add(p_grid_romance, BorderLayout.CENTER); // Usar CENTER para el grid
				panel_central.add(panel_romance);
				

				// AVENTURA
				panel_aventura = new JPanel();
				panel_aventura.setLayout(new BorderLayout());

				grid_aventura = new GridLayout(1, 0);
				p_grid_aventura = new JPanel();
				p_grid_aventura.setLayout(grid_aventura);

				t_aventura = new JLabel("Aventura");
				t_aventura.setForeground(turquesa);
				p_norte_aventura = new JPanel(new FlowLayout(FlowLayout.LEFT));
				p_norte_aventura.add(t_aventura);

				panel_aventura.add(p_norte_aventura, BorderLayout.NORTH);
				panel_aventura.add(p_grid_aventura, BorderLayout.CENTER); // Usar CENTER para el grid
				panel_central.add(panel_aventura);
				

				// DRAMA
				panel_drama = new JPanel();
				panel_drama.setLayout(new BorderLayout());

				grid_drama = new GridLayout(1, 0);
				p_grid_drama = new JPanel();
				p_grid_drama.setLayout(grid_drama);

				t_drama = new JLabel("Drama");
				t_drama.setForeground(turquesa);
				p_norte_drama = new JPanel(new FlowLayout(FlowLayout.LEFT));
				p_norte_drama.add(t_drama);

				panel_drama.add(p_norte_drama, BorderLayout.NORTH);
				panel_drama.add(p_grid_drama, BorderLayout.CENTER); // Usar CENTER para el grid
				panel_central.add(panel_drama);
				

				// CIENCIA FICCIÓN
				panel_cf = new JPanel();
				panel_cf.setLayout(new BorderLayout());

				grid_cf = new GridLayout(1, 0);
				p_grid_cf = new JPanel();
				p_grid_cf.setLayout(grid_cf);

				t_cf = new JLabel("Ciencia Ficción");
				t_cf.setForeground(turquesa);
				p_norte_cf = new JPanel(new FlowLayout(FlowLayout.LEFT));
				p_norte_cf.add(t_cf);

				panel_cf.add(p_norte_cf, BorderLayout.NORTH);
				panel_cf.add(p_grid_cf, BorderLayout.CENTER); // Usar CENTER para el grid
				panel_central.add(panel_cf);

				
				// TERROR
				panel_terror = new JPanel();
				panel_terror.setLayout(new BorderLayout());

				grid_terror = new GridLayout(1, 0);
				p_grid_terror = new JPanel();
				p_grid_terror.setLayout(grid_terror);

				t_terror = new JLabel("Terror");
				t_terror.setForeground(turquesa);
				p_norte_terror = new JPanel(new FlowLayout(FlowLayout.LEFT));
				p_norte_terror.add(t_terror);

				panel_terror.add(p_norte_terror, BorderLayout.NORTH);
				panel_terror.add(p_grid_terror, BorderLayout.CENTER); // Usar CENTER para el grid
				panel_central.add(panel_terror);
				
				
				panel_aventura.setBackground(Color.black);
				p_grid_aventura.setBackground(Color.black);
				p_norte_aventura.setBackground(Color.black);
				
				panel_terror.setBackground(Color.black);
				p_grid_terror.setBackground(Color.black);
				p_norte_terror.setBackground(Color.black);
				
				panel_comedia.setBackground(Color.black);
				p_grid_comedia.setBackground(Color.black);
				p_norte_comedia.setBackground(Color.black);
				
				panel_drama.setBackground(Color.black);
				p_grid_drama.setBackground(Color.black);
				p_norte_drama.setBackground(Color.black);
				
				panel_drama.setBackground(Color.black);
				p_grid_drama.setBackground(Color.black);
				p_norte_drama.setBackground(Color.black);
				
				panel_cf.setBackground(Color.black);
				p_grid_cf.setBackground(Color.black);
				p_norte_cf.setBackground(Color.black);
				
				panel_romance.setBackground(Color.black);
				p_grid_romance.setBackground(Color.black);
				p_norte_romance.setBackground(Color.black);
				

				cargarSeries();
				cargarPeliculas();
				inicializarFiltroPorGenero();
				
				
				// Configuramos el panel principal
				this.add(panel_principal);
				this.setTitle("Trailex");
				this.setExtendedState(MAXIMIZED_BOTH); //para que ocupe toda la pantalla
				this.setLocationRelativeTo(null);
				ImageIcon img= new ImageIcon("img/icono.png");
				Image img_icono= img.getImage();
				this.setIconImage(img_icono);
				
				JScrollPane scroll= new JScrollPane(panel_central, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //cambiar a never 
				panel_principal.add(scroll, BorderLayout.CENTER);
				this.setVisible(true);
				
				
				
				// Inicializar la barra de búsqueda
			    searchBar = new JTextField(20);
			    searchBar.setBorder(BorderFactory.createTitledBorder("Buscar Serie"));
			    
			    searchBar.setBackground(turquesa);

			    searchBar.addKeyListener(new KeyAdapter() {
			        @Override
			        public void keyReleased(KeyEvent e) {
			            String searchText = searchBar.getText().toLowerCase();
			            panel_central.removeAll();

			            for (Serie serie : Videoclub.getAlSeries()) {
			                if (serie.getTitulo().toLowerCase().contains(searchText)) {
			                    JLabel etiquetaSerie = new JLabel(serie.getTitulo());
			                    panel_central.add(etiquetaSerie);
			                }
			            }

			            panel_central.revalidate();
			            panel_central.repaint();
			        }
			    });

			    // Agregar la barra de búsqueda al panel superior
			    panel_arriba.add(searchBar, BorderLayout.NORTH);
			    
			    
	}
	
	private JPanel crearMenu_lat() {
		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.add(Box.createVerticalStrut(40)); // Separación entre botones
		
		JButton b_cerrar = new JButton("CERRAR SESIÓN");
		b_cerrar.setBackground(turquesa);
		menu.add(b_cerrar);
		
		// CERRAR APP:
		b_cerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
		JButton f_perfil = new JButton();
		ImageIcon icon_perfil= new ImageIcon("img/perfil.jpg");
		Image img_menu=icon_perfil.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		f_perfil.setIcon(new ImageIcon(img_menu));
		
		f_perfil.setOpaque(false); // Eliminar el fondo opaco del botón
		f_perfil.setContentAreaFilled(false); // No rellenar el área del botón
		f_perfil.setBorderPainted(false);
		
		f_perfil.setHorizontalAlignment(SwingConstants.CENTER);
		f_perfil.setVerticalAlignment(SwingConstants.CENTER);
		//f_perfil.setBorder(new EmptyBorder(0, 0, 40, 0));
		
		menu.add(Box.createVerticalGlue());
		
		menu.add(f_perfil);
		
		
		return menu;
	}
	
	private void Menu_Trailex(JPanel total) {
		
		if (((BorderLayout) total.getLayout()).getLayoutComponent(BorderLayout.WEST) != null) {
			
			BorderLayout layout = (BorderLayout) total.getLayout();
			Component panelEnOeste = layout.getLayoutComponent(BorderLayout.WEST);
			total.remove(panelEnOeste);
			
			total.revalidate();
			total.repaint();
			
			
		} else {
			JPanel menu_lat = crearMenu_lat();
			menu_lat.setBackground(Color.black);
			
			total.add(menu_lat, BorderLayout.WEST);
			total.revalidate();
			total.repaint();
		}
	}
	
	
	

	public void cargarSeries() {
	   

	    // Verificar que la lista de series no sea nula
	    if (Videoclub.getAlSeries() == null) {
	        System.err.println("Error: La lista de series es nula.");
	        return;
	    }

	    // Clasificar y agregar las series a los paneles correspondientes
	    for (Serie serie : Videoclub.getAlSeries()) {
	    	
	    	ImageIcon im = new ImageIcon(serie.getRutaFoto());
			Image im_tamaño= im.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
			ImageIcon imageicon_tamano= new ImageIcon(im_tamaño);
			imageicon_tamano.setDescription(serie.getRutaFoto());
			
			JLabel lblFoto = new JLabel(imageicon_tamano);
			
			Border border=BorderFactory.createLineBorder(turquesa,2);
			lblFoto.setBorder(border);
			
			lblFoto.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e) {
			        mostrarInfoSerie(serie);
			    }
			});

			
	        if (serie == null || serie.getGenero() == null) {
	            System.err.println("Advertencia: Serie o género nulo encontrado.");
	            continue;
	        }

	       

	        switch (serie.getGenero().toLowerCase()) {
            case "comedia":
                p_grid_comedia.add(lblFoto);
                break;
            case "romance":               
                p_grid_romance.add(lblFoto);
                break;               
            case "aventura":          	
            	p_grid_aventura.add(lblFoto);
                break;
            case "drama":             
                p_grid_drama.add(lblFoto);
                break;
            case "ciencia ficcion":              
                p_grid_cf.add(lblFoto);
                break;
            case "terror":           
                p_grid_terror.add(lblFoto);
                break;
            
	        }
	    }



	    // Refrescar la interfaz
	    panel_central.revalidate();
	    panel_central.repaint();
	}
	
	private void inicializarFiltroPorGenero() {
	    // Inicializar la barra de géneros
	    genreSelector = new JComboBox<>(new String[]{"Todos", "Comedia", "Romance", "Aventura", "Drama", "Ciencia Ficcion", "Terror"});
	    genreSelector.setBorder(BorderFactory.createTitledBorder("Filtrar por Género"));

	    genreSelector.setBackground(turquesa);
	    
	    genreSelector.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String selectedGenre = (String) genreSelector.getSelectedItem();
	            panel_central.removeAll();

	            for (Serie serie : Videoclub.getAlSeries()) {
	                if (selectedGenre.equals("Todos") || serie.getGenero().equalsIgnoreCase(selectedGenre)) {
	                    JLabel etiquetaSerie = new JLabel(serie.getTitulo());
	                    panel_central.add(etiquetaSerie);
	                }
	            }

	            panel_central.revalidate();
	            panel_central.repaint();
	        }
	    });

	    // Agregar la barra de géneros al panel superior
	    panel_arriba.add(genreSelector, BorderLayout.NORTH);
	    
	    panel_central.revalidate();
	    panel_central.repaint();
	}
	
	public void cargarPeliculas() {
	  

	    // Verificar que la lista de peliculas no sea nula
	    if (Videoclub.getAlPeliculas() == null) {
	        System.err.println("Error: La lista de peliculas es nula.");
	        return;
	    }

	    // Clasificar y agregar las peliculas a los paneles correspondientes
	    for (Pelicula pelicula : Videoclub.getAlPeliculas()) {
	    
	        ImageIcon im = new ImageIcon(pelicula.getRutaFoto());
	        Image im_tamaño = im.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
	        ImageIcon imageicon_tamano = new ImageIcon(im_tamaño);
	        imageicon_tamano.setDescription(pelicula.getRutaFoto());
	        
	        JLabel lblFoto = new JLabel(imageicon_tamano);
	        
	        Border border = BorderFactory.createLineBorder(turquesa, 2);
	        lblFoto.setBorder(border);
	        
	        lblFoto.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                mostrarInfoPelicula(pelicula);
	            }
	        });
;
	        if (pelicula == null || pelicula.getGenero() == null) {
	            System.err.println("Advertencia: Pelicula o género nulo encontrado.");
	            continue;
	        }

	        switch (pelicula.getGenero().toLowerCase()) {
	            case "comedia":
	                p_grid_comedia.add(lblFoto);
	                break;
	            case "romance":
	                p_grid_romance.add(lblFoto);
	                break;
	            case "aventura":
	                p_grid_aventura.add(lblFoto);
	                break;
	            case "drama":
	                p_grid_drama.add(lblFoto);
	                break;
	            case "ciencia ficcion":
	                p_grid_cf.add(lblFoto);
	                break;
	            case "terror":
	                p_grid_terror.add(lblFoto);
	                break;
	        }
	    }

	    // Refrescar la interfaz
	    panel_central.revalidate();
	    panel_central.repaint();
	}


	class ImagePanel extends JPanel {
	    private Image image;

	    public ImagePanel(String imagePath) {
	        this.image = new ImageIcon(imagePath).getImage();
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int x = (getWidth() - image.getWidth(this)) / 2;
	        int y = (getHeight() - image.getHeight(this)) / 2;
	        g.drawImage(image, x, y, this);
	    }

	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(image.getWidth(this), image.getHeight(this));
	    }
	}

	


	public void mostrarInfoPelicula(Pelicula pelicula) {
	    JFrame ventanaInfo = new JFrame("Información de la Película");
	    ventanaInfo.setSize(400, 500);
	    ventanaInfo.setLayout(new BorderLayout());
	    ventanaInfo.setLocationRelativeTo(null);

	    // Configuración de fondo de imagen para la película
	    JLabel background = new JLabel(new ImageIcon(pelicula.getRutaFoto()));
	    background.setLayout(new BorderLayout());
	    ventanaInfo.setContentPane(background);

	    // Configuración del título
	    JLabel labelTitulo = new JLabel(pelicula.getTitulo(), SwingConstants.CENTER);
	    labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
	    labelTitulo.setForeground(Color.WHITE); // Texto en blanco para visibilidad en la imagen
	    labelTitulo.setBorder(new EmptyBorder(10, 10, 10, 10));

	    // Edad recomendada
	    JLabel labelEdadRecomendada = new JLabel("Edad recomendada: " + pelicula.getEdadRecomendada() + " años", SwingConstants.CENTER);
	    labelEdadRecomendada.setFont(new Font("Arial", Font.PLAIN, 18));
	    labelEdadRecomendada.setForeground(Color.WHITE);

	    // Panel de detalles adicionales de la película
	    JPanel panelDetalles = new JPanel();
	    panelDetalles.setOpaque(false); // Hacer el panel transparente
	    panelDetalles.setLayout(new GridLayout(3, 1, 5, 5));
	    JLabel labelAnio = new JLabel("Año: " + pelicula.getAnio());
	    JLabel labelGenero = new JLabel("Género: " + pelicula.getGenero());
	    JLabel labelProtagonista = new JLabel("Protagonista: " + pelicula.getProtagonista());
	    labelAnio.setFont(new Font("Arial", Font.PLAIN, 16));
	    labelGenero.setFont(new Font("Arial", Font.PLAIN, 16));
	    labelProtagonista.setFont(new Font("Arial", Font.PLAIN, 16));
	    labelAnio.setForeground(Color.WHITE);
	    labelGenero.setForeground(Color.WHITE);
	    labelProtagonista.setForeground(Color.WHITE);
	    panelDetalles.add(labelAnio);
	    panelDetalles.add(labelGenero);
	    panelDetalles.add(labelProtagonista);

	    // Colocación de componentes en la ventana
	    ventanaInfo.add(labelTitulo, BorderLayout.NORTH);
	    ventanaInfo.add(labelEdadRecomendada, BorderLayout.CENTER);
	    ventanaInfo.add(panelDetalles, BorderLayout.SOUTH);

	    ventanaInfo.setVisible(true);
	}

	public void mostrarInfoSerie(Serie serie) {
	    JFrame ventanaInfo = new JFrame("Información de la Serie");
	    ventanaInfo.setSize(400, 500);
	    ventanaInfo.setLayout(new BorderLayout());
	    ventanaInfo.setLocationRelativeTo(null);

	    // Configuración de fondo de imagen para la serie
	    JLabel background = new JLabel(new ImageIcon(serie.getRutaFoto()));
	    background.setLayout(new BorderLayout());
	    ventanaInfo.setContentPane(background);

	    // Configuración del título
	    JLabel labelTitulo = new JLabel(serie.getTitulo(), SwingConstants.CENTER);
	    labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
	    labelTitulo.setForeground(Color.WHITE); // Texto en blanco para visibilidad en la imagen
	    labelTitulo.setBorder(new EmptyBorder(10, 10, 10, 10));

	    // Edad recomendada
	    JLabel labelEdadRecomendada = new JLabel("Edad recomendada: " + serie.getEdadRecomendada() + " años", SwingConstants.CENTER);
	    labelEdadRecomendada.setFont(new Font("Arial", Font.PLAIN, 18));
	    labelEdadRecomendada.setForeground(Color.WHITE);

	    // Panel de detalles adicionales de la serie
	    JPanel panelDetalles = new JPanel();
	    panelDetalles.setOpaque(false); // Hacer el panel transparente
	    panelDetalles.setLayout(new GridLayout(4, 1, 5, 5));
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

	    // Colocación de componentes en la ventana
	    ventanaInfo.add(labelTitulo, BorderLayout.NORTH);
	    ventanaInfo.add(labelEdadRecomendada, BorderLayout.CENTER);
	    ventanaInfo.add(panelDetalles, BorderLayout.SOUTH);

	    ventanaInfo.setVisible(true);
	}


}

