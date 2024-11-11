package trailex.elementos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Videoclub {
	private static ArrayList<Pelicula> alPeliculas = new ArrayList<>();
	private static ArrayList<Serie> alSeries = new ArrayList<>();
	
	
	public static void cargarSeries() {
		ArrayList<Serie> array_series= new ArrayList<Serie>();
		ArrayList<Serie> series = Videoclub.getAlSeries();

		//CARGAR SERIES
		try {
	
			File f= new File("src/series.csv");
			Scanner sc = new Scanner(f);
			
			if (sc.hasNextLine()) {
				sc.nextLine();
			}
			
			while(sc.hasNextLine()) {
				String linea=sc.nextLine();
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
				array_series.add(serie);
			}
			sc.close();

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Serie serie : array_series) {
			alSeries.add(serie);
		}

	}
	

	
	public static ArrayList<Pelicula> getAlPeliculas() {
		return alPeliculas;
	}

	public static ArrayList<Serie> getAlSeries() {
		return alSeries;
	}
	public static void agregarSerie(Serie serie) {
        alSeries.add(serie);
    }
	//Método que, dada la ruta de la foto de una película devuelve el objeto Pelicula con esa rutaFoto
	public static Serie buscarSerie(String rutaFoto) {
		boolean enc = false;
		int pos = 0; //La primera posición de un ArrayList es la 0
		Serie s = null;
		while(!enc && pos<alSeries.size()) { //Mientras no hayamos encontrado la película y no hayamos recorrido toda la lista, seguimos buscando
			s = alSeries.get(pos); //Guardamos en p la película de la posición pos de la lista
			if(s.getRutaFoto().equals(rutaFoto)) { //Si la ruta de la película p es igual a la que estoy buscando
				enc = true;
			}else {
				pos++;
			}
		}
		if(enc) {
			return s;
		}else {
			return null;
		}
	}
}
