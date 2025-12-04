# 🎯 GUÍA DE DEMOSTRACIÓN - PRESENTACIÓN DEL PROYECTO

## 📋 Checklist Pre-Presentación

### Antes de Iniciar
- [ ] Verificar que Java JDK está instalado: `java -version`
- [ ] Compilar el proyecto: `compilar.bat`
- [ ] Cerrar todas las instancias anteriores de la aplicación
- [ ] Tener `peliculas_ejemplo.csv` visible en el explorador
- [ ] Abrir este documento como referencia

---

## 🎬 Script de Demostración (10-15 minutos)

### Paso 1: Introducción al Proyecto (1 min)
**Qué decir:**
> "Desarrollamos un sistema de gestión de películas con Java Swing que implementa el patrón MVC, manejo de excepciones personalizadas, programación concurrente y conexión a OMDb API."

**Mostrar:**
- Estructura de carpetas en VS Code
- README.md abierto

---

### Paso 2: Demostrar MVC (2 min)

**Qué decir:**
> "La arquitectura MVC está estrictamente separada en tres capas:"

**Mostrar en Código:**

1. **Vista** (`src/vista/LoginVista.java`):
```java
// Línea ~50: Solo getters, sin lógica
public String getEmail() {
    return emailField.getText();
}
```
> "Las vistas solo muestran componentes y capturan entrada. No acceden a la base de datos."

2. **Controlador** (`src/controlador/LoginControlador.java`):
```java
// Línea ~67: Coordinación
Usuario usuario = Logica.login(email, password);
```
> "Los controladores coordinan: reciben eventos de la vista, llaman al modelo, manejan excepciones."

3. **Modelo** (`src/app/Logica.java`):
```java
// Línea ~463: Lógica de negocio
public static Usuario login(String email, String password) throws Exception {
    // Validaciones y acceso a BD
}
```
> "El modelo contiene la lógica de negocio y accede a la BD mediante DAOs."

---

### Paso 3: Demostrar Excepciones Propias (3 min)

**Qué decir:**
> "Implementamos 3 excepciones personalizadas que extienden Exception para manejar errores específicos del dominio."

**Demostración en Vivo:**

1. **UsuarioInvalidoException** - Email Duplicado
   - Ejecutar: `ejecutar.bat` → [1]
   - Click en "Registrar"
   - Ingresar datos con email ya existente (ej: admin@test.com)
   - **Resultado esperado:** "El email ya está registrado"

2. **PeliculaNoEncontradaException** - Búsqueda OMDb
   - Hacer login
   - En buscador ingresar: "asdfqwerzxcv123456"
   - Click en "Buscar"
   - **Resultado esperado:** "Película No Encontrada"

3. **ReseniaInvalidaException** - Comentario Inválido
   - Calificar una película
   - Dejar el campo de reseña vacío
   - Click en OK
   - **Resultado esperado:** "Debe ingresar una reseña para calificar"

**Mostrar en Código:**
```java
// src/enumerativo/UsuarioInvalidoException.java
public class UsuarioInvalidoException extends Exception {
    private String campo;
    private String valorIngresado;
    // ... con toString() descriptivo
}
```

---

### Paso 4: Demostrar Concurrencia (4 min)

**Qué decir:**
> "Implementamos 5 técnicas de concurrencia para evitar que la interfaz se congele."

**Demostraciones:**

1. **SwingWorker - Pantalla de Carga**
   - Cerrar sesión y hacer login nuevamente
   - **Mostrar:** Pantalla "Cargando contenido..." con barra de progreso animada
   - > "La carga de películas se ejecuta en background con SwingWorker, la UI permanece responsive"

2. **SwingWorker - Búsqueda OMDb**
   - Buscar "Inception"
   - **Mostrar:** Diálogo "Buscando en OMDb..."
   - > "La búsqueda en la API se hace en segundo plano"

3. **Timer/TimerTask - Auto-guardado**
   - Calificar una película
   - Empezar a escribir una reseña
   - > "Mientras escribe, un Timer guarda automáticamente el borrador cada 30 segundos"
   
   **Mostrar código:**
   ```java
   // src/utilidades/AutoGuardadoResenias.java línea ~40
   timer.scheduleAtFixedRate(new TimerTask() {
       public void run() {
           guardarBorradores();
       }
   }, 0, 30000);
   ```

4. **ExecutorService - Thread Pool**
   ```bash
   ejecutar.bat → [5] (Demostración de Concurrencia)
   ```
   - Ver output de búsquedas paralelas
   - > "Usamos un pool de 3 threads para búsquedas concurrentes"

5. **ImportadorCSV - SwingWorker con Progreso**
   ```bash
   ejecutar.bat → [4]
   ```
   - Seleccionar `peliculas_ejemplo.csv`
   - **Mostrar:** Barra de progreso actualizándose en tiempo real
   - > "Importa 15 películas sin bloquear la interfaz, reportando progreso"

---

### Paso 5: Integración OMDb (2 min)

**Demostración:**
1. Buscar "The Matrix"
2. **Mostrar resultado con todos los campos:**
   - Título: The Matrix
   - Año: 1999
   - Director: Wachowski Sisters
   - Género: Action, Sci-Fi
   - Rating IMDb: 8.7/10
   - Duración: 136 min
   - Sinopsis completa

**Mostrar código:**
```java
// src/db/ConsultaPeliculasOMDb.java
JSONObject resultado = buscarPelicula("The Matrix");
```

---

### Paso 6: Importación CSV (2 min)

