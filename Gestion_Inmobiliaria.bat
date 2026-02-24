Para que tu mamá sepa qué está pasando mientras espera, podemos modificar el .bat para que muestre una cuenta regresiva real en la pantalla.

Aquí tienes el código actualizado. He cambiado el timeout por un bucle que imprime los números uno por uno:

Fragmento de código
@echo off
set "BASEDIR=%~dp0"
set "JAVA_PATH=%BASEDIR%java21\bin\javaw.exe"

:: 1. Intentamos cerrar instancias viejas para evitar conflictos
taskkill /f /im javaw.exe >nul 2>&1

:: 2. Iniciamos el sistema en segundo plano
start "" "%JAVA_PATH%" -jar "%BASEDIR%InmoDoc.jar"

echo.
echo ========================================
echo   Iniciando Sistema InmoDoc...
echo ========================================
echo.

:: 3. Cuenta regresiva manual
set /a count=8
:loop
if %count%==0 goto open
echo El sistema se abre en %count%...
timeout /t 1 /nobreak > nul
set /a count-=1
goto loop

:open
echo.
echo ¡Listo! Abriendo el navegador...
start http://localhost:8080/dashboard
exit