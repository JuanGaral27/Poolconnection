package main;

import db.ConexionTask;
import db.Poolmanager;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class simuladorEngine {

    /**
     * Este método se encarga de orquestar la simulación de conexiones.
     * Se ha añadido el registro explícito del driver de PostgreSQL para solucionar
     * el error "No suitable driver found".
     */
    public void ejecutar(String tipo, int hilos, String url, String user, String pass, String query, int retries, int pSize) {
        
        // 1. REGISTRO EXPLÍCITO DEL DRIVER (Modificación crítica)
        try {
            // Esto obliga a Java a buscar la clase en el archivo .jar de la raíz
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("--- ERROR CRÍTICO ---");
            System.err.println("No se encontró el driver de PostgreSQL.");
            System.err.println("Asegúrate de que 'postgresql-42.7.10.jar' esté en 'Referenced Libraries' en VS Code.");
            return; // Detener ejecución si no hay driver
        }

        ExecutorService executor = Executors.newFixedThreadPool(hilos);
        
        // CountDownLatch para que todos los hilos inicien a la vez
        CountDownLatch latch = new CountDownLatch(1);
        
        AtomicInteger exitos = new AtomicInteger(0);
        AtomicInteger fallos = new AtomicInteger(0);
        AtomicInteger totalReintentos = new AtomicInteger(0);

        Poolmanager pool = null;
        if (tipo.equalsIgnoreCase("Pooled")) {
            try {
                // Ahora el pool podrá encontrar el driver registrado anteriormente
                pool = new Poolmanager(url, user, pass, pSize);
            } catch (Exception e) {
                System.err.println("Error inicializando el pool: " + e.getMessage());
            }
        }

        // Registrar tiempo de inicio para la métrica
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= hilos; i++) {
            // Cada muestra tiene un ID único
            executor.execute(new ConexionTask(i, tipo, url, user, pass, query, retries, pool, latch, exitos, fallos, totalReintentos));
        }

        latch.countDown(); // Arrancan todos los hilos simultáneamente
        executor.shutdown();
        
        try {
            // Freno manual/automático: si no termina en 5 min, se detiene
            if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        long tiempoTotal = System.currentTimeMillis() - startTime;

        // Impresión de métricas requeridas
        System.out.println("--- Resultados " + tipo + " ---");
        System.out.println("Tiempo total: " + tiempoTotal + "ms");
        System.out.println("Exitosas: " + exitos.get() + " | Fallidas: " + fallos.get());
        System.out.println("Promedio reintentos: " + (hilos > 0 ? (double)totalReintentos.get()/hilos : 0));
    }
}