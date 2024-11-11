package trailex.domain;

public class Serie {
	private String codigo;
	private String titulo;
	private String protagonista;
	private int anio;
	private int edadRecomendada;
	private int numeroTemporadas;
	private String genero;
	private String rutaFoto;
	
	public Serie() {
		super();
	}
	public Serie(String codigo, String titulo, int anio, String protagonista, int edadRecomendada, int numeroTemporadas, String genero, String rutaFoto) {
		super();
		this.codigo = codigo;
		this.titulo = titulo;
		this.protagonista = protagonista;
		this.anio = anio;
		this.edadRecomendada = edadRecomendada;
		this.numeroTemporadas= numeroTemporadas;
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
	
	
	public int getNumeroTemporadas() {
		return numeroTemporadas;
	}
	public void setNumeroTemporadas(int numeroTemporadas) {
		this.numeroTemporadas = numeroTemporadas;
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
		return "Serie [codigo=" + codigo + ", titulo=" + titulo + ", protagonista=" + protagonista + ", anio=" + anio
				+ ", edadRecomendada=" + edadRecomendada + ", numeroTemporadas=" + numeroTemporadas + ", genero="
				+ genero + ", rutaFoto=" + rutaFoto + "]";
	}
	

	
}
