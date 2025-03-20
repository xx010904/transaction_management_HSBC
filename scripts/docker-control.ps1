# 定义服务名称
$serviceName = "transaction-service"

function Start-Service {
    Write-Host "Starting $serviceName..."
    docker-compose up -d
    Write-Host "$serviceName started."
}

function Stop-Service {
    Write-Host "Stopping $serviceName..."
    docker-compose down
    Write-Host "$serviceName stopped."
}

# 检查参数
if ($args[0] -eq "start") {
    Start-Service
} elseif ($args[0] -eq "stop") {
    Stop-Service
} else {
    Write-Host "Usage: .\docker-control.ps1 {start|stop}"
    exit 1
}