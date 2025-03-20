@echo off
setlocal

:: Set console code page to UTF-8
chcp 65001 >nul

:: Set variables
set PROJECT_DIR=%cd%
set JAR_NAME=transaction-management-0.0.1.jar
set MAIN_CLASS=com.xjs.transactionmanagement.TransactionManagementApplication
set LOG_FILE=%PROJECT_DIR%\build.log

:: Clear the log file
echo. > "%LOG_FILE%"

:: Get Maven path from environment variable
set MAVEN_PATH=%MAVEN_HOME%\bin\mvn.cmd

:: Check if MAVEN_PATH exists, if not use hard-coded path
if not exist "%MAVEN_PATH%" (
    set MAVEN_PATH="C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.4.1\plugins\maven\lib\maven3\bin\mvn.cmd"
)

:: Package the project
echo Packaging the project... >> "%LOG_FILE%"

echo Before Maven command >> "%LOG_FILE%"
%MAVEN_PATH% clean package -DskipTests >> "%LOG_FILE%" 2>&1
echo After Maven command >> "%LOG_FILE%"


:: Check if packaging was successful
if ERRORLEVEL 1 (
    echo Packaging failed. Please check the error messages. >> "%LOG_FILE%"
    exit /b 1
)

:: Find the generated JAR file
for /f "delims=" %%i in ('dir /b /s "%PROJECT_DIR%\target\%JAR_NAME%"') do set JAR_PATH=%%i

:: Check if the JAR file exists
if not exist "%JAR_PATH%" (
    echo JAR file not found. Please ensure packaging was successful. >> "%LOG_FILE%"
    exit /b 1
)

endlocal