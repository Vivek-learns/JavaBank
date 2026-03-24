$ProgressPreference = "SilentlyContinue"
$ErrorActionPreference = "Stop"
$MavenUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
$MavenZip = "apache-maven.zip"
$MavenDir = "maven-local"
$MvnCmd = ".\$MavenDir\apache-maven-3.9.6\bin\mvn.cmd"

if (-Not (Test-Path "$MavenDir\apache-maven-3.9.6\bin\mvn.cmd")) {
    Write-Host "Downloading Maven from archive..."
    Invoke-WebRequest -Uri $MavenUrl -OutFile $MavenZip
    Write-Host "Extracting Maven..."
    Expand-Archive -Path $MavenZip -DestinationPath $MavenDir -Force
    Remove-Item $MavenZip
}

Write-Host "Building the project (mvn clean package)..."
cmd.exe /c "$MvnCmd clean package -DskipTests"

Write-Host "Starting JavaBank server..."
java -cp "target\classes;target\dependency\*" bank.Main
