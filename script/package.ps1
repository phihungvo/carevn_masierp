# this 


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
$ScriptPath = $MyInvocation.MyCommand.Path


$BasePath = (Split-Path $ScriptPath -Parent)
$JarsPath = Join-Path $BasePath "jars"
if (-not (Test-Path $JarsPath))
{
    Write-Host "Creating $JarsPath"
    New-Item -Path $JarsPath -ItemType Directory
}
$MasiSharedPath = Join-Path $BasePath "..\masi-shared"
$MasiApiPath = Join-Path $BasePath "..\masi-api"
$MasiEmployeePath = Join-Path $BasePath "..\masi-app\employee"
$MasiUtilityPath = Join-Path $BasePath "..\masi-app\utility"
$MasiProductionPath = Join-Path $BasePath "..\masi-app\production"
$MasiSalePath = Join-Path $BasePath "..\masi-app\sale"
$DockerScriptPath = Join-Path $BasePath "..\docker-compose"


function Build-Project {
    param(
        [string]$Path,
        [string]$SharedJarPath,
        [string]$JarsPath
    )
    Write-Host "Building project in $Path"
    Set-Location $Path
    Remove-Item -LiteralPath "target" -Recurse -Force -ErrorAction SilentlyContinue
    mvn install:install-file -Dfile="$SharedJarPath"
    mvn -DskipTests -Pdev package
    $JarPath = Get-ChildItem -Path "target" -Filter "*.jar" | Select-Object -ExpandProperty FullName
    Write-Host "Moving $JarPath to $JarsPath"
    Move-Item -Path $JarPath -Destination $JarsPath -Force
}

Set-Location $MasiSharedPath
mvn package
$SharedJarPath = Get-ChildItem -Path "target" -Filter "masi-shared-*.jar" | Select-Object -ExpandProperty FullName

# Paths to the project directories
$projectPaths = @( $MasiEmployeePath, $MasiProductionPath, $MasiSalePath, $MasiUtilityPath)

# Build the projects
foreach ($projectPath in $projectPaths) {
    Build-Project -Path $projectPath -SharedJarPath $SharedJarPath -JarsPath $JarsPath
}
