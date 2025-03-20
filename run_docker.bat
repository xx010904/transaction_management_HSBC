@echo off
setlocal

:: 设置变量
set PROJECT_NAME=transaction-management
set JAR_FILE=target\%PROJECT_NAME%-0.0.1-SNAPSHOT.jar
set DOCKER_IMAGE_NAME=transaction-management:latest
set LOG_FILE=run_docker.log

:: 清空日志文件
echo Starting build process... > %LOG_FILE%

:: 构建 Docker 镜像
echo Building Docker image... >> %LOG_FILE%
docker build -t %DOCKER_IMAGE_NAME% . >> %LOG_FILE% 2>&1
if %ERRORLEVEL% neq 0 (
    echo Docker image build failed. >> %LOG_FILE%
    echo Check the log for more details. >> %LOG_FILE%
    exit /b 1
)

:: 检查 Docker 镜像是否存在
docker images | findstr %DOCKER_IMAGE_NAME%
if %ERRORLEVEL% neq 0 (
    echo Docker image not found: %DOCKER_IMAGE_NAME% >> %LOG_FILE%
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