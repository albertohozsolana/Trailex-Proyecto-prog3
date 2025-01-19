package trailex.domain;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class NotificadorRecordatorio { //IAG chatgpt adaptado ayuda a solucionar errores
    private GestorRecordatorio gestorRecordatorios;

    public NotificadorRecordatorio(GestorRecordatorio gestorRecordatorios) { 
        this.gestorRecordatorios = gestorRecordatorios;
        iniciarNotificaciones();
    }

    private void iniciarNotificaciones() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Comprobar recordatorios y notificar
                gestorRecordatorios.obtenerRecordatoriosPorUsuario("usuarioEjemplo").forEach(recordatorio -> {
                    if (recordatorio.getFechaHora().isBefore(LocalDateTime.now())) {
                        System.out.println("¡Es hora de ver tu película o serie: " + recordatorio.getTitulo() + "!");
                    }
                });
            }
        }, 0, 60000); // comprobar cada minuto
    }
}
