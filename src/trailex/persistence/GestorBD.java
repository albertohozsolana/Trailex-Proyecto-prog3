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
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JLabel;

import trailex.domain.Serie;

public class GestorBD {
	
	private final String PROPERTIES_FILE = "resources/config/app.properties";
	private final String CSV_SERIES = "resources/data/series.csv";
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
		//Sólo se borra la BBDD si la propiedad deleteBBDD es true
		if (properties.get("deleteBBDD").equals("true")) {	
			String sql = "DROP TABLE IF EXISTS Serie;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar la tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql)) {
				
				//Se ejecutan la sentencia de borrado
		        if (!pStmt1.execute()) {
		        	logger.info("Se ha borrado la tabla");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar la tabla: %s", ex.getMessage()));
			}
			
			try {
				//Se borra físicamente el fichero de la BBDD
				Files.delete(Paths.get(databaseFile));
				logger.info("Se ha borrado el fichero de la BBDD");
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
}