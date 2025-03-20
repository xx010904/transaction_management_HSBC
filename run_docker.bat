@echo off
setlocal

:: 设置变量
set PROJECT_NAME=transaction-management
set JAR_FILE=target\%PROJECT_NAME%-0.0.1-SNAPSHOT.jar
set DOCKER_IMAGE_NAME=transaction-management:latest
set LOG_FILE=build.log

:: 清空日志文件
echo Starting build process... > %LOG_FILE%

:: 检查是否已安装 Maven
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Maven could not be found. Please install Maven and try again. >> %LOG_FILE%
    exit /b 1
)

:: 构建项目
echo Building the project... >> %LOG_FILE%
mvn clean package >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Maven build failed. >> %LOG_FILE%
    exit /b 1
)

:: 检查 JAR 文件是否存在
if not exist "%JAR_FILE%" (
    echo JAR file not found: %JAR_FILE% >> %LOG_FILE%
    exit /b 1
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
    echo COPY target/%PROJECT_NAME%-0.0.1-SNAPSHOT.jar app.jar
    echo.
    echo # 暴露应用运行的端口
    echo EXPOSE 8080
    echo.
    echo # 定义容器启动时的命令
    echo ENTRYPOINT ["java", "-jar", "app.jar"]
) > Dockerfile

:: 构建 Docker 镜像
echo Building Docker image... >> %LOG_FILE%
docker build -t %DOCKER_IMAGE_NAME% . >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Docker image build failed. >> %LOG_FILE%
    exit /b 1
)

:: 运行 Docker 容器
echo Running Docker container... >> %LOG_FILE%
docker run -p 8080:8080 %DOCKER_IMAGE_NAME% >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Failed to run Docker container. >> %LOG_FILE%
    exit /b 1
)

echo Done. >> %LOG_FILE%
endlocal