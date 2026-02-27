Para que tu repositorio destaque, necesitamos un README que no solo sea texto, sino que sea visual, esté bien organizado con iconos y facilite la lectura rápida a cualquiera (especialmente a un profesor o reclutador).

Aquí tienes una versión mejorada, con un diseño más moderno y estructurado:

---

# PoolConnection Simulation: RAW vs POOLED

Un simulador de carga de alta precisión diseñado para analizar y comparar el rendimiento de conexiones a bases de datos en entornos concurrentes. Este proyecto expone visualmente el impacto del **Connection Pooling** frente a las conexiones **Directas (RAW)**.

---

## Objetivo del Proyecto

El sistema somete a una base de datos PostgreSQL a un estrés de **3,000 hilos concurrentes** (configurables), midiendo:

1. **Latencia total** de ejecución.
2. **Tasa de éxito** bajo estrés.
3. **Eficiencia de recursos** del servidor.

---

## 📂 Arquitectura del Sistema

El proyecto sigue una estructura modular para separar la lógica de negocio de la conectividad:

* **`main`**: El motor del simulador (`simuladorEngine`) que orquesta los hilos.
* **`db`**: Gestión de conexiones y lógica del Pool personalizado.
* **`data`**: Utilidades para persistencia de logs y carga de configuraciones.

```bash
PROYECTO2/
├── bin/                 # Bytecode optimizado (.class)
├── src/
│   ├── data/            # ConfigLoader.java, LogManager.java
│   ├── db/              # ConexionTask.java, PoolManager.java
│   ├── main/            # Main.java (EntryPoint)
│   └── lib/             # postgresql-42.7.9.jar
├── config.properties    # ⚙️ Parámetros del sistema
└── simulacion.log       # 📝 Historial de eventos

```

---

## 🛠️ Tecnologías Clave

* **Multithreading Avanzado:** Uso de `ExecutorService` para la gestión de hilos y `CountDownLatch` para disparos sincronizados.
* **Atomicidad:** `AtomicInteger` para garantizar que los contadores sean precisos en entornos multihilo.
* **JDBC Nativo:** Comunicación directa con PostgreSQL sin ORMs para minimizar el overhead.

---

## 🚀 Guía de Inicio Rápido

### 1. Configuración Previa

Ajusta tus credenciales en el archivo `config.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/tu_db
db.user=postgres
db.pass=secret
sim.threads=3000
sim.pool_size=10

```

### 2. Compilación (PowerShell/CMD)

```powershell
# Crear carpeta de salida y compilar
mkdir bin -ErrorAction SilentlyContinue
javac -cp "src/main/postgresql-42.7.9.jar;src" -d bin src/data/*.java src/db/*.java src/main/*.java

```

### 3. Ejecución

```powershell
java -cp "bin;src/main/postgresql-42.7.9.jar" main.Main

```

---
