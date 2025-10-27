# Spring Air Quality Visualizer Build Script
# This script rebuilds the project and runs the application

param(
    [switch]$RunApp = $false
)

Write-Host "🔨 Building Spring Air Quality Visualizer..." -ForegroundColor Cyan

# Try to find Maven in common installation paths
$mvnPaths = @(
    "C:\Program Files\Apache\Maven\bin\mvn.cmd",
    "C:\Program Files (x86)\Apache\Maven\bin\mvn.cmd",
    "C:\Apache\Maven\bin\mvn.cmd",
    "$env:MAVEN_HOME\bin\mvn.cmd"
)

$mvnFound = $false
foreach ($path in $mvnPaths) {
    if (Test-Path $path) {
        Write-Host "✓ Found Maven at: $path" -ForegroundColor Green
        $mvnCmd = $path
        $mvnFound = $true
        break
    }
}

if (-not $mvnFound) {
    Write-Host "❌ Maven not found in standard paths" -ForegroundColor Red
    Write-Host ""
    Write-Host "To build the project, please:" -ForegroundColor Yellow
    Write-Host "1. Install Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "2. Add Maven to your PATH environment variable" -ForegroundColor Yellow
    Write-Host "3. Run: mvn clean package -DskipTests" -ForegroundColor Yellow
    exit 1
}

# Run Maven build
Write-Host ""
Write-Host "Running: mvn clean package -DskipTests" -ForegroundColor Cyan
Write-Host ""

& $mvnCmd clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "❌ Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "✅ Build successful!" -ForegroundColor Green

# Optional: Run the application
if ($RunApp) {
    Write-Host ""
    Write-Host "🚀 Starting application..." -ForegroundColor Cyan
    Write-Host ""
    
    $jar = Get-ChildItem "target" -Filter "spring-air-quality-visualizer-*.jar" | Select-Object -First 1
    if ($jar) {
        Write-Host "Starting: $($jar.Name)" -ForegroundColor Cyan
        java -jar $jar.FullName
    } else {
        Write-Host "❌ JAR file not found!" -ForegroundColor Red
        exit 1
    }
}
