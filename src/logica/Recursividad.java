package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import trailex.domain.Pelicula;
import trailex.domain.Serie;
import trailex.domain.Videoclub;

public class Recursividad {

	// CREAMOS UNA FUNCION QUE LLAMA A UN MÉTODO RECURSIVO PARA PODER GENERAR COMBINACIONES DE SERIES POR GÉNERO
	public static List<List<Serie>> combinacionesPorGenero(List<Serie> series, String genero, int n_series) {
		List<List<Serie>> resultado = new ArrayList<>();
		combinacionesPorGeneroR(series, genero, n_series, new ArrayList<>(),resultado, 0);
		
		if (resultado.isEmpty()) {
			System.out.println("No se han encontrado " + n_series + " series para el género " + genero);
			return null;
		} else {
			return resultado;
		}

	}
	
	public static void combinacionesPorGeneroR(List<Serie> series, String genero, int n_series, List<Serie> temp, List<List<Serie>> resultado, int start) {
		if (temp.size()==n_series) {
			List<Serie> ordenada = new ArrayList<>(temp);
			
			ordenada.sort(new Comparator<Serie>() {

				@Override
				public int compare(Serie o1, Serie o2) {
					return Integer.compare(Integer.valueOf(o1.getCodigo()), Integer.valueOf(o2.getCodigo()));
				}
			});
			
			if (!resultado.contains(ordenada)) {
				resultado.add(ordenada);
				return;
			}
			return;
		}

		else {
			for (int i=start; i<series.size(); i++) {
				Serie primera_serie = series.get(i);

				if (primera_serie.getGenero().equalsIgnoreCase(genero)) {
					temp.add(primera_serie);
					combinacionesPorGeneroR(series, genero, n_series, temp, resultado, i+1);
					temp.remove(temp.size()-1);
				}
			}
		}
		
		
	}
	
	// CREAMOS UNA FUNCION QUE LLAMA A UN MÉTODO RECURSIVO PARA PODER GENERAR COMBINACIONES DE SERIES EN FUNCIÓN DEL NÚMERO DE TEMPORADAS
	public static List<List<Serie>> combinacionesPorTemporada(List<Serie> series, int n_temporadas) {
		List<List<Serie>> resultado = new ArrayList<>();
		
		combinacionesPorTempR(series, n_temporadas, new ArrayList<Serie>(), resultado);
		
		if (resultado.isEmpty()) {
			System.out.println("No se han encontrado ninguna serie con " + n_temporadas + " temporadas.");
			return null;
		} else {
			return resultado;
		}

	}
	
	public static void combinacionesPorTempR(List<Serie> series, int n_temporadas, List<Serie> temp, List<List<Serie>> resultado) {
		if (num_temporadas(temp) == n_temporadas) {
			List<Serie> ordenada = new ArrayList<>(temp);
			
			ordenada.sort(new Comparator<Serie>() {

				@Override
				public int compare(Serie o1, Serie o2) {
					return Integer.compare(Integer.valueOf(o1.getCodigo()), Integer.valueOf(o2.getCodigo()));
				}
			});
			
			if (!resultado.contains(ordenada)) {
				resultado.add(ordenada);
			}
			
		} else if (num_temporadas(temp) > n_temporadas) {
	        return;
		} else {
			for (Serie s : series) {
				if (num_temporadas(temp) < n_temporadas) {
					temp.add(s);
					combinacionesPorTempR(series, n_temporadas, temp, resultado);
					temp.remove(temp.size()-1);
				}
			}
		}
		
		
		
	}
	
	
	public static int num_temporadas(List<Serie> series) {
		int total = 0;
		
		for (Serie s : series) {
			total = total + s.getNumeroTemporadas();
		}
		
		return total;
	}

}
