package trailex.gui;

import java.awt.BorderLayout;

import trailex.db.GestorBD;
import trailex.domain.GestorRecordatorio;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logica.Recursividad;
import trailex.domain.Pelicula;
import trailex.domain.Recordatorio;
import trailex.domain.Serie;
import trailex.domain.Usuario;
import trailex.domain.Videoclub;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.*;

public class Trailex_Principal extends JFrame {
	// Definir el enum para el tema
	public enum Tema {
	    BLANCO,
	    NEGRO
	}

	// Declarar las variables para el color de fondo y el tema actual
	private static Color BackgroundColorProgram;
	private Tema Tema_Interfaz;
	
	private JProgressBar progressBar;
	private JDialog progressDialog;
	private static JPanel panel_central;
	private static JPanel panel_principal;
	private JPanel panel_arriba;
	private static Usuario usuarioActual;
	
    private GestorRecordatorio gestorRecordatorios;

	
	private Trailex_Principal trailex_Principal; //ventana trailex
	
	private BarraDeCarga hilo_carga;

	private static JFrame ventana_portada;

	private JButton boton_menu;

	public static Color turquesa = new Color(0x5FA6AD);
	public static Color azulado = new Color(0x6392cd);

	private JPanel p_grid_comedia, p_grid_romance, p_grid_aventura, p_grid_drama, p_grid_cf, p_grid_terror;
	private JTextField searchBar;
	private JComboBox<String> genreSelector;
	
	private List<List<Serie>> series_combi = new ArrayList<List<Serie>>();

	// Atributos para acceder desde otra clase
	static JButton f_perfil;
	static JDialog selectorDialog;

	private static ArrayList<String> array_generos = new ArrayList<>(
			Arrays.asList("Comedia", "Romance", "Aventura", "Drama", "Ciencia Ficción", "Terror"));
	private ArrayList<JPanel> array_paneles = new ArrayList<>();
	private ArrayList<JLabel> array_series = new ArrayList<>();
	private static ArrayList<Serie> listaCompletaSeries = new ArrayList<>();

	private static GestorBD gestorBD = new GestorBD();

	private static final long serialVersionUID = 1L;
	

	public Trailex_Principal() {
		gestorBD.crearBBDD();

		gestorBD.crearTablaUsuario();
		// Se cargan los datos y se inicializa la BBDD
		gestorBD.initilizeFromCSV();
		gestorBD.insertarUsuarios(gestorBD.cargarUsuariosDesdeCSV());
		
		trailex_Principal= this;
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		
        
		
		IniciarSesion(); // Usuario: luiscoro
							// Contraseña: luiscoro
		
		
		gestorRecordatorios = new GestorRecordatorio();

	}
	
