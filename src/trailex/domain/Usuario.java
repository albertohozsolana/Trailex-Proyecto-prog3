package trailex.domain;

import java.util.ArrayList;

public class Usuario {
	private String nickname;
    private String email;
    private String contraseña;
    private ArrayList<String> favoritos;

    public Usuario(String nickname, String email, String contraseña) {
        this.nickname = nickname;
        this.email = email;
        this.contraseña = contraseña;
        this.favoritos = new ArrayList<>();
    }
    
    public Usuario(String nickname, String email, String contraseña, ArrayList<String> favoritos) {
        this.nickname = nickname;
        this.email = email;
        this.contraseña = contraseña;
        this.favoritos = favoritos;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNombre(String nickname) {
        this.nickname = nickname;
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

    public ArrayList<String> getFavoritos() {
        return favoritos;
    }
    
    public void setFavoritos(ArrayList<String> favoritos) {
    	this.favoritos = favoritos;
    }

    public void agregarAFavoritos(Serie serie) {
        if (!favoritos.contains(serie.getCodigo())) {
            favoritos.add(serie.getCodigo());
            System.out.println("Serie añadida a favoritos: " + serie.getTitulo());
        } else {
            System.out.println("La serie ya está en favoritos.");
        }
    }

    public void eliminarDeFavoritos(Serie serie) {
        if (favoritos.remove(serie.getCodigo())) {
            System.out.println("Serie eliminada de favoritos: " + serie.getTitulo());
        } else {
            System.out.println("La serie no estaba en favoritos.");
        }
    }

    public boolean esFavorita(Serie serie) {
        return favoritos.contains(serie.getCodigo());
    }
}
