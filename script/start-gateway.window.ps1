if ($null -eq $env)
{
    $env = 'dev'
}

$IsAllServices = $false

if ($args -contains "all")
{
    $IsAllServices = $true
}



Write-Host "Environment: $env"

function Get-Command-Not-Exist
{
    param(
        [string]$CommandName
    )
    $command = Get-Command $CommandName -ErrorAction SilentlyContinue
    return ($null -eq $command)
}

$principal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())

if (Get-Command-Not-Exist "docker")
{
    Write-Host "Please install Docker Desktop for Windows from https://docs.docker.com/docker-for-windows/install/"
    exit
}
if (Get-Command-Not-Exist "choco")
{
    Write-Host "chocolatey is not installed. Installing chocolatey..."
    $principal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
    if (-not ($principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)))
    {
        Start-Process -FilePath "powershell" -ArgumentList "$( '-File ""' )$( Get-Location )$( '\' )$( $MyInvocation.MyCommand.Name )$( '""' )" -Verb runAs
        exit
    }
    Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
}

if (Get-Command-Not-Exist "node")
{

    Write-Host "Node.js is not installed. Installing Node.js..."
    choco install nodejs-lts -y
    npm install -g npm
}

if (Get-Command-Not-Exist "java")
{
    Write-Host "Java is not installed. Installing Java..."
    choco install openjdk --version=17.0.2 -y
}

if (Get-Command-Not-Exist "mvn")
{
    Write-Host "Maven is not installed. Installing Maven..."
    choco install maven -y
}

Write-Host "All dependencies are installed. Running the project..."
$ScriptPath = $MyInvocation.MyCommand.Path
$BasePath = (Split-Path $ScriptPath -Parent)
$MasiSharedPath = Join-Path $BasePath "..\masi-shared"
$MasiApiPath = Join-Path $BasePath "..\masi-api"
$MasiEmployeePath = Join-Path $BasePath "..\masi-app\employee"
$MasiProductionPath = Join-Path $BasePath "..\masi-app\production"
$MasiSalePath = Join-Path $BasePath "..\masi-app\sale"
$DockerScriptPath = Join-Path $BasePath "..\docker-compose"

function Build-Project
{
    param(
        [string]$Path
    )
    Set-Location $Path
    Remove-Item -LiteralPath "target" -Recurse -Force -ErrorAction SilentlyContinue
    npm i
    mvn install:install-file -Dfile="$SharedJarPath"
    mvn install -DskipTests
}

Set-Location $MasiSharedPath
mvn package
$SharedJarPath = Get-ChildItem -Path "target" -Filter "masi-shared-*.jar" | Select-Object -ExpandProperty FullName

Set-Location $MasiApiPath
Remove-Item -LiteralPath "target" -Recurse -Force -ErrorAction SilentlyContinue
npm i
mvn install:install-file -Dfile="$SharedJarPath"
mvn package -Pprod jib:dockerBuild -DskipTests

Build-Project $MasiEmployeePath
Build-Project $MasiProductionPath
Build-Project $MasiSalePath
Build-Project $MasiUtilityPath
$SelectedService = @("masierp-postgresql"  "alertmanager"  "consul-config-loader"  "masierp"  "prometheus"  "consul"  "grafana" "masi-zookeeper" "kafka")
$ListService = @("masiemployee"  "masiproduction"  "masisale" "masiutility")
$ServicePath = @($MasiEmployeePath  $MasiProductionPath  $MasiSalePath $MasiUtilityPath)
if ($IsAllServices)
{
    $SelectedService = $SelectedService + " " + ($ListService -join " ")
}
else
{
    foreach ($service in $ListService)
    {
        Write-Host "Do you want to start $service service? (y/n)"
        $response = Read-Host
        if ($response -like "y*")
        {
            Set-Location $ServicePath[$ListService.IndexOf($service)]
            mvn -ntp verify -DskipTests -Pprod jib:dockerBuild
            $SelectedService += $service
        }
    }
}
Set-Location $DockerScriptPath
docker compose -f "docker-compose.yml" up -d --build $SelectedService
"masierp-postgresql"  "alertmanager"  "consul-config-loader"  "masierp"  "prometheus"  "consul"  "grafana" "masi-zookeeper" "kafka"