# Function to check if a command exists
function CommandExists {
    param (
        [string]$command
    )
    $null -ne (Get-Command $command -ErrorAction SilentlyContinue)
}

# Check if Chocolatey is installed
if (-not (CommandExists "choco")) {
    Write-Output "Chocolatey is not installed. Installing Chocolatey..."
    Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
}

# Check if Maven is installed
if (-not (CommandExists "mvn")) {
    Write-Output "Maven is not installed. Installing Maven..."
    choco install maven -y
}

# Check if Docker is installed
if (-not (CommandExists "docker")) {
    Write-Output "Docker is not installed. Installing Docker..."
    choco install docker-desktop -y
    Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    Start-Sleep -Seconds 10
}

# Check if Docker Compose is installed
if (-not (CommandExists "docker-compose")) {
    Write-Output "Docker Compose is not installed. Installing Docker Compose..."
    choco install docker-compose -y
}

# Build the Spring Boot application
Write-Output "Building the Spring Boot application..."
mvn clean package

# Build and start the Docker containers
Write-Output "Building and starting the Docker containers..."
docker-compose up --build