@echo off
echo ========================================
echo 图片喜好调查系统测试脚本
echo ========================================

echo.
echo 1. 检查后端依赖...
cd backend
if not exist "target" (
    echo 编译后端项目...
    mvn compile
) else (
    echo 后端项目已编译
)

echo.
echo 2. 检查前端依赖...
cd ..\frontend
if not exist "node_modules" (
    echo 安装前端依赖...
    npm install
) else (
    echo 前端依赖已安装
)

echo.
echo 3. 检查图片目录...
if not exist "public\images" (
    echo 创建图片目录...
    mkdir public\images
    echo 请在 public\images 目录下添加8张图片（image1.jpg 到 image8.jpg）
)

echo.
echo 4. 系统准备就绪！
echo.
echo 启动说明：
echo - 后端：运行 start-backend.bat
echo - 前端：运行 start-frontend.bat
echo.
echo 访问地址：
echo - 前端：http://localhost:5173
echo - 后端API：http://localhost:8080/api
echo.
pause 