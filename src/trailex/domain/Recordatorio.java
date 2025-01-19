package trailex.domain;

import java.time.LocalDateTime;

public class Recordatorio {
    private String titulo;
    private LocalDateTime fechaHora;
    private String idUsuario;

    public Recordatorio(String titulo, LocalDateTime fechaHora, String idUsuario) {
        this.titulo = titulo;
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Recordatorio{" +
                "titulo='" + titulo + '\'' +
                ", fechaHora=" + fechaHora +
                ", idUsuario='" + idUsuario + '\'' +
                '}';
    }
}
