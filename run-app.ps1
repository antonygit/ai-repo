# Spring AI Chat Application Setup Script

Write-Host "===========================================" -ForegroundColor Blue
Write-Host "Spring AI Chat Application Setup" -ForegroundColor Blue
Write-Host "===========================================" -ForegroundColor Blue
Write-Host

Write-Host "Please follow these steps:" -ForegroundColor Yellow
Write-Host

Write-Host "1. Get your OpenAI API key from: " -NoNewline
Write-Host "https://platform.openai.com/account/api-keys" -ForegroundColor Cyan
Write-Host

Write-Host "2. Set the environment variable by running:" -ForegroundColor Yellow
Write-Host '   $env:OPENAI_API_KEY="your_actual_api_key_here"' -ForegroundColor Green
Write-Host

if (-not $env:OPENAI_API_KEY -or $env:OPENAI_API_KEY -eq "") {
    Write-Host "ERROR: OPENAI_API_KEY environment variable is not set!" -ForegroundColor Red
    Write-Host "Please set it first using the command above and then run this script again." -ForegroundColor Red
    Write-Host
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "API Key is set. Checking for port conflicts..." -ForegroundColor Green

# Kill any process using port 8082
$portProcesses = netstat -ano | Select-String ":8082" | ForEach-Object {
    $line = $_.Line.Trim()
    $parts = $line -split '\s+'
    if ($parts.Length -ge 5) {
        $parts[4]
    }
}

foreach ($pid in $portProcesses) {
    if ($pid -and $pid -ne "0") {
        Write-Host "Killing process $pid using port 8082..." -ForegroundColor Yellow
        try {
            Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
        }
        catch {
            # Ignore errors if process is already stopped
        }
    }
}

Write-Host "Starting the application on port 8082..." -ForegroundColor Green
Write-Host

# Start the application
java -jar target\demo-backend-0.0.1-SNAPSHOT.jar
