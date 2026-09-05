@echo off
set "BASEDIR=%~dp0"

:: IMPORTANTE: te recomiendo mover la carpeta java21 fuera de la carpeta
:: sincronizada con Google Drive (por ejemplo a C:\InmoDoc\java21) y
:: actualizar esta ruta. Un runtime de Java son miles de archivos chicos,
:: y tenerlos dentro de una carpeta que Drive vigila/sincroniza es
:: probablemente la causa principal de que el sistema tarde tanto en abrir.
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

:: 3. Esperamos de verdad a que el sistema responda en el puerto 8080,
::    en vez de contar un tiempo fijo. Reintenta 1 vez por segundo,
::    hasta un maximo de 90 segundos.
set /a intentos=0
set /a maximo=90

:esperar
set /a intentos+=1

powershell -NoProfile -Command "try { $c = New-Object System.Net.Sockets.TcpClient; $c.Connect('localhost',8080); $c.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if %errorlevel%==0 goto listo

if %intentos% GEQ %maximo% goto demorado

echo Esperando a que el sistema termine de iniciar... (%intentos%/%maximo%)
timeout /t 1 /nobreak > nul
goto esperar

:listo
echo.
echo ¡Listo! Abriendo el navegador...
:: Pequeño margen extra por si el puerto ya respondio pero el sistema
:: todavia esta terminando de acomodar la primera pagina.
timeout /t 1 /nobreak > nul
start http://localhost:8080/dashboard
exit

:demorado
echo.
echo El sistema esta tardando mas de lo normal en iniciar (mas de %maximo% segundos).
echo Puede que Google Drive este haciendo mas lento el arranque - consulta con tu tecnico.
echo Abriendo el navegador de todas formas, puede que necesites refrescar la pagina.
start http://localhost:8080/dashboard
exit