**Demostración:**
1. Abrir `peliculas_ejemplo.csv` en Notepad
2. Mostrar formato:
   ```
   Titulo,Director,Genero,Año,Rating,Duracion,Sinopsis
   "The Matrix","Wachowski","Action|Sci-Fi",1999,8.7,136,"..."
   ```

3. Ejecutar importador:
   ```bash
   ejecutar.bat → [4]
   ```

4. **Mostrar:**
   - Barra de progreso: "Procesando película 5 de 15..."
   - Resultado: "✅ 15 importadas, ⚠️ 0 errores"

**Explicar ordenamiento:**
> "Las películas se pueden ordenar por título, duración o género usando Comparators personalizados"

---

### Paso 7: Base de Datos (1 min)

**Mostrar en código:**
```java
// src/daoJDBC/PeliculaDAOjdbc.java línea ~19
String sql = "INSERT INTO PELICULA (GENERO, TITULO, RESUMEN, DIRECTOR, 
              DURACION, RATING_PROMEDIO, ANIO, POSTER) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

pstmt.setFloat(6, pelicula.getRatingPromedio());  // ✅ Rating persiste
pstmt.setInt(7, pelicula.getAnio());              // ✅ Año persiste
pstmt.setString(8, pelicula.getPoster());         // ✅ Poster persiste
```

**Qué decir:**
> "Todos los campos nuevos (año, rating IMDb, poster URL) se guardan correctamente en la base de datos SQLite."

---

## 🎯 Puntos Clave a Enfatizar

### Durante la Demo

1. **MVC Estricto:**
   - "Las vistas NO acceden a la BD"
   - "Los controladores coordinan, no procesan"
   - "El modelo contiene toda la lógica de negocio"

2. **Excepciones Descriptivas:**
   - "Cada excepción tiene campos específicos (campo, valor, criterio)"
   - "Mensajes claros al usuario, no genéricos"
   - "Todas son checked exceptions para forzar manejo"

3. **UI Never Freezes:**
   - "Todas las operaciones pesadas en background"
   - "SwingWorker para operaciones largas"
   - "Timer para tareas periódicas"
   - "Thread pools para paralelización"

4. **Integración Completa:**
   - "OMDb API funciona estable"
   - "Importación CSV sin bloqueos"
   - "Persistencia completa en SQLite"

---

## ❓ Preguntas Frecuentes y Respuestas

### "¿Por qué no usar un framework MVC como Spring?"
> "El objetivo del curso es entender la arquitectura MVC desde cero. Implementamos la separación manualmente para comprender cada capa."

### "¿Cómo manejan la concurrencia en la base de datos?"
> "Usamos ConcurrentHashMap para el auto-guardado en memoria. Las escrituras a BD son secuenciales a través de JDBC con transacciones."

### "¿Qué pasa si hay error de red con OMDb?"
> "Capturamos la excepción y mostramos un mensaje claro al usuario. Implementamos timeouts de 10 segundos."

### "¿Por qué 3 excepciones y no más?"
> "El requisito era mínimo 3. Cubren los casos críticos: usuario inválido, película no encontrada, reseña inválida."

### "¿La GUI se puede congelar?"
> "No. Todas las operaciones que toman más de 100ms se ejecutan en background con SwingWorker o Thread."

---

## 🐛 Troubleshooting Durante la Demo

### Si la aplicación no compila:
```bash
# Verificar Java
java -version

# Limpiar y recompilar
rmdir /s /q bin
compilar.bat
```

### Si la BD está corrupta:
```bash
# Eliminar y recrear
del plataforma_streaming.db
# Ejecutar app, se crea automáticamente
```

### Si OMDb no responde:
> "En caso de error de red, el sistema captura la excepción y muestra mensaje. Podemos ver el código de manejo de errores en PeliculasControlador.java línea 270."

---

## 📊 Métricas del Proyecto

**Para mencionar:**
- **Líneas de código:** ~3,500
- **Clases:** 45+
- **Paquetes:** 8 (vista, controlador, model, dao, daoJDBC, enumerativo, utilidades, gestion)
- **Excepciones propias:** 3
- **Implementaciones de concurrencia:** 5
- **Tiempo de desarrollo:** [Ajustar según corresponda]

---

## ✅ Checklist Post-Demostración

- [ ] Mostré el flujo completo de usuario
- [ ] Demostré MVC con código
- [ ] Provoqué las 3 excepciones
- [ ] Mostré pantallas de carga (no congelamiento)
- [ ] Importé CSV con barra de progreso
- [ ] Busqué en OMDb exitosamente
- [ ] Expliqué la persistencia en BD
- [ ] Respondí preguntas con seguridad

---

## 🎓 Cierre de la Presentación

**Qué decir:**
> "En resumen, implementamos un sistema completo que cumple con todos los requisitos en nivel Excelente:
> - MVC correctamente aplicado con separación estricta
> - 3 excepciones personalizadas bien utilizadas
> - 5 implementaciones de concurrencia sin bloqueos
> - Integración estable con OMDb
> - Importación CSV con progreso visual
> - Persistencia completa en SQLite
> 
> El código está bien organizado, documentado y listo para producción."

**Agradecer y abrir a preguntas.**

---

## 📝 Notas Finales

- **Practica la demo al menos 2 veces** antes de presentar
- **Ten el código abierto** en puntos clave (LoginControlador, ImportadorCSV, excepciones)
- **Sé honesto** si algo no funciona: explica el concepto
- **Muestra confianza** en tu código: lo conoces mejor que nadie
- **README y RESUMEN_ENTREGA** son tus mejores amigos para recordar detalles

---

**¡Éxito en la presentación! 🚀**
