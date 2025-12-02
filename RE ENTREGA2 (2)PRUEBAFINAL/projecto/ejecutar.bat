@echo off
echo ===============================================
echo    SISTEMA DE GESTION DE PELICULAS Y RESENIAS
echo ===============================================
echo.
echo Iniciando aplicacion...
echo.

REM Intentar con java en PATH primero
java -version >nul 2>&1
if %errorlevel%==0 (
    echo Ejecutando con Java del sistema...
    java -jar sistema-peliculas.jar
) else (
    echo Java no encontrado en PATH, usando instalacion local...
    "C:\Program Files\Java\jdk-25\bin\java" -jar sistema-peliculas.jar
)

echo.
echo Aplicacion finalizada.
pause