package trailex.elementos;

import java.util.ArrayList;


public class Videoclub {
	private static ArrayList<Pelicula> alPeliculas = new ArrayList<>();
	
	public static void cargarPeliculas() {
		//añadir codigo!!
	}
	
	
	
	public static ArrayList<Pelicula> getAlPeliculas() {
		return alPeliculas;
	}



	//Método que, dada la ruta de la foto de una película devuelve el objeto Pelicula con esa rutaFoto
	public static Pelicula buscarPelicula(String rutaFoto) {
		boolean enc = false;
		int pos = 0; //La primera posición de un ArrayList es la 0
		Pelicula p = null;
		while(!enc && pos<alPeliculas.size()) { //Mientras no hayamos encontrado la película y no hayamos recorrido toda la lista, seguimos buscando
			p = alPeliculas.get(pos); //Guardamos en p la película de la posición pos de la lista
			if(p.getRutaFoto().equals(rutaFoto)) { //Si la ruta de la película p es igual a la que estoy buscando
				enc = true;
			}else {
				pos++;
			}
		}
		if(enc) {
			return p;
		}else {
			return null;
		}
	}
}