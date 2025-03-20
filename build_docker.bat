@echo off
setlocal

:: 设置变量
set PROJECT_NAME=transaction-management
set JAR_FILE=target\%PROJECT_NAME%-0.0.1.jar
set DOCKER_IMAGE_NAME=transaction-management:latest
set LOG_FILE=build_docker.log

:: 清空日志文件
echo Starting build process... > %LOG_FILE%

:: Specify the path to Maven
set MAVEN_PATH="C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.4.1\plugins\maven\lib\maven3\bin\mvn.cmd"

:: 构建项目
echo Building the project... >> %LOG_FILE%
%MAVEN_PATH% clean package >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Maven build failed. >> %LOG_FILE%
)

:: 检查 JAR 文件是否存在
if not exist "%JAR_FILE%" (
    echo JAR file not found: %JAR_FILE% >> %LOG_FILE%
)

:: 创建 Dockerfile
echo Creating Dockerfile... >> %LOG_FILE%
(
    echo # 使用 OpenJDK 21 作为基础镜像
    echo FROM openjdk:21-jdk-slim
    echo.
    echo # 定义工作目录
    echo WORKDIR /app
    echo.
    echo # 将 Maven 构建的 jar 文件复制到容器中
    echo COPY target/%PROJECT_NAME%-0.0.1.jar app.jar
    echo.
    echo # 暴露应用运行的端口
    echo EXPOSE 8080
    echo.
    echo # 定义容器启动时的命令
    echo ENTRYPOINT ["java", "-jar", "app.jar"]
) > Dockerfile

echo Done. >> %LOG_FILE%
endlocal