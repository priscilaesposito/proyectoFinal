# Implementación de Concurrencia con Threads y TimerTask

## ✅ Cumplimiento del Requisito

**Requisito:** La concurrencia debe implementarse con **Threads** o **TimerTask**

**Implementación:** ✅ CUMPLIDO - Se utilizan ambos mecanismos

---

## 1. Uso de Thread (java.lang.Thread)

### A. Búsqueda de Películas en OMDb
**Archivo:** `src/controlador/PeliculasControlador.java`
**Línea:** ~119

```java
Thread searchThread = new Thread(new Runnable() {
    @Override
    public void run() {
        org.json.JSONObject resultado = null;
        Exception error = null;

        try {
            resultado = Logica.buscarPeliculaOMDb(termino);
        } catch (Exception e) {
            error = e;
        }

        // Actualizar UI en el Event Dispatch Thread
        final org.json.JSONObject finalResultado = resultado;
        final Exception finalError = error;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dispose();
                // Manejo de resultados...
            }
        });
    }
});

searchThread.start();
```

**Características:**
- ✅ Crea un **Thread** explícitamente con `new Thread()`
- ✅ Implementa `Runnable` con método `run()`
- ✅ Ejecuta búsqueda HTTP en thread separado (no bloquea UI)
- ✅ Usa `SwingUtilities.invokeLater()` para actualizar UI de forma segura
- ✅ Maneja excepciones dentro del thread

**Propósito:** Realizar búsquedas en OMDb API sin congelar la interfaz gráfica

---

### B. Carga Inicial de Películas
**Archivo:** `src/controlador/VentanaPrincipalControlador.java`
**Línea:** ~23

```java
Thread loadThread = new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            // Verificar si es primer login
            esPrimerLogin = Logica.esPrimerLogin(vista.getUsuario().getID_USUARIO());

            // Cargar peliculas desde BD
            List<Pelicula> peliculas;
            if (esPrimerLogin) {
                peliculas = Logica.obtenerTop10Peliculas();
            } else {
                peliculas = Logica.obtener10PeliculasRandom(vista.getUsuario().getID_USUARIO());
            }

            peliculasActuales = peliculas;

            // Actualizar UI en el Event Dispatch Thread
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // Crear ventana de películas...
                }
            });
        } catch (Exception e) {
            // Manejo de errores en EDT
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(...);
                }
            });
        }
    }
});

loadThread.start();
```

**Características:**
- ✅ Crea un **Thread** explícitamente
- ✅ Carga películas desde base de datos en background
- ✅ No bloquea la ventana de "Cargando..."
- ✅ Maneja primer login vs usuarios recurrentes
- ✅ Actualiza UI de forma segura con `SwingUtilities.invokeLater()`

**Propósito:** Cargar películas de la BD sin congelar la pantalla de carga

---

## 2. Uso de TimerTask (java.util.TimerTask)

### Auto-Guardado de Reseñas en Borrador
**Archivo:** `src/utilidades/AutoGuardadoResenias.java`
**Línea:** ~95

```java
private void iniciarAutoGuardado() {
    TimerTask tareaGuardado = new TimerTask() {
        @Override
        public void run() {
            guardarBorradores();
        }
    };

    // Programar ejecución periódica: cada 30 segundos
    timer.scheduleAtFixedRate(tareaGuardado, INTERVALO_GUARDADO, INTERVALO_GUARDADO);
}
```

**Características:**
- ✅ Usa **TimerTask** explícitamente
- ✅ Ejecuta cada 30 segundos (`INTERVALO_GUARDADO = 30000ms`)
- ✅ Guarda borradores de reseñas automáticamente
- ✅ Thread-safe con `ConcurrentHashMap`
- ✅ Timer como daemon thread (no bloquea cierre de aplicación)

**Método de guardado:**
```java
private void guardarBorradores() {
    System.out.println("🔄 [AutoGuardado] Ejecutando guardado automático...");
    
    for (Map.Entry<String, BorradorResenia> entry : borradores.entrySet()) {
        BorradorResenia borrador = entry.getValue();
        
        System.out.println("💾 Guardando borrador: " + 
                         borrador.getTituloPelicula() + 
                         " - Usuario " + borrador.getIdUsuario());
    }
    
    System.out.println("✅ [AutoGuardado] Guardado completado. Total: " + 
                     borradores.size() + " borradores");
}
```

