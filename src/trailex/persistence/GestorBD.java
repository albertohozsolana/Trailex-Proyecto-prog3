package trailex.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JLabel;

import trailex.domain.Serie;
import trailex.domain.Usuario;

public class GestorBD {
	
	private final String PROPERTIES_FILE = "resources/config/app.properties";
	private final String CSV_SERIES = "resources/data/series.csv";
	private final String CSV_USUARIOS = "resources/data/Users.csv";
	private final String LOG_FOLDER = "resources/log";
	
	private Properties properties;
	private String driverName;
	private String databaseFile;
	private String connectionString;
	
	private static Logger logger = Logger.getLogger(GestorBD.class.getName());
	
	public GestorBD() {
		try (FileInputStream fis = new FileInputStream("resources/config/logger.properties")) {
			//Inicialización del Logger
			LogManager.getLogManager().readConfiguration(fis);
			
			//Lectura del fichero properties
			properties = new Properties();
			properties.load(new FileReader(PROPERTIES_FILE));
			
			driverName = properties.getProperty("driver");
			databaseFile = properties.getProperty("file");
			connectionString = properties.getProperty("connection");
			
			//Crear carpetas de log si no existe
			File dir = new File(LOG_FOLDER);
			
			if (!dir.exists()) {
				dir.mkdirs();
			}

			//Crear carpeta para la BBDD si no existe
			dir = new File(databaseFile.substring(0, databaseFile.lastIndexOf("/")));
			
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			//Cargar el diver SQLite
			Class.forName(driverName);
		} catch (Exception ex) {
			logger.warning(String.format("Error al cargar el driver de BBDD: %s", ex.getMessage()));
		}
	}
	
