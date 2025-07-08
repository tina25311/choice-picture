@echo off
echo ========================================
echo 图片喜好调查系统 - 快速启动
echo ========================================

echo.
echo 选择启动方式：
echo 1. 本地开发模式（需要手动启动MySQL和Redis）
echo 2. Docker模式（自动启动所有服务）
echo.
set /p choice="请输入选择 (1 或 2): "

if "%choice%"=="1" (
    echo.
    echo 启动本地开发模式...
    echo 请确保MySQL和Redis已启动
    echo.
    echo 启动后端服务...
    start "Backend" cmd /k "cd backend && mvn spring-boot:run"
    timeout /t 5 /nobreak >nul
    echo 启动前端服务...
    start "Frontend" cmd /k "cd frontend && npm run dev"
    echo.
    echo 服务启动完成！
    echo 前端地址：http://localhost:5173
    echo 后端地址：http://localhost:8080/api
) else if "%choice%"=="2" (
    echo.
    echo 启动Docker模式...
    echo 正在启动所有服务，请稍候...
    docker-compose up -d
    echo.
    echo Docker服务启动完成！
    echo 前端地址：http://localhost:3000
    echo 后端地址：http://localhost:8080/api
    echo.
    echo 查看服务状态：docker-compose ps
    echo 查看日志：docker-compose logs -f
) else (
    echo 无效选择，请重新运行脚本
)

echo.
pause 