package db;

import data.Logmanager;
import java.sql.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConexionTask implements Runnable {
    private int id;
    private String type, url, user, pass, query;
    private int maxRetries;
    private Poolmanager pool;
    private CountDownLatch latch;
    private AtomicInteger exitos, fallos, totalReintentos;

    public ConexionTask(int id, String type, String url, String user, String pass, String query, 
                        int retries, Poolmanager pool, CountDownLatch latch, 
                        AtomicInteger ex, AtomicInteger fa, AtomicInteger re) {
        this.id = id; this.type = type; this.url = url; this.user = user; 
        this.pass = pass; this.query = query; this.maxRetries = retries; 
        this.pool = pool; this.latch = latch;
        this.exitos = ex; this.fallos = fa; this.totalReintentos = re;
    }

    @Override
    public void run() {
        try { 
            latch.await(); // Espera la señal de inicio [cite: 16]
        } catch (InterruptedException e) { 
            return; 
        }

        int intentosRealizados = 0;
        boolean lograda = false;

        while (intentosRealizados <= maxRetries && !lograda) {
            Connection conn = null;
            try {
                // Selecciona el tipo de simulación: Raw o Pooled [cite: 9, 10]
                conn = (pool != null) ? pool.getConnection() : DriverManager.getConnection(url, user, pass);
                
                Statement st = conn.createStatement();
                st.executeQuery(query); // Ejecuta la query de muestra [cite: 21]
                
                lograda = true;
                exitos.incrementAndGet();
                Logmanager.writeLog(id, type, "EXITOSA"); // Registro en Log [cite: 7]
            } catch (Exception e) {
                intentosRealizados++;
                totalReintentos.incrementAndGet();
                if (intentosRealizados > maxRetries) {
                    fallos.incrementAndGet();
                    Logmanager.writeLog(id, type, "FALLIDA"); // Registro en Log [cite: 7]
                }
            } finally {
                // Reciclaje de conexiones para el modo Pooled [cite: 10]
                if (pool != null && conn != null) {
                    pool.releaseConnection(conn);
                } else if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }
        }
    }
}