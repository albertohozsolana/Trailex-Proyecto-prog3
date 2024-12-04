package trailex.domain;

import java.util.ArrayList;

public class Usuario {
	private String nombre;
    private String email;
    private String contraseña;
    private ArrayList<Serie> favoritos;

    public Usuario(String nombre, String email, String contraseña) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.favoritos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public ArrayList<Serie> getFavoritos() {
        return favoritos;
    }

    public void agregarAFavoritos(Serie serie) {
        if (!favoritos.contains(serie)) {
            favoritos.add(serie);
            System.out.println("Serie añadida a favoritos: " + serie.getTitulo());
        } else {
            System.out.println("La serie ya está en favoritos.");
        }
    }

    public void eliminarDeFavoritos(Serie serie) {
        if (favoritos.remove(serie)) {
            System.out.println("Serie eliminada de favoritos: " + serie.getTitulo());
        } else {
            System.out.println("La serie no estaba en favoritos.");
        }
    }

    public boolean esFavorita(Serie serie) {
        return favoritos.contains(serie);
    }
}
