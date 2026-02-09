@echo off
echo ==========================================
echo Spring AI Chat Application Setup
echo ==========================================
echo.

echo Please follow these steps:
echo.
echo 1. Get your OpenAI API key from: https://platform.openai.com/account/api-keys
echo.
echo 2. Set the environment variable by running this command in PowerShell:
echo    $env:OPENAI_API_KEY="your_actual_api_key_here"
echo.
echo 3. Then run this script again to start the application
echo.

if "%OPENAI_API_KEY%"=="" (
    echo ERROR: OPENAI_API_KEY environment variable is not set!
    echo Please set it first and then run this script again.
    echo.
    pause
    exit /b 1
)

echo API Key is set. Starting the application...
echo.
java -jar target\demo-backend-0.0.1-SNAPSHOT.jar

pause