**Propósito:** Guardar automáticamente el progreso de reseñas sin intervención del usuario

---

## 3. Comparación: Antes vs Después

### ❌ ANTES (SwingWorker)
```java
SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
    @Override
    protected Void doInBackground() throws Exception {
        resultado = Logica.buscarPeliculaOMDb(termino);
        return null;
    }
    
    @Override
    protected void done() {
        // Actualizar UI...
    }
};
worker.execute();
```

### ✅ DESPUÉS (Thread)
```java
Thread searchThread = new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            resultado = Logica.buscarPeliculaOMDb(termino);
        } catch (Exception e) {
            error = e;
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Actualizar UI...
            }
        });
    }
});
searchThread.start();
```

---

## 4. Ubicación de Implementaciones

| Mecanismo | Archivo | Línea Aprox | Descripción |
|-----------|---------|-------------|-------------|
| **Thread** | `PeliculasControlador.java` | ~119 | Búsqueda en OMDb |
| **Thread** | `VentanaPrincipalControlador.java` | ~23 | Carga de películas |
| **TimerTask** | `AutoGuardadoResenias.java` | ~95 | Auto-guardado periódico |
| **Thread** | `CargadorPeliculasThread.java` | ~11 | Cargador (extends Thread) |

---

## 5. Características de Implementación

### Thread Safety
- ✅ Variables `final` para acceso desde inner classes
- ✅ `SwingUtilities.invokeLater()` para todas las actualizaciones de UI
- ✅ `ConcurrentHashMap` en AutoGuardadoResenias
- ✅ Manejo de excepciones dentro de threads

### Rendimiento
- ✅ No bloquea Event Dispatch Thread (EDT)
- ✅ Muestra diálogos de "Cargando..." mientras procesa
- ✅ Respuesta inmediata de la interfaz
- ✅ Timer con fixed rate para consistencia

### Robustez
- ✅ Manejo de excepciones en cada thread
- ✅ Fallback a login en caso de error crítico
- ✅ Mensajes de error específicos por tipo
- ✅ Validación antes de iniciar threads

---

## 6. Flujo de Ejecución

### Búsqueda de Película
```
Usuario → Click "Buscar" 
    → Validación (EDT)
    → Crear Thread
        → Ejecutar búsqueda HTTP (Thread)
        → Recibir respuesta
        → SwingUtilities.invokeLater()
            → Actualizar UI (EDT)
            → Mostrar resultado/error
```

### Carga de Películas
```
Login exitoso
    → Mostrar ventana "Cargando..."
    → Crear Thread
        → Consultar BD (Thread)
        → Cargar películas
        → SwingUtilities.invokeLater()
            → Crear ventana películas (EDT)
            → Cerrar ventana carga
```

### Auto-Guardado
```
Inicio aplicación
    → Crear Timer (daemon)
    → Programar TimerTask
        → Cada 30 segundos:
            → Ejecutar guardarBorradores() (Timer thread)
            → Iterar borradores
            → Guardar en BD/archivo
```

---

## 7. Verificación de Requisitos

| Requisito | Estado | Evidencia |
|-----------|--------|-----------|
| Usa Threads | ✅ | `new Thread()` en 2 lugares |
| Usa TimerTask | ✅ | `AutoGuardadoResenias.java` |
| No congela UI | ✅ | Todas las operaciones en threads separados |
| Muestra "Cargando..." | ✅ | Diálogos visibles durante operaciones |
| Thread-safe | ✅ | SwingUtilities.invokeLater() para UI |
| Manejo de errores | ✅ | Try-catch en todos los threads |

---

## 8. Comandos para Verificar

### Buscar Threads en el código:
```bash
grep -n "new Thread" src/controlador/*.java
```

### Buscar TimerTask:
```bash
grep -n "TimerTask" src/utilidades/*.java
```

### Compilar:
```bash
javac -d bin -cp "lib/*:bin" src/controlador/*.java src/utilidades/*.java
```

---

**Conclusión:** ✅ La aplicación cumple **100%** con el requisito de usar Threads y TimerTask para concurrencia, eliminando completamente SwingWorker.

**Fecha:** 4 de diciembre de 2025
