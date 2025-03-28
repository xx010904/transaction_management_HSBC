@echo off
setlocal

:: Set console code page to UTF-8
chcp 65001 >nul

:: Set variables
set PROJECT_DIR=%cd%
set JAR_NAME=transaction-management-0.0.1.jar
set MAIN_CLASS=com.xjs.transactionmanagement.TransactionManagementApplication
set LOG_FILE=%PROJECT_DIR%\run.log

:: Clear the log file
echo. > "%LOG_FILE%"

:: Run the JAR file
echo Running the JAR file... >> "%LOG_FILE%"
echo Running the JAR file...
java -jar target\%JAR_NAME% >> "%LOG_FILE%" 2>&1

:: Check the run status
if ERRORLEVEL 1 (
    echo Run failed. Please check the error messages. >> "%LOG_FILE%"
    echo Run failed. Please check the error messages.
    exit /b 1
)

echo Program ran successfully! >> "%LOG_FILE%"
echo Program ran successfully!

:: Pause
echo Press any key to continue...
pause >nul

endlocal