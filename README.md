# Project Run Instructions

## Environment Requirements

1. **Local Environment**:
   - Install JDK 21
   - Install Maven
   - Install Docker (if using Docker)

## Build the Project Using Maven (Without Docker)

1. **Prepare Build Steps**:
   - Ensure JDK 21 and Maven are installed.
   - In the project root directory, execute the following command:

     ```bash
     build.bat
     ```
   - The build process log will be output to the `build.log` file in the same directory.

2. **Run the Project**:
   - In the project root directory, execute the following command to start the project:
   - The log will be output to the `run.log` file in the same directory.

     ```bash
     run.bat
     ```

3. **Access the Project**:
   - After starting, visit the following URL to view the transaction records:

     ```
     http://localhost:8080/transactions
     ```

---

## Build and Run the Project Using Docker

1. **Prepare Build Steps**:
   - Ensure Docker is installed.
   - In the project root directory, execute the following command:

     ```bash
     build_docker.bat
     ```
   - The build process log will be output to the `build_docker.log` file in the same directory.

2. **Run the Project**:
   - In the project root directory, execute the following command to start the project:
   - The log will be output to the `run_docker.log` file in the same directory.

     ```bash
     run_docker.bat
     ```

3. **Access the Project**:
   - After starting, visit the following URL to view the transaction records:

     ```
     http://localhost:8080/transactions
     ```

---

## Notes

- Ensure that the environment variables for Maven and Docker are correctly configured to use the `mvn` and `docker` commands directly in the command line.
- If you encounter any issues during the build or run process, please check the corresponding log files for more information.

# 项目运行说明

## 环境要求

1. **本地运行环境**：
    - 安装 JDK 21
    - 安装 Maven
    - 安装 Docker（如果使用 Docker 方式）

## 使用 Maven 构建项目（不使用 Docker）

1. **准备构建步骤**：
    - 确保已安装 JDK 21 和 Maven。
    - 在项目根目录下，执行以下命令：

      ```bash
      build.bat
      ```
    - 构建过程的日志会输出到同目录下的 `build.log` 文件中。

2. **运行项目**：
    - 在项目根目录下，执行以下命令启动项目：
    - 日志会输出到同目录下的 `run.log` 文件中。

      ```bash
      run.bat
      ```

3. **访问项目**：
    - 启动后，访问以下 URL 来查看交易记录：

      ```
      http://localhost:8080/transactions
      ```

---

## 使用 Docker 构建和运行项目

1. **准备构建步骤**：
    - 确保已安装 Docker。
    - 在项目根目录下，执行以下命令：

      ```bash
      build_docker.bat
      ```
    - 构建过程的日志会输出到同目录下的 `build_docker.log` 文件中。

2. **运行项目**：
    - 在项目根目录下，执行以下命令启动项目：
    - 日志会输出到同目录下的 `run_docker.log` 文件中。

      ```bash
      run_docker.bat
      ```

3. **访问项目**：
    - 启动后，访问以下 URL 来查看交易记录：

      ```
      http://localhost:8080/transactions
      ```

---

## 注意事项

- 确保 Maven 和 Docker 的环境变量已正确配置，以便在命令行中直接使用 `mvn` 和 `docker` 命令。
- 如果在构建或运行过程中遇到任何问题，请检查相应的日志文件以获取更多信息。