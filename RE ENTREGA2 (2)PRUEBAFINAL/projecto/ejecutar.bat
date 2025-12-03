@echo off
echo ===============================================
echo    SISTEMA DE GESTION DE PELICULAS Y RESENIAS
echo ===============================================
echo.
echo Iniciando aplicacion...
echo.

REM Mostrar version de Java disponible
echo Verificando Java instalado...
java -version 2>&1
if %errorlevel%==0 (
    echo.
    echo Ejecutando aplicacion...
    echo.
    java -jar sistema-peliculas.jar
) else (
    echo.
    echo ERROR: Java no esta instalado o no esta en el PATH.
    echo.
    echo SOLUCION:
    echo 1. Descarga Java desde: https://adoptium.net/
    echo 2. Durante la instalacion, marca la opcion "Set JAVA_HOME variable"
    echo 3. Reinicia esta ventana de comandos y ejecuta de nuevo
    echo.
    echo Alternativamente, ejecuta manualmente:
    echo   "C:\Program Files\Java\jdk-XX\bin\java" -jar sistema-peliculas.jar
    echo   (reemplaza XX con tu version de Java, ej: jdk-23 o jdk-24)
)

echo.
echo Aplicacion finalizada.
pause