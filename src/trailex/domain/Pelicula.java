package trailex.domain;

public class Pelicula {
	private String codigo;
	private String titulo;
	private String protagonista;
	private int anio;
	private int edadRecomendada;
	private String genero;
	private String rutaFoto;
	
	public Pelicula() {
		super();
	}
	public Pelicula(String codigo, String titulo, int anio, String protagonista, int edadRecomendada, String genero, String rutaFoto) {
		super();
		this.codigo = codigo;
		this.titulo = titulo;
		this.protagonista = protagonista;
		this.anio = anio;
		this.edadRecomendada = edadRecomendada;
		this.genero=genero;
		this.rutaFoto = rutaFoto;
	}
	
	
	public String getRutaFoto() {
		return rutaFoto;
	}
	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getProtagonista() {
		return protagonista;
	}
	public void setProtagonista(String protagonista) {
		this.protagonista = protagonista;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	
	
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public int getEdadRecomendada() {
		return edadRecomendada;
	}
	public void setEdadRecomendada(int edadRecomendada) {
		this.edadRecomendada = edadRecomendada;
	}
	
	@Override
	public String toString() {
		return "Pelicula [codigo=" + codigo + ", titulo=" + titulo + ", protagonista=" + protagonista + ", anio=" + anio
				+ ", edadRecomendada=" + edadRecomendada + ", genero=" + genero + ", rutaFoto=" + rutaFoto + "]";
	}

}
