# 使用 OpenJDK 21 作为基础镜像
FROM openjdk:21-jdk-slim

# 定义工作目录
WORKDIR /app

# 将 Maven 构建的 jar 文件复制到容器中
COPY target/transaction-management-0.0.1.jar app.jar

# 暴露应用运行的端口
EXPOSE 8080

# 定义容器启动时的命令
ENTRYPOINT ["java", "-jar", "app.jar"]