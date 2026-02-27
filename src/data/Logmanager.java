package data;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logmanager {
    private static final String FILE_NAME = "simulacion.log";

    public synchronized static void writeLog(int id, String tipo, String estado) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.printf("[%s] ID: %d | Sim: %s | Estado: %s%n", timestamp, id, tipo, estado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}