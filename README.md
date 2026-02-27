PoolConnection Simulation 
Este proyecto es un simulador de carga y rendimiento para conexiones a bases de datos PostgreSQL. Permite comparar el comportamiento y la eficiencia entre conexiones directas (RAW) y el uso de un gestor de conexiones (POOLING) bajo entornos multi-hilo.

📁 Estructura del Proyecto
El código está organizado siguiendo las mejores prácticas de Java con la siguiente estructura de paquetes:

Plaintext

PROYECTO2/
├── bin/                    # Clases compiladas (.class)
├── src/
│   ├── data/
│   │   ├── ConfigLoader.java   # Carga de parámetros desde config.properties
│   │   └── LogManager.java     # Gestión de logs de la simulación
│   ├── db/
│   │   ├── ConexionTask.java   # Lógica de cada hilo de conexión (Runnable)
│   │   └── PoolManager.java     # Implementación del Pool de conexiones
│   ├── main/
│   │   └── Main.java           # Punto de entrada y orquestador
│   └── postgresql-42.7.9.jar   # Driver JDBC de PostgreSQL
├── config.properties           # Configuración de base de datos y hilos
└── simulacion.log              # Resultados de la ejecución
⚙️ Requisitos
Java JDK 8 o superior.

PostgreSQL (Local o remoto).

El driver JDBC postgresql-42.7.9.jar (incluido en el repo).

Instalación y Uso
1. Configuración
Edita el archivo config.properties en la raíz con tus credenciales:

Properties

db.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
db.user=tu_usuario
db.pass=tu_contraseña
sim.threads=3000
sim.retries=3


2. Compilación
Desde la carpeta raíz del proyecto (PROYECTO2), ejecuta:

javac -cp "src/main/postgresql-42.7.9.jar;src" -d bin src/data/*.java src/db/*.java src/main/*.java
3. Ejecución
Lanza la simulación apuntando a la clase principal:

PowerShell

java -cp "bin;src/main/postgresql-42.7.9.jar" main.Main


Funcionalidades:

Simulación RAW: Crea y cierra una conexión física por cada hilo.

Simulación POOLED: Utiliza una cola de conexiones reutilizables para optimizar recursos.

Manejo de Reintentos: Configurable para casos de fallo en la conexión.

Logs Detallados: Registro de éxitos, fallos y tiempos en simulacion.log.

Sincronización: Uso de CountDownLatch para asegurar que todos los hilos inicien simultáneamente.

Tecnologías Utilizadas
Java Core: Multithreading (ExecutorService, AtomicInteger).

JDBC: Conectividad nativa con PostgreSQL.