	public void crearBBDD() {
		//Sólo se crea la BBDD si la propiedad initBBDD es true.
		if (properties.get("createBBDD").equals("true")) {
			//La base de datos tiene 1 tabla: Serie
			String sql = "CREATE TABLE IF NOT EXISTS Serie (\n"
			           + " codigo TEXT NOT NULL UNIQUE,\n"  // Asegura que los códigos sean únicos
			           + " titulo TEXT NOT NULL,\n"
			           + " anio INTEGER NOT NULL,\n"
			           + " protagonista TEXT NOT NULL,\n"
			           + " edadRecomendada INTEGER NOT NULL,\n"
			           + " numeroTemporadas INTEGER NOT NULL,\n"
			           + " genero TEXT NOT NULL,\n"
			           + " rutaFoto TEXT NOT NULL,\n"
			           + " PRIMARY KEY (codigo) \n"  // Declara el código como clave primaria
			           + ");";

	        //Se abre la conexión y se crea un PreparedStatement para crer cada tabla
			//Al abrir la conexión, si no existía el fichero por defecto, se crea.
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql)) {
				
				//Se ejecutan las sentencias de creación de las tablas
		        if (!pStmt1.execute()) {
		        	logger.info("Se ha creado la tabla");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al crear la tabla: %s", ex.getMessage()));
			}
		}
	}
	
	public void borrarBBDD() {
	    // Sólo se borra la BBDD si la propiedad deleteBBDD es true
	    if (properties.get("deleteBBDD").equals("true")) {
	        // SQL para borrar las tablas Serie y Usuario
	        String sqlBorrarSerie = "DROP TABLE IF EXISTS Serie;";
	        String sqlBorrarUsuario = "DROP TABLE IF EXISTS Usuario;";

	        try (Connection con = DriverManager.getConnection(connectionString)) {
	            // Borrar la tabla Serie
	            try (PreparedStatement pStmtSerie = con.prepareStatement(sqlBorrarSerie)) {
	                if (!pStmtSerie.execute()) {
	                    logger.info("Se ha borrado la tabla Serie.");
	                }
	            }

	            // Borrar la tabla Usuario
	            try (PreparedStatement pStmtUsuario = con.prepareStatement(sqlBorrarUsuario)) {
	                if (!pStmtUsuario.execute()) {
	                    logger.info("Se ha borrado la tabla Usuario.");
	                }
	            }
	        } catch (Exception ex) {
	            logger.warning(String.format("Error al borrar las tablas: %s", ex.getMessage()));
	        }

	        try {
	            // Se borra físicamente el fichero de la BBDD
	            Files.delete(Paths.get(databaseFile));
	            logger.info("Se ha borrado el fichero de la BBDD.");
	        } catch (Exception ex) {
	            logger.warning(String.format("Error al borrar el fichero de la BBDD: %s", ex.getMessage()));
	        }
	    }
	}

	
	public void borrarDatos() {
		//Sólo se borran los datos si la propiedad cleanBBDD es true
		if (properties.get("cleanBBDD").equals("true")) {	
			String sql = "DELETE FROM Serie; VACUUM;";

			
	        //Se abre la conexión y se crea un PreparedStatement para borrar los datos de cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql)) {
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute()) {
		        	logger.info("Se han borrado los datos");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar los datos: %s", ex.getMessage()));
			}
		}
	}
	
	public List<Serie> loadCSVSeries() {
		List<Serie> series = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(CSV_SERIES))) {
			String linea = null;
			//Omitir la cabecera
			in.readLine();		
			
			while ((linea = in.readLine()) != null) {
				String[] campos=linea.split(";");
				
				String codigo=campos[0];
				String titulo=campos[1];
				int anio=Integer.parseInt(campos[2]);
				String protagonista= campos[3];
				int edad_recomendada=Integer.parseInt(campos[4]);
				int numeroTemporadas= Integer.parseInt(campos[5]);
				String genero= campos[6];
				String ruta_img= campos[7];
				
				Serie serie=new Serie(codigo, titulo, anio, protagonista, edad_recomendada,numeroTemporadas, genero, ruta_img);
				series.add(serie);
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo series del CSV: %s", ex.getMessage()));
		}
		
		return series;
	}
	
	public void insertarSeries(Serie... series) {
		//Se define la plantilla de la sentencia SQL
		String sql = "INSERT INTO Serie (codigo, titulo, anio, protagonista, edadRecomendada, numeroTemporadas, genero, rutaFoto) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
									
			//Se recorren las series y se insertan una a una
			for (Serie s : series) {
				//Se añaden los parámetros al PreparedStatement
				pStmt.setString(1, s.getCodigo());
				pStmt.setString(2, s.getTitulo());
				pStmt.setString(3, String.valueOf(s.getAnio()));
				pStmt.setString(4, s.getProtagonista());
				pStmt.setString(5, String.valueOf(s.getEdadRecomendada()));
				pStmt.setString(6, String.valueOf(s.getNumeroTemporadas()));
				pStmt.setString(7, s.getGenero());
				pStmt.setString(8, s.getRutaFoto());
				
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado la Serie: %s", s));
				} else {				
					logger.info(String.format("Se ha insertado la Serie: %s", s));
				}
			}
			
			logger.info(String.format("%d Series insertadas en la BBDD", series.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar series: %s", ex.getMessage()));
		}			
	}
	
	public void initilizeFromCSV() {
		//Sólo se inicializa la BBDD si la propiedad initBBDD es true.
		if (properties.get("loadCSV").equals("true")) {
			//Se borran los datos, si existía alguno
			this.borrarDatos();
			
			//Se leen las series del CSV
			List<Serie> series = this.loadCSVSeries();
			//Se insertan las series en la BBDD
			this.insertarSeries(series.toArray(new Serie[series.size()]));
				
		}
	}
	
	public Serie getSerieporTitulo(String nombre) {
		Serie serie = null;
		String sql = "SELECT * FROM Serie WHERE titulo = ? LIMIT 1";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			//Se definen los parámetros de la sentencia SQL
			pStmt.setString(1, nombre);
			
			//Se ejecuta la sentencia y se obtiene el ResultSet con los resutlados
			ResultSet rs = pStmt.executeQuery();			

			//Se procesa el único resultado
			if (rs.next()) {
				serie = new Serie(rs.getString("codigo"), 
						rs.getString("titulo"), 
						rs.getInt("anio"),
						rs.getString("protagonista"),
						rs.getInt("edadRecomendada"),
						rs.getInt("numeroTemporadas"),
						rs.getString("genero"),
						rs.getString("rutaFoto")
						);
			}
			
			//Se cierra el ResultSet
			rs.close();
			
			logger.info(String.format("Se ha recuperado la serie %s", serie));			
		} catch (Exception ex) {
			logger.warning(String.format("Error recuperar la serie con nombre %s: %s", nombre, ex.getMessage()));						
		}		
		
		return serie;
	}
	
	public void guardarSeriesCSV(List<Serie> series) {
		if (series != null) {			
			try (PrintWriter out = new PrintWriter(new File(CSV_SERIES))) {
				out.println("codigo;titulo;anio;protagonista;edadRecomendada;numeroTemporadas;genero;rutaFoto");
				series.forEach(s -> out.println(Serie.toCSV(s)));			
				logger.info("Se han guardado los comics en un CSV.");
			} catch (Exception ex) {
				logger.warning(String.format("Error guardando comics en el CSV: %s", ex.getMessage()));
			}
		}
	}
	
	public void modificarSerieBD(Serie serie) {
		String sql = "UPDATE Serie SET titulo = ?, anio = ?, protagonista = ?, edadRecomendada = ?, numeroTemporadas = ?, genero = ?, rutaFoto = ? WHERE codigo = ?;";

     //Se abre la conexión y se crea un PreparedStatement
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {
				pStmt.setString(1, serie.getTitulo());
				pStmt.setInt(2, serie.getAnio());
				pStmt.setString(3, serie.getProtagonista());
				pStmt.setInt(4, serie.getEdadRecomendada());
				pStmt.setInt(5, serie.getNumeroTemporadas());
				pStmt.setString(6, serie.getGenero());
				pStmt.setString(7, serie.getRutaFoto());
				pStmt.setString(8, serie.getCodigo());
				
				int rowsAffected = pStmt.executeUpdate();
				
				// Comprobamos si la actualización fue exitosa
		        if (rowsAffected > 0) {
		            logger.info(String.format("La serie con código %s ha sido actualizada correctamente.", serie.getCodigo()));
		        } else {
		            logger.warning(String.format("No se encontró la serie con el código %s para actualizar.", serie.getCodigo()));
		        }
			
		} catch (Exception ex) {
			logger.warning(String.format("Error modificar la serie: %s", ex.getMessage()));
		}
	}
	
	public void borrarSerie(Serie serie) {
	    String sql = "DELETE FROM Serie WHERE codigo = ?";

	     //Se abre la conexión y se crea un PreparedStatement
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt = con.prepareStatement(sql)) {
					
				pStmt.setString(1, serie.getCodigo());

		        // Ejecutamos la sentencia de eliminación
		        int rowsAffected = pStmt.executeUpdate();

		        // Comprobamos si la eliminación fue exitosa
		        if (rowsAffected > 0) {
		            logger.info(String.format("La serie con código %s ha sido borrada correctamente.", serie.getCodigo()));
		        } else {
		            logger.warning(String.format("No se encontró la serie con el código %s para borrar.", serie.getCodigo()));
		        }
					
				
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar la serie: %s", ex.getMessage()));
			}
	}
	
	public void añadirSerie(Serie serie) {
	    String sql = "INSERT INTO Serie (codigo, titulo, anio, protagonista, edadRecomendada, numeroTemporadas, genero, rutaFoto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	     //Se abre la conexión y se crea un PreparedStatement
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt = con.prepareStatement(sql)) {
					
				pStmt.setString(1, serie.getCodigo());

				// Asignar valores a los parámetros
		        pStmt.setString(1, serie.getCodigo());
		        pStmt.setString(2, serie.getTitulo());
		        pStmt.setInt(3, serie.getAnio());
		        pStmt.setString(4, serie.getProtagonista());
		        pStmt.setInt(5, serie.getEdadRecomendada());
		        pStmt.setInt(6, serie.getNumeroTemporadas());
		        pStmt.setString(7, serie.getGenero());
		        pStmt.setString(8, serie.getRutaFoto());

		     // Ejecutar la sentencia
		        int rowsAffected = pStmt.executeUpdate();

		        // Comprobar si la inserción fue exitosa
		        if (rowsAffected > 0) {
		            logger.info(String.format("La serie con código %s ha sido añadida correctamente.", serie.getCodigo()));
		        } else {
		            logger.warning(String.format("No se pudo añadir la serie con el código %s.", serie.getCodigo()));
		        }
					
				
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar la serie: %s", ex.getMessage()));
			}
	}
	
	public String conseguirCODIGOmasalto() {
		String sql = "SELECT MAX(codigo) AS maxCodigo FROM Serie";
	    String maxCodigo = null;

	    // Se abre la conexión y se crea un PreparedStatement
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql);
	         ResultSet rs = pStmt.executeQuery()) {

	        // Si hay un resultado, obtenemos el valor del código más alto
	        if (rs.next()) {
	            maxCodigo = rs.getString("maxCodigo");
	        }

	    } catch (Exception ex) {
	        logger.warning(String.format("Error al obtener el código más alto: %s", ex.getMessage()));
	    }
	    
	    String resultado = String.valueOf(1 + Integer.parseInt(maxCodigo));
	    return resultado; // Devuelve el código más alto o null si no se encontró
	}
	
	public void crearTablaUsuario() {
		//Sólo se crea la BBDD si la propiedad initBBDD es true.
		if (properties.get("createBBDD").equals("true")) {
			//La base de datos tiene 1 tabla: Serie
			String sql = "CREATE TABLE IF NOT EXISTS Usuario (\n"
	                   + " nickname TEXT NOT NULL UNIQUE,\n"
	                   + " email TEXT NOT NULL,\n"
	                   + " password TEXT NOT NULL,\n"
	                   + " favoritos TEXT NOT NULL,\n" // Almacena favoritos como un string (JSON o separado por comas)
	                   + " PRIMARY KEY (nickname)\n"
	                   + ");";

	        //Se abre la conexión y se crea un PreparedStatement para crer cada tabla
			//Al abrir la conexión, si no existía el fichero por defecto, se crea.
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt2 = con.prepareStatement(sql)) {
				
				//Se ejecutan las sentencias de creación de las tablas
		        if (!pStmt2.execute()) {
		        	logger.info("Se ha creado la tabla");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al crear la tabla: %s", ex.getMessage()));
			}
		}
	}
	
	public List<Usuario> cargarUsuariosDesdeCSV() {
	    List<Usuario> usuarios = new ArrayList<>();

	    try (BufferedReader in = new BufferedReader(new FileReader(CSV_USUARIOS))) {
	        String linea;
	        in.readLine(); // Omitir la cabecera

	        while ((linea = in.readLine()) != null) {
	            String[] campos = linea.split(",", 4); // Dividir la línea en un máximo de 4 partes
	            if (campos.length == 4) {
	                String nickname = campos[0].replace("\"", "").trim();
	                String password = campos[1].trim();
	                String email = campos[2].trim();
	                String favoritosString = campos[3].trim();

	             // Eliminar las comillas dobles externas y los corchetes
	             favoritosString = favoritosString.replace("\"", "").replace("[", "").replace("]", "").replace("'", "").trim();

	             // Dividir por comas y procesar cada título
	             String[] favoritosArray = favoritosString.split(",\\s*"); // Divide por comas con espacios opcionales

	             ArrayList<String> favoritos = new ArrayList<>();
	             for (String codigo : favoritosArray) {
	                 codigo = codigo.trim(); // Eliminar espacios adicionales
	                 Serie serie = this.getSeriePorCodigo(codigo);
	                 if (serie != null) {
	                     favoritos.add(serie.getCodigo());
	                 } else {
	                     System.err.println("No se encontró la serie con código: " + codigo);
	                 }
	             }

	                // Crear el objeto Usuario y añadirlo a la lista
	                Usuario usuario = new Usuario(nickname, email, password, favoritos);
	                usuarios.add(usuario);
	            }
	        }
	    } catch (Exception ex) {
	        System.err.println("Error leyendo usuarios desde el CSV: " + ex.getMessage());
	    }

	    return usuarios;
	}
	
	public void insertarUsuarios(List<Usuario> usuarios) {
        String sql = "INSERT INTO Usuario (nickname, email, password, favoritos) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(connectionString);
             PreparedStatement pStmt = con.prepareStatement(sql)) {

            for (Usuario usuario : usuarios) {
                pStmt.setString(1, usuario.getNickname());
                pStmt.setString(2, usuario.getEmail());
                pStmt.setString(3, usuario.getContraseña());
                String favoritosString = String.join(", ", usuario.getFavoritos());
                pStmt.setString(4, favoritosString);

                try {
                    if (pStmt.executeUpdate() == 1) {
                        logger.info(String.format("Usuario %s insertado correctamente.", usuario.getNickname()));
                    } else {
                        logger.warning(String.format("No se pudo insertar el usuario %s.", usuario.getNickname()));
                    }
                } catch (Exception ex) {
                    logger.warning(String.format("Error al insertar usuario %s: %s", usuario.getNickname(), ex.getMessage()));
                }
            }

        } catch (Exception ex) {
            logger.warning(String.format("Error al insertar usuarios: %s", ex.getMessage()));
        }
    }
	
	public boolean esUsuarioValido(String nickname, String password) {
	    String sql = "SELECT COUNT(*) FROM Usuario WHERE nickname = ? AND password = ?";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {

	        // Configurar los parámetros de la consulta
	    	pStmt.setString(1, nickname.trim());
	    	pStmt.setString(2, password.trim());
	    	System.out.println("Comparando usuario: '" + nickname + "' y contraseña: '" + password + "'");

	        // Ejecutar la consulta y obtener el resultado
	        ResultSet rs = pStmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Devuelve true si hay al menos un resultado
	        }
	    } catch (Exception ex) {
	        System.err.println("Error al validar usuario: " + ex.getMessage());
	    }

	    return false; // Devuelve false si algo falla o no hay coincidencias
	}
	
	public Serie getSeriePorCodigo(String codigo) {
	    Serie serie = null;
	    String sql = "SELECT * FROM Serie WHERE codigo = ? LIMIT 1";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {

	        // Ajustar el código al formato de tres dígitos
	        String codigoFormateado = String.format("%03d", Integer.parseInt(codigo));

	        // Configurar el parámetro del código
	        pStmt.setString(1, codigoFormateado);

	        // Ejecutar la consulta
	        ResultSet rs = pStmt.executeQuery();

	        // Procesar el resultado
	        if (rs.next()) {
	            serie = new Serie(
	                rs.getString("codigo"),
	                rs.getString("titulo"),
	                rs.getInt("anio"),
	                rs.getString("protagonista"),
	                rs.getInt("edadRecomendada"),
	                rs.getInt("numeroTemporadas"),
	                rs.getString("genero"),
	                rs.getString("rutaFoto")
	            );
	        }

	        // Cerrar el ResultSet
	        rs.close();

	    } catch (NumberFormatException ex) {
	        logger.warning(String.format("El código %s no es un número válido: %s", codigo, ex.getMessage()));
	    } catch (Exception ex) {
	        logger.warning(String.format("Error al recuperar la serie con código %s: %s", codigo, ex.getMessage()));
	    }

	    return serie;
	}

	
	public Usuario getUsuarioPorNickname(String nickname) {
	    Usuario usuario = null;
	    String sql = "SELECT * FROM Usuario WHERE nickname = ? LIMIT 1";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {

	        // Configurar el parámetro del nickname
	        pStmt.setString(1, nickname);

	        // Ejecutar la consulta
	        ResultSet rs = pStmt.executeQuery();

	        // Procesar el resultado
	        if (rs.next()) {
	            // Recuperar la lista de códigos de favoritos como un string y dividirlo en un ArrayList<String>
	            String favoritosString = rs.getString("favoritos");
	            ArrayList<String> favoritos = new ArrayList<>(Arrays.asList(favoritosString.split(",\\s*"))); // Dividir por comas y espacios

	            // Crear el objeto Usuario con el ArrayList<String> de códigos
	            usuario = new Usuario(
	                rs.getString("nickname"),
	                rs.getString("email"),
	                rs.getString("password"),
	                favoritos
	            );
	        }

	        // Cerrar el ResultSet
	        rs.close();

	    } catch (Exception ex) {
	        logger.warning(String.format("Error al recuperar el usuario con nickname %s: %s", nickname, ex.getMessage()));
	    }

	    return usuario;
	}
	
	public ArrayList<String> cargarRutasFotos() {
	    ArrayList<String> rutasFotos = new ArrayList<>();
	    String sql = "SELECT rutaFoto FROM Serie";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql);
	         ResultSet rs = pStmt.executeQuery()) {

	        // Iterar sobre los resultados y añadir las rutas a la lista
	        while (rs.next()) {
	            rutasFotos.add(rs.getString("rutaFoto"));
	        }
	        logger.info("Se han cargado las rutas de fotos desde la base de datos.");

	    } catch (Exception ex) {
	        logger.warning(String.format("Error al cargar las rutas de fotos: %s", ex.getMessage()));
	    }

	    return rutasFotos;
	}
	
	public ArrayList<String> cargarTitulosSeries() {
	    ArrayList<String> titulos = new ArrayList<>();
	    String sql = "SELECT titulo FROM Serie";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql);
	         ResultSet rs = pStmt.executeQuery()) {

	        // Iterar sobre los resultados y añadir las rutas a la lista
	        while (rs.next()) {
	            titulos.add(rs.getString("titulo"));
	        }
	        logger.info("Se han cargado los titulos desde la base de datos.");

	    } catch (Exception ex) {
	        logger.warning(String.format("Error al cargar los titulos: %s", ex.getMessage()));
	    }

	    return titulos;
	}
	
	public Serie getSeriePorTitulo(String titulo) {
	    Serie serie = null;
	    String sql = "SELECT * FROM Serie WHERE titulo = ? LIMIT 1";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {


	        // Configurar el parámetro del código
	        pStmt.setString(1, titulo);

	        // Ejecutar la consulta
	        ResultSet rs = pStmt.executeQuery();

	        // Procesar el resultado
	        if (rs.next()) {
	            serie = new Serie(
	                rs.getString("codigo"),
	                rs.getString("titulo"),
	                rs.getInt("anio"),
	                rs.getString("protagonista"),
	                rs.getInt("edadRecomendada"),
	                rs.getInt("numeroTemporadas"),
	                rs.getString("genero"),
	                rs.getString("rutaFoto")
	            );
	        }

	        // Cerrar el ResultSet
	        rs.close();

	    } catch (NumberFormatException ex) {
	        logger.warning(String.format("El código %s no es un número válido: %s", titulo, ex.getMessage()));
	    } catch (Exception ex) {
	        logger.warning(String.format("Error al recuperar la serie con código %s: %s", titulo, ex.getMessage()));
	    }

	    return serie;
	}
	
	public void añadirSerieFavorita(String nickname, String codigoSerie) {
	    String sqlSelect = "SELECT favoritos FROM Usuario WHERE nickname = ?";
	    String sqlUpdate = "UPDATE Usuario SET favoritos = ? WHERE nickname = ?";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement selectStmt = con.prepareStatement(sqlSelect);
	         PreparedStatement updateStmt = con.prepareStatement(sqlUpdate)) {

	        // Obtener la lista actual de favoritos
	        selectStmt.setString(1, nickname);
	        ResultSet rs = selectStmt.executeQuery();

	        if (rs.next()) {
	            String favoritos = rs.getString("favoritos");
	            List<String> listaFavoritos = new ArrayList<>();
	            if (favoritos != null && !favoritos.isEmpty()) {
	                listaFavoritos = new ArrayList<>(List.of(favoritos.split(",")));
	            }

	            // Añadir la nueva serie si no existe ya
	            if (!listaFavoritos.contains(codigoSerie)) {
	                listaFavoritos.add(codigoSerie);
	                String nuevosFavoritos = String.join(",", listaFavoritos);

	                // Actualizar la base de datos
	                updateStmt.setString(1, nuevosFavoritos);
	                updateStmt.setString(2, nickname);
	                updateStmt.executeUpdate();
	                logger.info(String.format("Serie con código %s añadida a favoritos del usuario %s", codigoSerie, nickname));
	            } else {
	                logger.warning(String.format("La serie con código %s ya está en los favoritos del usuario %s", codigoSerie, nickname));
	            }
	        }
	        rs.close();
	    } catch (Exception ex) {
	        logger.warning(String.format("Error al añadir serie favorita para el usuario %s: %s", nickname, ex.getMessage()));
	    }
	}
	
	public void eliminarSerieFavorita(String nickname, String codigoSerie) {
		System.out.println(nickname);
		System.out.println(codigoSerie);
	    String sqlSelect = "SELECT favoritos FROM Usuario WHERE nickname = ?";
	    String sqlUpdate = "UPDATE Usuario SET favoritos = ? WHERE nickname = ?";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement selectStmt = con.prepareStatement(sqlSelect);
	         PreparedStatement updateStmt = con.prepareStatement(sqlUpdate)) {

	        // Obtener la lista actual de favoritos
	        selectStmt.setString(1, nickname);
	        System.out.println(selectStmt);
	        ResultSet rs = selectStmt.executeQuery();

	        if (rs.next()) {
	            String favoritos = rs.getString("favoritos");
	            List<String> listaFavoritos = new ArrayList<>();
	            if (favoritos != null && !favoritos.isEmpty()) {
	                listaFavoritos = new ArrayList<>(List.of(favoritos.split(",")));
	            }

	            // Eliminar la serie si existe
	            if (listaFavoritos.contains(codigoSerie)) {
	                listaFavoritos.remove(codigoSerie);
	                String nuevosFavoritos = String.join(",", listaFavoritos);

	                // Actualizar la base de datos
	                updateStmt.setString(1, nuevosFavoritos);
	                updateStmt.setString(2, nickname);
	                System.out.println(updateStmt);
	                updateStmt.executeUpdate();
	                logger.info(String.format("Serie con código %s eliminada de favoritos del usuario %s", codigoSerie, nickname));
	            } else {
	                logger.warning(String.format("La serie con código %s no está en los favoritos del usuario %s", codigoSerie, nickname));
	            }
	        }
	        rs.close();
	    } catch (Exception ex) {
	        logger.warning(String.format("Error al eliminar serie favorita para el usuario %s: %s", nickname, ex.getMessage()));
	    }
	}



}