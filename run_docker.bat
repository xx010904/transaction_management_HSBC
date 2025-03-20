@echo off
setlocal

:: Set variables
set PROJECT_NAME=transaction-management
set JAR_FILE=target\%PROJECT_NAME%-0.0.1.jar
set IMAGE_NAME=transaction-management
set IMAGE_NAME_VERSION=%IMAGE_NAME%:latest
set LOG_FILE=run_docker.log

:: Clear the log file
echo Starting build process... > %LOG_FILE%
echo Starting build process...

:: Build Docker image
:build_image
echo Building Docker image... >> %LOG_FILE%
echo Building Docker image...
docker build -t %IMAGE_NAME_VERSION% . >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Docker image build failed. >> %LOG_FILE%
    echo Docker image build failed.
    echo Check the log for more details. >> %LOG_FILE%
    exit /b 1
)

:: Check if JAR file exists
if not exist %JAR_FILE% (
    echo JAR file not found: %JAR_FILE% >> %LOG_FILE%
    echo JAR file not found: %JAR_FILE%
    exit /b 1
)

:check_image
:: Check if Docker image exists
docker images | findstr %IMAGE_NAME%
if %ERRORLEVEL% neq 0 (
    echo Docker image not found: %IMAGE_NAME_VERSION%. >> %LOG_FILE%
    echo Docker image not found: %IMAGE_NAME_VERSION%.
    echo Waiting for the image to be built... >> %LOG_FILE%
    echo Waiting for the image to be built...
    timeout /t 5 >nul
    goto build_image
)

:: Run Docker container
echo Running Docker container... >> %LOG_FILE%
echo Running Docker container...
docker run -p 8080:8080 %IMAGE_NAME_VERSION% >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Failed to run Docker container. >> %LOG_FILE%
    echo Failed to run Docker container.
    exit /b 1
)

echo Done. >> %LOG_FILE%
echo Done.
endlocal