	public void Reiniciar() {
	    SwingUtilities.invokeLater(() -> {
	        // Cierra la ventana actual
	        dispose();

	        // Crea una nueva instancia de la aplicación
	        Trailex_Principal nuevaVentana = new Trailex_Principal();
	    });
	}

	
	
	
	private void IniciarSesion() {
		JFrame inicio = new JFrame("Login Panel");
		inicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inicio.setSize(300, 200);
		inicio.setResizable(false);

		JPanel panel_login = new JPanel();
		panel_login.setBackground(Color.black);
		panel_login.setLayout(new GridLayout(3, 2, 5, 5));

		JLabel usuario = new JLabel("Usuario:");
		usuario.setForeground(turquesa);
		usuario.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		JTextField tField = new JTextField(15);
		tField.setBackground(turquesa);

		JLabel contraseña = new JLabel("Contraseña:");
		contraseña.setForeground(turquesa);
		contraseña.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		JPasswordField contrafield = new JPasswordField(15);
		contrafield.setBackground(turquesa);

		JButton bote = new JButton("Enter");
		bote.setBackground(turquesa);

		ActionListener comprobacion = e -> {
			String user = tField.getText().trim();
			String pass = new String(contrafield.getPassword()).trim();

			if (gestorBD.esUsuarioValido(user, pass)) {
				inicio.dispose();
				usuarioActual = gestorBD.getUsuarioPorNickname(user);
				
				//IAG
				//SIN CAMBIOS (sugirió chatgpt hacerlo llamarlo de esta manera para que funcionase)
				hilo_carga = new BarraDeCarga(this);
				hilo_carga.setVisible(true);
				hilo_carga.startLoading();

			} else {
				JOptionPane.showMessageDialog(inicio, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
			}
		};

		KeyListener keylistener_enter = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					comprobacion.actionPerformed(null);
				}
			}
		};

		inicio.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int respuesta = JOptionPane.showConfirmDialog(inicio, "¿Estás seguro de que deseas salir?",
						"Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (respuesta == JOptionPane.YES_OPTION) {
					System.out.println("Ventana cerrada por el usuario.");
					inicio.dispose(); // Cierra la ventana
					gestorBD.borrarBBDD();
				} else {
					System.out.println("Cierre cancelado por el usuario.");
				}
			}
		});

		contrafield.addKeyListener(keylistener_enter);

		bote.addActionListener(comprobacion);
		tField.addActionListener(comprobacion);

		panel_login.add(usuario);
		panel_login.add(tField);
		panel_login.add(contraseña);
		panel_login.add(contrafield);

		JPanel panel_boton = new JPanel();
		panel_boton.setBackground(Color.black);
		panel_boton.add(bote);

		inicio.add(panel_login, BorderLayout.CENTER);
		inicio.add(panel_boton, BorderLayout.SOUTH);

		inicio.setVisible(true);
		inicio.setLocationRelativeTo(null);
	}

	public void Iniciar_Trailex() {
		revalidate();
		repaint();
		
		Tema_Interfaz = ThemeManager.cargarTema();
		System.out.println("temaprin"+Tema_Interfaz);
		
		if (Tema_Interfaz.equals(Tema.NEGRO)) {
			BackgroundColorProgram = Color.black;
		}
		else {
			BackgroundColorProgram = Color.white;
		}
		
		System.out.println(BackgroundColorProgram);

		// Creamos el panel con su disposición (BorderLayout) donde se mostrará la app
		panel_principal = new JPanel(new BorderLayout());
		panel_principal.setBackground(BackgroundColorProgram);

		// Creamos el panel que se encontrará en la parte superior del programa
		panel_arriba = new JPanel(new FlowLayout(50, 50, 50));
		panel_arriba.setBackground(BackgroundColorProgram);

		JButton startButton = new JButton("Iniciar Juego Cajas");
		startButton.setFont(new Font("Arial", Font.BOLD, 12));
		startButton.setPreferredSize(new Dimension(200, 20));
		startButton.setBackground(turquesa);
		startButton.setToolTipText("Iniciar Juego Cajas");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Abrir la ventana del juego
				SwingUtilities.invokeLater(() -> {
					ChooseYourSeriesGame game = new ChooseYourSeriesGame();
					game.setVisible(true);
				});
			}
		});

		JButton startButton2 = new JButton("Iniciar Juego Slots");
		startButton2.setFont(new Font("Arial", Font.BOLD, 12));
		startButton2.setPreferredSize(new Dimension(200, 20));
		startButton2.setBackground(turquesa);
		startButton2.setToolTipText("Iniciar Juego Slots");
		startButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Abrir la ventana del juego
				SwingUtilities.invokeLater(() -> {
					SlotMachineSeriesGame game2 = new SlotMachineSeriesGame();
					game2.setVisible(true);
				});
			}
		});

		// Creamos y añadimos el botón de Menú al panel de arriba:
		boton_menu = new JButton();
		boton_menu.setPreferredSize(new Dimension(30, 30));

		ImageIcon icon_menu = new ImageIcon("resources/images/menu.png");
		Image img_menu = icon_menu.getImage().getScaledInstance(17, 17, Image.SCALE_SMOOTH);
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
		panel_arriba.add(boton_menu);

		// Añadimos a la pantalla principal la parte de arriba y le decimos que queremos
		// que esté arriba
		panel_principal.add(panel_arriba, BorderLayout.NORTH);

		// Añadimos a la pantalla principal la parte central (CON UN BOTÓN) y le decimos
		// que queremos que esté en el centro
		panel_central = new JPanel(new GridLayout(7, 1));
		panel_central.setBackground(BackgroundColorProgram);

		agregarPanelUltimosEstrenos();
		panel_central.add(panelUltimosEstrenos);

		
		// ORGANIZAMOS LAS SERIES EN FILAS POR GENERO
		
		MouseListener mouselistener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if (ventana_portada != null && ventana_portada.isVisible()) {
		            cerrarPortadaSerie();
		        }
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		

		for (String genero : array_generos) {
			JPanel panel_genero = new JPanel();
			panel_genero.setLayout(new BorderLayout());
			panel_genero.setBackground(BackgroundColorProgram);
			panel_genero.addMouseListener(mouselistener);

			JPanel panel_fotos = new JPanel();
			panel_fotos.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel_fotos.setBackground(BackgroundColorProgram);
			panel_fotos.addMouseListener(mouselistener);

			JLabel texto_genero = new JLabel(genero);
			texto_genero.setForeground(turquesa);
			texto_genero.setBackground(BackgroundColorProgram);

			JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel_texto.add(texto_genero);
			panel_texto.setBackground(BackgroundColorProgram);

			panel_genero.add(panel_texto, BorderLayout.NORTH);
			panel_genero.add(panel_fotos, BorderLayout.SOUTH);

			panel_central.add(panel_genero);
			array_paneles.add(panel_fotos);

		}
		
		
		
		
		cargarSeries();
		inicializarFiltroPorGenero();
		iniciarFiltroPorNombre();

		// Agregar la barra de búsqueda al panel superior
		panel_arriba.add(searchBar, BorderLayout.NORTH);
		panel_arriba.add(startButton);
		panel_arriba.add(startButton2);

		// Configuramos el panel principal
		this.add(panel_principal);
		this.setTitle("Trailex");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH); // para que ocupe toda la pantalla
		this.setLocationRelativeTo(null);
		ImageIcon img = new ImageIcon("resources/images/icono.png");
		Image img_icono = img.getImage();
		this.setIconImage(img_icono);

		JScrollPane scroll = new JScrollPane(panel_central, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		scroll.getVerticalScrollBar().setUnitIncrement(16); //cambiamos la velocidad para que suba y baje más rápido
		
		panel_principal.add(scroll, BorderLayout.CENTER);
		

		// this.setVisible(true); lo hace la barra de carga
	}
	
	
	
	
	
	
	
	//AÑADIDO NUEVO
	public void mostrarVentana() {
	    SwingUtilities.invokeLater(() -> {
	        this.setExtendedState(MAXIMIZED_BOTH);
	        this.setVisible(true);
	        this.revalidate();
	        this.repaint();
	    });
	}
	

	// Getters para acceder desde otra clase

	static JButton getF_perfil() {
		return f_perfil;
	}

	static JDialog getSelectorDialog() {
		return selectorDialog;
	}

	static ImageIcon iconoPerfilActual = new ImageIcon("resources/images/perfil.jpg"); // Imagen de perfil
																						// predeterminada

	private JPanel crearMenu_lat() {
		Border borde_boton = BorderFactory.createLineBorder(Color.black, 7);

		JPanel menu = new JPanel();
		menu.setLayout(new BorderLayout());

		// Panel para contener el botón y limitar su tamaño
		JPanel panelFavoritos = new JPanel();
		panelFavoritos.setLayout(new BoxLayout(panelFavoritos, BoxLayout.Y_AXIS));
		panelFavoritos.setBackground(BackgroundColorProgram);
		menu.add(panelFavoritos, BorderLayout.CENTER);

		Dimension buttonSize = new Dimension(210, 50); // Ancho 150, Alto 50

		
		JButton btnAddRecordatorio = new JButton("Añadir Recordatorio");
	    btnAddRecordatorio.setBackground(Trailex_Principal.turquesa);
	    btnAddRecordatorio.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String titulo = JOptionPane.showInputDialog("Introduce el título de la serie:");
	            String fechaHoraStr = JOptionPane.showInputDialog("Introduce la fecha y hora (yyyy-MM-dd HH:mm):");
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, formatter);
	            gestorRecordatorios.añadirRecordatorio(titulo, fechaHora, usuarioActual.getNickname());
	        }
	    });
	    btnAddRecordatorio.setPreferredSize(buttonSize);
	    btnAddRecordatorio.setMaximumSize(buttonSize);
	    btnAddRecordatorio.setMinimumSize(buttonSize);
	    btnAddRecordatorio.setBorder(borde_boton);

	    JButton btnShowRecordatorios = new JButton("Mostrar Recordatorios");
	    btnShowRecordatorios.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            java.util.List<Recordatorio> recordatorios = gestorRecordatorios.obtenerRecordatoriosPorUsuario(usuarioActual.getNickname());
	            StringBuilder sb = new StringBuilder("<html>");  // Ajuste para el texto en blanco
	            
	            if (recordatorios.isEmpty()) {
	                sb.append("No hay recordatorios para mostrar.");
	            } else {
	                for (Recordatorio rec : recordatorios) {
	                    sb.append("<p>Título: ").append(rec.getTitulo())
	                      .append("<br>Fecha y Hora: ").append(rec.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
	                      .append("<br>Usuario: ").append(rec.getIdUsuario())
	                      .append("</p>");
	                }
	            }
	            sb.append("</body></html>");

	            // Crear la ventana para mostrar los recordatorios
	            JFrame frame = new JFrame("Recordatorios");
	            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            frame.setSize(400, 300);
	            frame.setLocationRelativeTo(null);

	            // Crear un componente JLabel para mostrar el texto con fondo negro
	            JLabel label = new JLabel(sb.toString());
	            label.setHorizontalAlignment(SwingConstants.LEFT);
	            label.setVerticalAlignment(SwingConstants.TOP);
	            label.setOpaque(true);  // Hacer opaco el label para que el fondo sea efectivo
	            label.setBackground(Color.BLACK);  // Establecer el fondo del label a negro

	            // Añadir scroll pane para visualizar si hay muchos recordatorios
	            JScrollPane scrollPane = new JScrollPane(label);
	            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	            scrollPane.getViewport().setBackground(Color.BLACK);  // Fondo negro para el scroll pane

	            frame.getContentPane().setBackground(Color.BLACK);  // Fondo negro para el frame
	            frame.add(scrollPane);
	            frame.setVisible(true);
	        }
	    });
		
	    btnShowRecordatorios.setPreferredSize(buttonSize);
	    btnShowRecordatorios.setMaximumSize(buttonSize);
	    btnShowRecordatorios.setMinimumSize(buttonSize);
	    btnShowRecordatorios.setBorder(borde_boton);
	    
	    
	    panelFavoritos.add(btnAddRecordatorio);
	    panelFavoritos.add(btnShowRecordatorios);
		
		
		
		
		JButton btnFavoritos = new JButton("FAVORITOS");
		btnFavoritos.setBackground(Trailex_Principal.turquesa);
		btnFavoritos.addActionListener(e -> mostrarFavoritos());
		btnFavoritos.setFocusable(true);

		panelFavoritos.add(btnFavoritos); // Añadir el botón al panel

		// SERIE_LIST
		JButton boton_combi = new JButton("CREAR LISTA");
		boton_combi.setBorder(borde_boton);
		boton_combi.setBackground(turquesa);
		boton_combi.setFocusable(true);

		panelFavoritos.add(boton_combi);
		boton_combi.addActionListener(e -> {
			ventana_combinaciones();
		});

		boton_combi.setPreferredSize(buttonSize);
		boton_combi.setMaximumSize(buttonSize);
		boton_combi.setMinimumSize(buttonSize);

		boton_combi.setPreferredSize(buttonSize);
		boton_combi.setMaximumSize(buttonSize);
		boton_combi.setMinimumSize(buttonSize);

		boton_combi.setBorder(borde_boton);

		
		// AÑADIR SERIE
		JButton boton_añadir = new JButton("CREAR SERIE");
		boton_añadir.setBorder(borde_boton);
		boton_añadir.setBackground(azulado);
		boton_añadir.setFocusable(true);

		panelFavoritos.add(boton_añadir);
		boton_añadir.addActionListener(e -> {
			crearSerie();
		});
	    


		boton_añadir.setPreferredSize(buttonSize);
		boton_añadir.setMaximumSize(buttonSize);
		boton_añadir.setMinimumSize(buttonSize);

		btnFavoritos.setPreferredSize(buttonSize);
		btnFavoritos.setMaximumSize(buttonSize);
		btnFavoritos.setMinimumSize(buttonSize);

		btnFavoritos.setBorder(borde_boton);

		JButton b_cerrar = new JButton("CERRAR SESIÓN");
		b_cerrar.setBorder(borde_boton);
		b_cerrar.setBackground(new Color(0xfd7153));
		b_cerrar.setFocusable(true);
		menu.add(b_cerrar, BorderLayout.NORTH);

		// CERRAR APP:
		b_cerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gestorBD.guardarSeriesCSV(listaCompletaSeries);
				gestorBD.borrarBBDD();
				Reiniciar();
			}
		});
		
		JToggleButton toggleTema = new JToggleButton("");
		toggleTema.setFocusable(true);
		toggleTema.setBorder(borde_boton);
		toggleTema.setPreferredSize(buttonSize);
		toggleTema.setMaximumSize(buttonSize);
		toggleTema.setMinimumSize(buttonSize);
		toggleTema.setBackground(Color.GRAY); // Color inicial

		// Estado inicial según el tema actual
		try {
		    if (ThemeManager.cargarTema().equals(Tema.BLANCO)) {
		    	System.out.println(ThemeManager.cargarTema());
		        toggleTema.setText("Modo Oscuro");
		        toggleTema.setSelected(false);
		    } else {
		        toggleTema.setText("Modo Claro");
		        toggleTema.setSelected(true);
		    }
		} catch (Exception ex) {
		    System.err.println("Error al cargar el tema: " + ex.getMessage());
		    toggleTema.setText("Modo Oscuro"); // Valor predeterminado
		    toggleTema.setSelected(false);
		}

		// Acción del ToggleButton para cambiar el tema
		toggleTema.addActionListener(e -> {
		    if (toggleTema.isSelected()) {
		        // Cambiar a modo oscuro
		        BackgroundColorProgram = Color.BLACK;
		        Tema_Interfaz = Tema.NEGRO;
		        toggleTema.setText("Modo Claro");
		    } else {
		        // Cambiar a modo claro
		        BackgroundColorProgram = Color.WHITE;
		        Tema_Interfaz = Tema.BLANCO;
		        toggleTema.setText("Modo Oscuro");
		    }

		    // Guardar el tema seleccionado
		    try {
		        ThemeManager.guardarTema(Tema_Interfaz);
		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this, "No se pudo guardar el tema. Reintenta más tarde.",
		                "Error", JOptionPane.ERROR_MESSAGE);
		        ex.printStackTrace();
		    }

		    gestorBD.guardarSeriesCSV(listaCompletaSeries);
			gestorBD.borrarBBDD();
			Reiniciar();

		    
		});

		
		panelFavoritos.add(toggleTema);

		f_perfil = new JButton();
		// Usa el icono de perfil actual para que conserve la selección previa
		Image img_menu = iconoPerfilActual.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		f_perfil.setIcon(new ImageIcon(img_menu));

		f_perfil.setOpaque(false); // Eliminar el fondo opaco del botón
		f_perfil.setContentAreaFilled(false); // No rellenar el área del botón
		f_perfil.setBorderPainted(false);

		f_perfil.setHorizontalAlignment(SwingConstants.CENTER);
		f_perfil.setVerticalAlignment(SwingConstants.CENTER);
		f_perfil.setBorder(new EmptyBorder(0, 0, 40, 0));

		// Acción para cambiar foto de perfil al hacer clic en f_perfil
		f_perfil.addActionListener(e -> mostrarSelectorFotoPerfil(f_perfil));

		menu.add(f_perfil, BorderLayout.SOUTH);

		return menu;
	}

	private void ventana_combinaciones() {
		
		// CREAMOS LA VENTANA QUE MOSTRARÁ LAS COMBINACIONES
		JFrame combinaciones = new JFrame();
		
		JPanel panel_combi = new JPanel(new BorderLayout());
				
		JPanel panel_obj = new JPanel(new GridLayout(1, 4, 10, 10));
		
		// COLOCAMOS UN BOTÓN Y UN JCOMBOBOX PARA GENERAR COMBINACIONES POR GÉNERO 
		JComboBox jcb_genero = new JComboBox<>(
				new String[] {"" ,"Comedia", "Romance", "Aventura", "Drama", "Ciencia Ficción", "Terror" });
		
		jcb_genero.setBackground(turquesa);
		panel_obj.add(jcb_genero);
		
		Border borde_boton = BorderFactory.createLineBorder(Color.black, 7);
		
		JButton boton_genero = new JButton("LISTA POR GÉNERO");
		boton_genero.setBorder(borde_boton);
		boton_genero.setBackground(turquesa);
		boton_genero.setFocusable(true);

		panel_obj.add(boton_genero);
		boton_genero.addActionListener(e -> {
			String genero = (String) jcb_genero.getSelectedItem();
			if (!genero.equals("")) {
				series_combi = Recursividad.combinacionesPorGenero(listaCompletaSeries, genero, 4);
				cargarCombi(panel_combi);
			}
			System.out.println("Seleccione un género");
		});		
		
		// COLOCAMOS UN BOTÓN Y UN SPINNER PARA LAS COMBINACIONES POR TEMPORADA
		JButton boton_horas = new JButton("LISTA POR TEMPORADAS");
		boton_horas.setBorder(borde_boton);
		boton_horas.setBackground(turquesa);
		boton_horas.setFocusable(true);

		panel_obj.add(boton_horas);
		
		JSpinner sp_num_horas = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
		sp_num_horas.setForeground(turquesa);
		panel_obj.add(sp_num_horas);
		
		boton_horas.addActionListener(e -> {
			series_combi = Recursividad.combinacionesPorTemporada(listaCompletaSeries, (Integer) sp_num_horas.getValue());
			cargarCombi(panel_combi);
		});
		
		panel_combi.add(panel_obj, BorderLayout.NORTH);	
		
		combinaciones.add(panel_combi);
		combinaciones.setSize(800, 600);
		combinaciones.setTitle("Combinaciones de series");
		combinaciones.setIconImage(null);
		combinaciones.setVisible(true);
		combinaciones.setLocationRelativeTo(null);
		combinaciones.setLocationRelativeTo(null);
		
		
	}
	
	private void cargarCombi(JPanel tot) {
		// CUANDO SE PULSEN LOS BOTONES DE GENERAR COMBINACIONES LLAMAREMOS A ESTA FUNCIÓN
		JPanel panel_conseries = new JPanel();
		panel_conseries.setLayout(new BoxLayout(panel_conseries, BoxLayout.Y_AXIS));
		
		// USO DE IA PARA AJUSTAR EL PANEL
		panel_conseries.setPreferredSize(new Dimension(
			    tot.getWidth() - 50, // Ancho dinámico basado en el panel padre
			    series_combi.size() * 300 // Altura basada en la cantidad de combinaciones (200px por combinación aprox.)
			));
		
		int cont = 0;
		for (List<Serie> lista : series_combi) {
			if (!lista.isEmpty()) {
				cont++;
				
				JPanel pane_temp = new JPanel();
				pane_temp.setLayout(new GridLayout(1, lista.size(), 10, 10));
				
				TitledBorder border = new TitledBorder("COMBINACION " + cont);
		        border.setTitlePosition(TitledBorder.TOP); // Posición del título: en la parte superior
		        border.setTitleJustification(TitledBorder.CENTER); // Centrado del texto en el borde
		        border.setTitleColor(turquesa);
		        
				pane_temp.setBorder(border);
				
				for (Serie s : lista) {
					JLabel label = new JLabel(s.getTitulo());
					label.setOpaque(true);
					label.setBackground(Color.black);
					label.setForeground(azulado);
					pane_temp.add(label);
				}
				panel_conseries.add(pane_temp, BorderLayout.CENTER);	
			}
			
		}
		
		// EN EL BUCLE ANTERIOR HEMOS GENERADO LAS COMBINACIONES EN LABELS Y LAS AÑADIMOS AL SCROLLPANE
		JScrollPane scrollPane = new JScrollPane(panel_conseries);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
	    BorderLayout layout = (BorderLayout) tot.getLayout();

	    // USO DE IA: Obtener el componente en la posición CENTER (BorderLayout.CENTER)
	    Component componenteCentro = layout.getLayoutComponent(BorderLayout.CENTER);

	    // Verificar si hay un componente en el centro y eliminarlo
	    if (componenteCentro != null) {
	        tot.remove(componenteCentro);
	    }

	    
	    tot.add(scrollPane, BorderLayout.CENTER);
        
	    scrollPane.revalidate();
	    scrollPane.repaint();
        tot.revalidate();
        tot.repaint();
 
        
	}

	
	
	
	
	public void mostrarSelectorFotoPerfil(JButton f_perfil) {
		// Crear ventana emergente de selección
		selectorDialog = new JDialog((Frame) null, "Seleccionar Foto de Perfil", true);
		selectorDialog.setSize(450, 150);
		selectorDialog.setLayout(new FlowLayout());
		selectorDialog.getContentPane().setBackground(BackgroundColorProgram);

		// Cargar las 5 imágenes de perfil
		String[] rutas = { "resources/images/perfil.jpg", "resources/images/perfil2.jpg",
				"resources/images/perfil3.jpg", "resources/images/perfil4.jpg" };
		for (String ruta : rutas) {
			// Crear icono y botón para cada imagen de perfil
			ImageIcon iconoPerfil = new ImageIcon(ruta);

			// Verificar si la imagen se carga correctamente
			if (iconoPerfil.getIconWidth() == -1) {
				System.err.println("Error al cargar la imagen: " + ruta); // Mensaje de error en consola
				continue; // Saltar esta imagen si no se carga
			}

			// Escalar la imagen
			Image imgPerfil = iconoPerfil.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			ImageIcon iconoEscalado = new ImageIcon(imgPerfil);
			JButton botonPerfil = new JButton(iconoEscalado);

			// Ajustar el tamaño del botón al de la imagen
			botonPerfil.setPreferredSize(new Dimension(iconoEscalado.getIconWidth(), iconoEscalado.getIconHeight()));
			botonPerfil.setMinimumSize(new Dimension(iconoEscalado.getIconWidth(), iconoEscalado.getIconHeight()));
			botonPerfil.setMaximumSize(new Dimension(iconoEscalado.getIconWidth(), iconoEscalado.getIconHeight()));

			// Eliminar bordes y relleno del botón
			botonPerfil.setOpaque(false);
			botonPerfil.setContentAreaFilled(false);
			botonPerfil.setBorderPainted(false);

			// Agregar acción para seleccionar la imagen como perfil
			botonPerfil.addActionListener(e -> {
				selectorDialog.dispose();
				Ventana_cambiar_perfil ventana_perfil = new Ventana_cambiar_perfil(this, iconoEscalado); // iniciar
			});

			// Añadir cada botón de imagen al diálogo
			selectorDialog.add(botonPerfil);
		}

		// Si no hay imágenes cargadas, muestra un mensaje en el diálogo	
		if (selectorDialog.getComponentCount() == 0) {	//IAG chatgpt adaptado, ayuda a solucionar errores
			JLabel errorLabel = new JLabel("No se pudieron cargar las imágenes de perfil.");
			errorLabel.setForeground(turquesa);
			errorLabel.setBackground(BackgroundColorProgram);
			selectorDialog.add(errorLabel);
		}
		selectorDialog.setLocationRelativeTo(null); // Centrar la ventana de selección
		selectorDialog.setVisible(true);
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
			menu_lat.setBackground(BackgroundColorProgram);

			total.add(menu_lat, BorderLayout.WEST);
			total.revalidate();
			total.repaint();
		}
	}

	private JFrame ventanaInfoPelicula;
	private static JFrame ventanaInfoSerie;

	// Función para cerrar la ventana de información de la película
	public void cerrarInfoPelicula() {
		if (ventanaInfoPelicula != null) {
			ventanaInfoPelicula.dispose();
			ventanaInfoPelicula = null;
		}
	}

	// Función para cerrar la ventana de información de la serie
	public void cerrarInfoSerie() {
		if (ventanaInfoSerie != null) {
			ventanaInfoSerie.dispose();
			ventanaInfoSerie = null;
		}
	}
	
	// Función para cerrar la ventana de la portada de la serie
	public static void cerrarPortadaSerie() {
		if (ventana_portada != null) {
			ventana_portada.dispose();
			ventana_portada = null;
		}
	}

	public void cargarSeries() { //IAG chatgpt adaptado, ayuda a solucionar errores

		// Verificar que la lista de series no sea nula
		if (Videoclub.getAlSeries() == null) {
			System.err.println("Error: La lista de series es nula.");
			return;
		}

		listaCompletaSeries.clear(); // Limpiar la lista original
		/*
		 * CARGAR LAS SERIES MEDIANTE EL CSV // Clasificar y agregar las series a los
		 * paneles correspondientes for (Serie serie : Videoclub.getAlSeries()) {
		 * listaCompletaSeries.add(serie); // Guardar todas las series en la lista
		 * completa
		 */

		// USAMOS LA BASE DE DATOS
		listaCompletaSeries = (ArrayList<Serie>) gestorBD.loadCSVSeries();

		for (Serie serie : listaCompletaSeries) {
			ImageIcon im = new ImageIcon(serie.getRutaFoto());
			Image im_tamaño = im.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
			ImageIcon imageicon_tamano = new ImageIcon(im_tamaño);
			imageicon_tamano.setDescription(serie.getRutaFoto());

			JLabel lblFoto = new JLabel(imageicon_tamano);

			lblFoto.setToolTipText(serie.getTitulo());

			Border border = BorderFactory.createLineBorder(turquesa, 2);
			lblFoto.setBorder(border);

			array_series.add(lblFoto);

			lblFoto.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					mostrarPortadaSerie(serie, lblFoto, e.getComponent().getLocationOnScreen());
				}
				
				
				

			});

			if (serie == null || serie.getGenero() == null) {
				System.err.println("Advertencia: Serie o género nulo encontrado.");
				continue;
			}

			switch (serie.getGenero().toLowerCase()) {
			case "comedia":
				array_paneles.get(0).add(lblFoto);
				break;
			case "romance":
				array_paneles.get(1).add(lblFoto);
				break;
			case "aventura":
				array_paneles.get(2).add(lblFoto);
				break;
			case "drama":
				array_paneles.get(3).add(lblFoto);
				break;
			case "ciencia ficción":
				array_paneles.get(4).add(lblFoto);
				break;
			case "terror":
				array_paneles.get(5).add(lblFoto);
				break;

			}
		}

		// Refrescar la interfaz
		panel_central.revalidate();
		panel_central.repaint();
	}

	private void mostrarTodasLasSeries() {
		panel_central.removeAll();

		for (int i = 0; i < array_generos.size(); i++) {
			JPanel panel_genero = new JPanel(new BorderLayout());
			panel_genero.setBackground(BackgroundColorProgram);

			JLabel texto_genero = new JLabel(array_generos.get(i));
			texto_genero.setForeground(turquesa);

			JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel_texto.setBackground(BackgroundColorProgram);
			panel_texto.add(texto_genero);

			panel_genero.add(panel_texto, BorderLayout.NORTH);
			panel_genero.add(array_paneles.get(i), BorderLayout.SOUTH);

			panel_central.add(panel_genero);
		}

		panel_central.revalidate();
		panel_central.repaint();
	}

	private void inicializarFiltroPorGenero() {
		// Inicializar la barra de géneros
		genreSelector = new JComboBox<>(
				new String[] { "Todos", "Comedia", "Romance", "Aventura", "Drama", "Ciencia Ficción", "Terror" });

		genreSelector.setBackground(turquesa);

		genreSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedGenre = (String) genreSelector.getSelectedItem();
				panel_central.removeAll();

				if (selectedGenre.equals("Todos")) {           
					agregarPanelUltimosEstrenos();
					panel_central.add(panelUltimosEstrenos);
					int contador = 0;
					for (JPanel panel : array_paneles) {
						JPanel panel_genero = new JPanel();
						panel_genero.setLayout(new BorderLayout());
						panel_genero.setBackground(BackgroundColorProgram);
						JLabel texto_genero = new JLabel(array_generos.get(contador));
						texto_genero.setForeground(turquesa);
						texto_genero.setBackground(BackgroundColorProgram);
						JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
						panel_texto.add(texto_genero);
						panel_texto.setBackground(BackgroundColorProgram);
						panel_genero.add(panel_texto, BorderLayout.NORTH);
						panel_genero.add(panel, BorderLayout.SOUTH);
						panel_central.add(panel_genero);
						contador++;
					}
				}

				else if (selectedGenre.equals("Comedia")) {

					JPanel panel_genero = new JPanel();
					panel_genero.setLayout(new BorderLayout());
					panel_genero.setBackground(BackgroundColorProgram);
					JLabel texto_genero = new JLabel("Comedia");
					texto_genero.setForeground(turquesa);
					texto_genero.setBackground(BackgroundColorProgram);
					JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
					panel_texto.add(texto_genero);
					panel_texto.setBackground(BackgroundColorProgram);
					panel_genero.add(panel_texto, BorderLayout.NORTH);
					panel_genero.add(array_paneles.get(0), BorderLayout.SOUTH);
					panel_central.add(panel_genero);
				}

				else if (selectedGenre.equals("Romance")) {       
					JPanel panel_genero = new JPanel();
					panel_genero.setLayout(new BorderLayout());
					panel_genero.setBackground(BackgroundColorProgram);
					JLabel texto_genero = new JLabel("Romance");
					texto_genero.setForeground(turquesa);
					texto_genero.setBackground(BackgroundColorProgram);
					JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
					panel_texto.add(texto_genero);
					panel_texto.setBackground(BackgroundColorProgram);
					panel_genero.add(panel_texto, BorderLayout.NORTH);
					panel_genero.add(array_paneles.get(1), BorderLayout.SOUTH);
					panel_central.add(panel_genero);
				}

				else if (selectedGenre.equals("Aventura")) {
					JPanel panel_genero = new JPanel();
					panel_genero.setLayout(new BorderLayout());
					panel_genero.setBackground(BackgroundColorProgram);
					JLabel texto_genero = new JLabel("Aventura");
					texto_genero.setForeground(turquesa);
					texto_genero.setBackground(BackgroundColorProgram);
					JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
					panel_texto.add(texto_genero);
					panel_texto.setBackground(BackgroundColorProgram);
					panel_genero.add(panel_texto, BorderLayout.NORTH);
					panel_genero.add(array_paneles.get(2), BorderLayout.SOUTH);
					panel_central.add(panel_genero);
				}

				else if (selectedGenre.equals("Drama")) {
					JPanel panel_genero = new JPanel();
					panel_genero.setLayout(new BorderLayout());
					panel_genero.setBackground(BackgroundColorProgram);
					JLabel texto_genero = new JLabel("Drama");
					texto_genero.setForeground(turquesa);
					texto_genero.setBackground(BackgroundColorProgram);
					JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
					panel_texto.add(texto_genero);
					panel_texto.setBackground(BackgroundColorProgram);
					panel_genero.add(panel_texto, BorderLayout.NORTH);
					panel_genero.add(array_paneles.get(3), BorderLayout.SOUTH);
					panel_central.add(panel_genero);
				}

				else if (selectedGenre.equals("Ciencia Ficción")) {
					JPanel panel_genero = new JPanel();
					panel_genero.setLayout(new BorderLayout());
					panel_genero.setBackground(BackgroundColorProgram);
					JLabel texto_genero = new JLabel("Ciencia Ficción");
					texto_genero.setForeground(turquesa);
					texto_genero.setBackground(BackgroundColorProgram);
					JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
					panel_texto.add(texto_genero);
					panel_texto.setBackground(BackgroundColorProgram);
					panel_genero.add(panel_texto, BorderLayout.NORTH);
					panel_genero.add(array_paneles.get(4), BorderLayout.SOUTH);
					panel_central.add(panel_genero);

				}

				else if (selectedGenre.equals("Terror")) {
					JPanel panel_genero = new JPanel();
					panel_genero.setLayout(new BorderLayout());
					panel_genero.setBackground(BackgroundColorProgram);
					JLabel texto_genero = new JLabel("Terror");
					texto_genero.setForeground(turquesa);
					texto_genero.setBackground(BackgroundColorProgram);
					JPanel panel_texto = new JPanel(new FlowLayout(FlowLayout.LEFT));
					panel_texto.add(texto_genero);
					panel_texto.setBackground(BackgroundColorProgram);
					panel_genero.add(panel_texto, BorderLayout.NORTH);
					panel_genero.add(array_paneles.get(5), BorderLayout.SOUTH);
					panel_central.add(panel_genero);
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

	private JPanel panelUltimosEstrenos;

	// Método para obtener las series cuyo año es 2020 o posterior
	private ArrayList<Serie> obtenerSeriesUltimosEstrenos() {
		ArrayList<Serie> seriesUltimosEstrenos = new ArrayList<>();
		for (Serie serie : Videoclub.getAlSeries()) {
			if (serie.getAnio() >= 2022) {
				seriesUltimosEstrenos.add(serie);
			}
		}
		return seriesUltimosEstrenos;
	}

	private void agregarPanelUltimosEstrenos() {
		// Crear el panel de "Últimos Estrenos" con configuración visual llamativa
		panelUltimosEstrenos = new JPanel();
		panelUltimosEstrenos.setLayout(new BorderLayout());
		panelUltimosEstrenos.setBackground(BackgroundColorProgram);
		panelUltimosEstrenos.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0), // Espacio
																														// exterior
																														// al
																														// borde
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 1), "ÚLTIMOS ESTRENOS    ", // Espacios
																														// adicionales
																														// en
																														// el
																														// texto
																														// del
																														// título
						TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
						new Font("Arial", Font.BOLD, 12), Color.RED)));

		// Panel interno para las portadas
		JPanel panelPortadas = new JPanel();
		panelPortadas.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panelPortadas.setBackground(BackgroundColorProgram);

		// Obtener y filtrar las series de "Últimos Estrenos"
		ArrayList<Serie> seriesUltimosEstrenos = obtenerSeriesUltimosEstrenos();

		for (Serie serie : seriesUltimosEstrenos) {

			// Etiqueta de portada de la serie
			ImageIcon im = new ImageIcon(serie.getRutaFoto());
			Image im_tamaño = im.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
			ImageIcon imageicon_tamano = new ImageIcon(im_tamaño);
			imageicon_tamano.setDescription(serie.getRutaFoto());

			JLabel lblFoto = new JLabel(imageicon_tamano);

			lblFoto.setToolTipText(serie.getTitulo());

			Border border = BorderFactory.createLineBorder(Color.red, 2);
			lblFoto.setBorder(border);

			array_series.add(lblFoto);

			lblFoto.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					mostrarPortadaSerie(serie, lblFoto, e.getComponent().getLocationOnScreen());
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					mostrarInfoSerie(serie);
					if (ventana_portada != null || ventanaInfoSerie.isActive()) {
						cerrarPortadaSerie();
					}
					
				}
			});

			// Agregar el panel de cada serie al panel de portadas
			panelPortadas.add(lblFoto);
	
		}
		MouseListener mouselistener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if (ventana_portada != null && ventana_portada.isVisible()) {
		            cerrarPortadaSerie();
		        }
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		panelPortadas.addMouseListener(mouselistener);
		panelUltimosEstrenos.addMouseListener(mouselistener);
		// Agregar el panel de portadas al panel principal de "Últimos Estrenos"
		panelUltimosEstrenos.add(panelPortadas, BorderLayout.CENTER);

	}

	public void iniciarFiltroPorNombre() { //IAG chatgpt adaptado, ayuda a solucionar errores
		// Inicializar la barra de búsqueda
		searchBar = new JTextField(20);

		searchBar.setBackground(turquesa);

		searchBar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String searchText = searchBar.getText().toLowerCase();

				genreSelector.setSelectedItem("Todos");

				panel_central.removeAll();

				if (searchText.isEmpty()) {
					mostrarTodasLasSeries(); // Restaurar todas las series si no hay texto
				} else {

					JPanel panelResultados = new JPanel();
					panelResultados.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
					panelResultados.setBackground(BackgroundColorProgram);

					for (Serie serie : listaCompletaSeries) {
						if (serie.getTitulo().toLowerCase().contains(searchText)) {
							ImageIcon im = new ImageIcon(serie.getRutaFoto());
							Image im_tamaño = im.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
							ImageIcon imageicon_tamano = new ImageIcon(im_tamaño);

							JLabel lblFoto = new JLabel(imageicon_tamano);
							lblFoto.setToolTipText(serie.getTitulo());

							Border border = BorderFactory.createLineBorder(turquesa, 2);
							lblFoto.setBorder(border);

							lblFoto.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseEntered(MouseEvent e) {
									mostrarPortadaSerie(serie, lblFoto, e.getComponent().getLocationOnScreen());

								}

								@Override
								public void mouseExited(MouseEvent e) {
									if (ventana_portada != null && ventana_portada.isVisible()) {
							            cerrarPortadaSerie();
							        }
								}

								@Override
								public void mouseWheelMoved(MouseWheelEvent e) {
									if (ventana_portada != null && ventana_portada.isVisible()) {
							            cerrarPortadaSerie();
							        }
								}
								
								
								
							});

							panelResultados.add(lblFoto); // Añadir al panel de resultados
						}
					}

					// Añadir el panel de resultados al panel central
					panel_central.add(panelResultados);
				}

				panel_central.revalidate(); // Refrescar el panel
				panel_central.repaint();
			}
		});

	}

	public void cargarPeliculas() { //IAG chatgpt adaptado, ayuda a solucionar errores

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
					if (ventana_portada != null || ventanaInfoSerie.isActive()) {
						cerrarPortadaSerie();
					}
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

	private Map<String, Integer> progresoPeliculas = new HashMap<>();
	private static Map<String, Integer> progresoSeries = new HashMap<>();

	public void mostrarInfoPelicula(Pelicula pelicula) {
		if (ventanaInfoPelicula != null) {
			ventanaInfoPelicula.dispose();
		}

		ImageIcon originalIcon = new ImageIcon(pelicula.getRutaFoto());
		int imageWidth = originalIcon.getIconWidth();
		int imageHeight = originalIcon.getIconHeight();
		double aspectRatio = (double) imageWidth / imageHeight;

		int windowWidth = 400;
		int windowHeight = (int) (windowWidth / aspectRatio);

		ventanaInfoPelicula = new JFrame("Información de la Película");
		ventanaInfoPelicula.setSize(windowWidth, windowHeight);
		ventanaInfoPelicula.setLayout(new BorderLayout());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width - windowWidth - 50;
		int y = (screenSize.height - windowHeight) / 2;
		ventanaInfoPelicula.setLocation(x, y);

		Image image = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
		JLabel background = new JLabel(new ImageIcon(image));
		background.setLayout(new BorderLayout());
		ventanaInfoPelicula.setContentPane(background);

		// Panel oscuro para el texto
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());
		textPanel.setBackground(new Color(0, 0, 0, 150)); // Fondo negro semi-transparente
		textPanel.setOpaque(true);

		JLabel labelTitulo = new JLabel(pelicula.getTitulo(), SwingConstants.CENTER);
		labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		labelTitulo.setForeground(Color.WHITE);
		labelTitulo.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel labelEdadRecomendada = new JLabel("Edad recomendada: " + pelicula.getEdadRecomendada() + " años",
				SwingConstants.CENTER);
		labelEdadRecomendada.setFont(new Font("Arial", Font.PLAIN, 18));
		labelEdadRecomendada.setForeground(Color.WHITE);

		JPanel panelDetalles = new JPanel();
		panelDetalles.setOpaque(false);
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

		// Añadir los componentes al panel oscuro
		textPanel.add(labelTitulo, BorderLayout.NORTH);
		textPanel.add(labelEdadRecomendada, BorderLayout.CENTER);
		textPanel.add(panelDetalles, BorderLayout.SOUTH);

		// Añadir el panel oscuro a la ventana
		ventanaInfoPelicula.add(textPanel, BorderLayout.SOUTH);
		ventanaInfoPelicula.setVisible(true);

		// Obtener o generar progreso para la serie
		int progresoAleatorio;
		if (progresoPeliculas.containsKey(pelicula.getTitulo())) {
			// Recuperar progreso guardado
			progresoAleatorio = progresoPeliculas.get(pelicula.getTitulo());
		} else {
			// Generar y guardar un progreso aleatorio
			progresoAleatorio = (int) (Math.random() * 101);
			progresoPeliculas.put(pelicula.getTitulo(), progresoAleatorio);
		}

		// Crear la barra de progreso
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(progresoAleatorio); // Configurar el valor de progreso
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(0, 204, 0));
		progressBar.setBackground(new Color(50, 50, 50));
		progressBar.setFont(new Font("Arial", Font.BOLD, 14));
		progressBar.setString(progresoAleatorio + "% visto");

		// Añadir la barra de progreso al panel de detalles
		panelDetalles.add(progressBar, BorderLayout.CENTER);

	}
	
	
	
	
	//el método recibe la serie sobre la que esta el ratón y su punto en la pantalla
	public static void mostrarInfoSerie(Serie serie) {
		if (ventanaInfoSerie != null) {
			ventanaInfoSerie.dispose();
		}

		JButton btnFavoritos = new JButton("Añadir a Favoritos");
		btnFavoritos.setBackground(Trailex_Principal.turquesa);

		if (usuarioActual.esFavorita(serie)) {
			btnFavoritos.setText("Eliminar de Favoritos");
		}

		btnFavoritos.addActionListener(e -> {
			if (usuarioActual.esFavorita(serie)) {
				usuarioActual.eliminarDeFavoritos(serie);
				gestorBD.eliminarSerieFavorita(usuarioActual.getNickname(), serie.getCodigo());
				btnFavoritos.setText("Añadir a Favoritos");

				// Si estamos en la vista de Favoritos, refrescamos la lista
				if (panel_central.getComponentCount() > 0) {
					Component firstComponent = panel_central.getComponent(0);
					if (firstComponent instanceof JLabel && ((JLabel) firstComponent).getText().equals("Favoritos")) {
						mostrarFavoritos();
					}
				}
			} else {
				usuarioActual.agregarAFavoritos(serie);
				gestorBD.añadirSerieFavorita(usuarioActual.getNickname(), serie.getCodigo());
				btnFavoritos.setText("Eliminar de Favoritos");

				if (panel_central.getComponentCount() > 0) {
					Component firstComponent = panel_central.getComponent(0);
					if (firstComponent instanceof JLabel && ((JLabel) firstComponent).getText().equals("Favoritos")) {
						mostrarFavoritos();
					}
				}
			}
		});

		ImageIcon originalIcon = new ImageIcon(serie.getRutaFoto());
		int imageWidth = originalIcon.getIconWidth();
		int imageHeight = originalIcon.getIconHeight();
		double aspectRatio = (double) imageWidth / imageHeight;

		int windowWidth = 400;
		int windowHeight = (int) (windowWidth / aspectRatio);

		ventanaInfoSerie = new JFrame("Información de la Serie");
		ventanaInfoSerie.setSize(windowWidth, windowHeight);
		ventanaInfoSerie.setLayout(new BorderLayout());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width - windowWidth - 50;
		int y = (screenSize.height - windowHeight) / 2;
		ventanaInfoSerie.setLocation(x, y);

		Image image = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
		JLabel background = new JLabel(new ImageIcon(image));
		background.setLayout(new BorderLayout());
		ventanaInfoSerie.setContentPane(background);

		// Panel oscuro para el texto

		JPanel panelDetalles = new JPanel();

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());
		textPanel.setBackground(new Color(0, 0, 0, 150)); // Fondo negro semi-transparente
		textPanel.setOpaque(true);

		JLabel labelTitulo = new JLabel(serie.getTitulo(), SwingConstants.CENTER);
		labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		labelTitulo.setForeground(Color.WHITE);
		labelTitulo.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel labelEdadRecomendada = new JLabel("Edad recomendada: " + serie.getEdadRecomendada() + " años",
				SwingConstants.CENTER);
		labelEdadRecomendada.setFont(new Font("Arial", Font.PLAIN, 18));
		labelEdadRecomendada.setForeground(Color.WHITE);
		panelDetalles.setOpaque(false);
		panelDetalles.setLayout(new GridLayout(5, 1, 5, 5));
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

		// Añadir los componentes al panel oscuro
		textPanel.add(labelTitulo, BorderLayout.NORTH);
		textPanel.add(labelEdadRecomendada, BorderLayout.CENTER);
		textPanel.add(panelDetalles, BorderLayout.SOUTH);

		// Añadir el panel oscuro a la ventana
		ventanaInfoSerie.add(textPanel, BorderLayout.SOUTH);
		ventanaInfoSerie.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ventanaInfoSerie.setVisible(true);

		// Obtener o generar progreso para la serie
		int progresoAleatorio;
		if (progresoSeries.containsKey(serie.getTitulo())) {
			// Recuperar progreso guardado
			progresoAleatorio = progresoSeries.get(serie.getTitulo());
		} else {
			// Generar y guardar un progreso aleatorio
			progresoAleatorio = (int) (Math.random() * 101);
			progresoSeries.put(serie.getTitulo(), progresoAleatorio);
		}

		// Crear la barra de progreso
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(progresoAleatorio); // Configurar el valor de progreso
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(0, 204, 0));
		progressBar.setBackground(new Color(50, 50, 50));
		progressBar.setFont(new Font("Arial", Font.BOLD, 14));
		progressBar.setString(progresoAleatorio + "% visto");

		// MODIFICAR SERIE
		JButton btnmod = new JButton("Modificar");
		btnmod.setBackground(Trailex_Principal.turquesa);
		btnmod.addActionListener(e -> {
			modificar(serie);
			ventanaInfoSerie.dispose();
		});

		// BORRAR SERIE
		JButton btn_borrar = new JButton("BORRAR");
		btn_borrar.setBackground(Color.RED);
		btn_borrar.setForeground(BackgroundColorProgram);
		btn_borrar.addActionListener(e -> {
			borrarSerie(serie);
			ventanaInfoSerie.dispose();
		});

		// Añadir la barra de progreso al panel de detalles
		panelDetalles.add(btnFavoritos);
		panelDetalles.add(btnmod);
		panelDetalles.add(btn_borrar);
		panelDetalles.add(progressBar, BorderLayout.CENTER);

	}

	public static void mostrarFavoritos() { //IAG chatgpt adaptado, ayuda a solucionar errores
		panel_central.removeAll();

		// Título para la sección de Favoritos
		JLabel tituloFavoritos = new JLabel("Favoritos");
		tituloFavoritos.setForeground(Trailex_Principal.turquesa);
		tituloFavoritos.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		tituloFavoritos.setHorizontalAlignment(SwingConstants.CENTER);
		panel_central.add(tituloFavoritos);

		// Panel para las series de favoritos
		JPanel panelFavoritos = new JPanel();
		panelFavoritos.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		panelFavoritos.setBackground(BackgroundColorProgram);

		for (String seriefav : usuarioActual.getFavoritos()) {
			Serie serie = gestorBD.getSeriePorCodigo(seriefav);
			ImageIcon icon = new ImageIcon(serie.getRutaFoto());
			Image img = icon.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
			JLabel lblSerie = new JLabel(new ImageIcon(img));
			lblSerie.setToolTipText(serie.getTitulo());

			// Añadir un borde visual
			lblSerie.setBorder(BorderFactory.createLineBorder(turquesa, 2));

			// Añadir evento de clic para mostrar información de la serie
			lblSerie.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mostrarInfoSerie(serie); // Muestra la información de la serie
					if (ventana_portada != null || ventanaInfoSerie.isActive()) {
						cerrarPortadaSerie();
					}
				}
			});

			// Añadir la etiqueta al panel de favoritos
			panelFavoritos.add(lblSerie);
		}

		// Agregar el panel de favoritos al panel central
		panel_central.add(panelFavoritos);

		// Refrescar la interfaz
		panel_central.revalidate();
		panel_central.repaint();
	}
	
	
	//metodo que muestra la ventana con solo la portada mas grande
	
	public void mostrarPortadaSerie(Serie serie, Component componenteSerie, Point p) {
		if (ventana_portada != null) {
			ventana_portada.dispose();
		}
		
		//IAG
		//SIN CAMBIOS
		ImageIcon originalIcon = new ImageIcon(serie.getRutaFoto());
		int windowWidth = (int)(componenteSerie.getWidth() * 1.5);
	    int windowHeight = (int)(componenteSerie.getHeight() * 1.5);
	    
		ventana_portada = new JFrame("Información de la Serie");
		ventana_portada.setSize(windowWidth, windowHeight);
		ventana_portada.setLayout(new BorderLayout());
	
		int x = p.x - (windowWidth - componenteSerie.getWidth()) / 2;
		int y = p.y - (windowHeight - componenteSerie.getHeight()) / 2;
		ventana_portada.setLocation(x, y);
	
		Image image = originalIcon.getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
	    JLabel lblFoto = new JLabel(new ImageIcon(image));
	    
	    
	    
	    lblFoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarInfoSerie(serie);
				if (ventana_portada != null || ventanaInfoSerie.isActive()) {
					cerrarPortadaSerie();
				}

			}
		});
	    
	    //IAG
	    //SIN CAMBIOS
	 // Agregar un MouseWheelListener para detectar el desplazamiento del ratón
	    lblFoto.addMouseWheelListener(new MouseWheelListener() {
	        @Override
	        public void mouseWheelMoved(MouseWheelEvent e) {
	            // Si el scroll es hacia abajo (positivo)
	            if (e.getWheelRotation() > 0) {
	                cerrarPortadaSerie(); // Cerrar la ventana si se hace scroll hacia abajo
	            }
	        }
	    });
	    
	    
	    
		ventana_portada.setContentPane(lblFoto);
		
		ventana_portada.setUndecorated(true); //para que no tenga la barra de la ventana
		ventana_portada.setVisible(true);
	
	}
	

	public static void modificar(Serie serie) {
		JPanel panel = new JPanel(new GridLayout(6, 2));

		// Etiquetas y campos de texto para cada atributo de Serie

		JTextField tituloField = new JTextField(serie.getTitulo());
		JTextField anioField = new JTextField(String.valueOf(serie.getAnio()));
		JTextField protagonistaField = new JTextField(serie.getProtagonista());
		JTextField edadField = new JTextField(String.valueOf(serie.getEdadRecomendada()));
		JTextField temporadasField = new JTextField(String.valueOf(serie.getNumeroTemporadas()));
		JComboBox<String> generoComboBox = new JComboBox<>(array_generos.toArray(new String[0]));
		generoComboBox.setSelectedItem(serie.getGenero());

		// Agregar componentes al panel

		panel.add(new JLabel("Título:"));
		panel.add(tituloField);
		panel.add(new JLabel("Año:"));
		panel.add(anioField);
		panel.add(new JLabel("Protagonista:"));
		panel.add(protagonistaField);
		panel.add(new JLabel("Edad Recomendada:"));
		panel.add(edadField);
		panel.add(new JLabel("Número de Temporadas:"));
		panel.add(temporadasField);
		panel.add(new JLabel("Género:"));
		panel.add(generoComboBox);

		// Mostrar el cuadro de diálogo
		int result = JOptionPane.showConfirmDialog(null, panel, "Editar Serie", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {

				serie.setTitulo(tituloField.getText().trim());
				serie.setAnio(Integer.parseInt(anioField.getText().trim()));
				serie.setProtagonista(protagonistaField.getText().trim());
				serie.setEdadRecomendada(Integer.parseInt(edadField.getText().trim()));
				serie.setNumeroTemporadas(Integer.parseInt(temporadasField.getText().trim()));
				;
				serie.setGenero((String) generoComboBox.getSelectedItem());

				gestorBD.modificarSerieBD(serie);
				panel_principal.repaint();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al procesar los valores: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void borrarSerie(Serie serie) {
		int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres borrar esta serie?",
				"Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			gestorBD.borrarSerie(serie);
			listaCompletaSeries.remove(listaCompletaSeries.indexOf(serie));
			panel_principal.repaint();
		}
	}

	public void crearSerie() {
		JPanel panel = new JPanel(new GridLayout(6, 2));

		// Campos de texto para los atributos
		JTextField tituloField = new JTextField();
		tituloField.setBackground(turquesa);
		JTextField anioField = new JTextField();
		anioField.setBackground(turquesa);
		JTextField protagonistaField = new JTextField();
		protagonistaField.setBackground(turquesa);
		JTextField edadRecomendadaField = new JTextField();
		edadRecomendadaField.setBackground(turquesa);
		JTextField numeroTemporadasField = new JTextField();
		numeroTemporadasField.setBackground(turquesa);
		JComboBox<String> generoBox = new JComboBox<>(
				new String[] { "Comedia", "Romance", "Aventura", "Drama", "Ciencia Ficción", "Terror" });
		generoBox.setBackground(turquesa);
		
		// Agregar componentes al panel
		panel.add(new JLabel("Título:"));
		panel.add(tituloField);
		panel.add(new JLabel("Año:"));
		panel.add(anioField);
		panel.add(new JLabel("Protagonista:"));
		panel.add(protagonistaField);
		panel.add(new JLabel("Edad Recomendada:"));
		panel.add(edadRecomendadaField);
		panel.add(new JLabel("Número de Temporadas:"));
		panel.add(numeroTemporadasField);
		panel.add(new JLabel("Género:"));
		panel.add(generoBox);

		// Mostrar el cuadro de diálogo
		int result = JOptionPane.showConfirmDialog(null, panel, "Crear Nueva Serie", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				// Crear una nueva instancia de Serie con los datos proporcionados
				String codigo = gestorBD.conseguirCODIGOmasalto();
				String titulo = tituloField.getText().trim();
				int anio = Integer.parseInt(anioField.getText().trim());
				String protagonista = protagonistaField.getText().trim();
				int edadRecomendada = Integer.parseInt(edadRecomendadaField.getText().trim());
				int numeroTemporadas = Integer.parseInt(numeroTemporadasField.getText().trim());
				String genero = generoBox.getSelectedItem().toString();
				String rutaFoto = "resources/images/incognita.jpg";

				Serie serie = new Serie(codigo, titulo, anio, protagonista, edadRecomendada, numeroTemporadas, genero,
						rutaFoto);
				gestorBD.añadirSerie(serie);
				listaCompletaSeries.add(serie);

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Error: Asegúrate de que los campos numéricos son válidos.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
