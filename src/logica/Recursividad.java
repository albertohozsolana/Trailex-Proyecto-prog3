package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import trailex.domain.Pelicula;
import trailex.domain.Serie;
import trailex.domain.Videoclub;

public class Recursividad {


	public static void combinacionesPorGenero(List<Serie> series, String genero, int n_series, int n_combinaciones) {
		List<List<Serie>> resultado = new ArrayList<>();
		combinacionesPorGeneroR(series, genero, n_series, n_combinaciones, new ArrayList<>(),resultado, 0);
		
		if (resultado.isEmpty()) {
			System.out.println("No se han encontrado " + n_combinaciones + " combinaciones de "+ n_series + " series para el género " + genero);
		}
		for (List<Serie> list : resultado) {
			System.out.println(list + "\n");
		}

	}
	
	public static void combinacionesPorGeneroR(List<Serie> series, String genero, int n_series, int n_combinaciones, List<Serie> temp, List<List<Serie>> resultado, int start) {
		if (temp.size()==n_series) {
			List<Serie> ordenada = new ArrayList<>(temp);
			
			ordenada.sort(new Comparator<Serie>() {

				@Override
				public int compare(Serie o1, Serie o2) {
					return Integer.compare(Integer.valueOf(o1.getCodigo()), Integer.valueOf(o2.getCodigo()));
				}
			});
			
			if (!resultado.contains(ordenada) && resultado.size()<n_combinaciones) {
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
					combinacionesPorGeneroR(series, genero, n_series, n_combinaciones, temp, resultado, i+1);
					temp.remove(temp.size()-1);
				}
			}
		}
		
		
	}
	
	
	

}
