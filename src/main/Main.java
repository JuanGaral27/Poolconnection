package main;

import data.ConfigLoader;

// Cambiamos 'main' por 'Main' para evitar el conflicto con el nombre del método
public class Main { 
    /**
     * El método principal debe ser 'public static void main'.
     * Al cambiar el nombre de la clase a 'Main', Java ya no se confunde.
     */
    public static void main(String[] args) {
        // 1. Cargar configuración desde el archivo externo
        ConfigLoader config = new ConfigLoader();
        
        // Verificación de seguridad para asegurar que el archivo existe
        if (config.get("db.url") == null) {
            System.err.println("Error: No se pudo encontrar el archivo config.properties en la raíz del proyecto.");
            return;
        }

        // 2. Extraer datos del archivo (Desacoplamiento)
        String url = config.get("db.url");
        String user = config.get("db.user");
        String pass = config.get("db.pass");
        String query = config.get("db.query"); //
        int hilos = config.getInt("sim.muestras"); //
        int retries = config.getInt("sim.reintentos"); //
        int pSize = config.getInt("sim.pool_size");

        // 3. Instanciar el motor de simulación
        simuladorEngine engine = new simuladorEngine();

        System.out.println("--- Iniciando Simulador de Conexiones PostgreSQL ---");
        System.out.println("Hilos a ejecutar: " + hilos);

        // 4. Ejecución Simulación RAW
        System.out.println("\nIniciando simulación RAW (Conexión directa)...");
        engine.ejecutar("Raw", hilos, url, user, pass, query, retries, 0);

        // 5. Ejecución Simulación POOLED
        System.out.println("\nIniciando simulación POOLED (Conexión con Pool)...");
        engine.ejecutar("Pooled", hilos, url, user, pass, query, retries, pSize);

        System.out.println("\nProceso terminado. Por favor, analice los tiempos en consola y el archivo 'simulacion.log'."); //
    }
}