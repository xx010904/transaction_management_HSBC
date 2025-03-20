@echo off
setlocal

:: Set variables
set PROJECT_NAME=transaction-management
set JAR_FILE=target\%PROJECT_NAME%-0.0.1.jar
set DOCKER_IMAGE_NAME=transaction-management:latest
set LOG_FILE=build_docker.log

:: Clear the log file
echo Starting build process... > %LOG_FILE%

:: Get Maven path from environment variable
set MAVEN_PATH=%MAVEN_HOME%\bin\mvn.cmd

:: Check if MAVEN_PATH exists, if not use hard-coded path
if not exist "%MAVEN_PATH%" (
    set MAVEN_PATH="C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.4.1\plugins\maven\lib\maven3\bin\mvn.cmd"
)

:: Package the project
echo Building the project... >> %LOG_FILE%
%MAVEN_PATH% clean package >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Maven build failed. >> %LOG_FILE%
)

:: Check if packaging was successful
if not exist "%JAR_FILE%" (
    echo JAR file not found: %JAR_FILE% >> %LOG_FILE%
)

:: Create Dockerfile
echo Creating Dockerfile... >> %LOG_FILE%
(
    echo # Use OpenJDK 21 as base image
    echo FROM openjdk:21-jdk-slim
    echo.
    echo # Define the working directory
    echo WORKDIR /app
    echo.
    echo # Copy the Maven built jar file into the container
    echo COPY target/%PROJECT_NAME%-0.0.1.jar app.jar
    echo.
    echo # Expose the port the app runs on
    echo EXPOSE 8080
    echo.
    echo # Define the command to run the container
    echo ENTRYPOINT ["java", "-jar", "app.jar"]
) > Dockerfile

echo Done. >> %LOG_FILE%
endlocal