package trailex.domain;
import trailex.domain.Recordatorio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorRecordatorio {  //IAG chatgpt adaptado, ayuda a solucionar errores
    private List<Recordatorio> recordatorios;

    public GestorRecordatorio() {
        this.recordatorios = new ArrayList<>();
    }

    public void añadirRecordatorio(String titulo, LocalDateTime fechaHora, String idUsuario) {
        Recordatorio nuevoRecordatorio = new Recordatorio(titulo, fechaHora, idUsuario);
        recordatorios.add(nuevoRecordatorio);
        System.out.println("Recordatorio añadido: " + nuevoRecordatorio);
    }

    public void eliminarRecordatorio(String titulo, String idUsuario) {
        recordatorios.removeIf(recordatorio -> recordatorio.getTitulo().equals(titulo) && recordatorio.getIdUsuario().equals(idUsuario));
        System.out.println("Recordatorio eliminado para la película/serie: " + titulo);
    }

    public List<Recordatorio> obtenerRecordatoriosPorUsuario(String idUsuario) {
        return recordatorios.stream()
                            .filter(recordatorio -> recordatorio.getIdUsuario().equals(idUsuario))
                            .collect(Collectors.toList());
    }
}
