Se usa solo los datos presentados en la base de datos del pdf del entregable.
En el main decidimos hacer un menu.
Se agrego la clase Resenia que no estaba en el UML que entregamos en el entregable 1.

---

## Cómo ejecutar la aplicación

### Requisitos
- Java 17 o superior (compatible con Java 23, 24, etc.)
- Descargar desde: https://adoptium.net/

### Pasos para ejecutar

#### Opción 1: Windows (doble clic)
1. Abre el Explorador de archivos
2. Navega a la carpeta `projecto`
3. Haz doble clic en `ejecutar.bat`

#### Opción 2: Línea de comandos (Windows/Mac/Linux)
1. Abre una terminal (CMD, PowerShell o Terminal)
2. Navega a la carpeta del proyecto:
   ```
   cd "ruta/a/RE ENTREGA2 (2)PRUEBAFINAL/projecto"
   ```
3. Ejecuta:
   ```
   java -jar sistema-peliculas.jar
   ```

#### Opción 3: Si Java no está en el PATH (Windows)
Ejecuta directamente con la ruta completa a Java:
```
"C:\Program Files\Java\jdk-23\bin\java" -jar sistema-peliculas.jar
```
(Cambia `jdk-23` por la versión que tengas instalada)

### Modo consola
Para ejecutar en modo consola (sin interfaz gráfica):
```
java -jar sistema-peliculas.jar --console
```

### Solución de problemas
- **"Java no encontrado"**: Asegúrate de que Java esté instalado y la variable JAVA_HOME esté configurada
- **Error de base de datos**: Ejecuta desde la carpeta `projecto` donde está el archivo `plataforma_streaming.db`