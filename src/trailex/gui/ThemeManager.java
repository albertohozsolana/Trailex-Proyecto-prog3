package trailex.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import trailex.gui.Trailex_Principal.Tema;

public class ThemeManager { //IAG chatgpt adaptado, ayuda a solucionar errores
    private static final String THEME_FILE = "theme.txt";

    public static Tema cargarTema() {
        try (BufferedReader reader = new BufferedReader(new FileReader(THEME_FILE))) {
            String theme = reader.readLine();
            return Tema.valueOf(theme.toUpperCase());
        } catch (IOException | IllegalArgumentException e) { //Mejora de IAG
            return Tema.BLANCO; // Valor predeterminado si no se encuentra el archivo o hay un error
        }
    }

    public static void guardarTema(Tema tema) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(THEME_FILE))) {
            writer.write(tema.name());
        } catch (IOException e) { //Mmejora de IAG
            e.printStackTrace();
        }
    }
}
