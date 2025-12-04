# Script para remover BOM de archivos Java

$sourceDir = ".\src"
$javaFiles = Get-ChildItem -Path $sourceDir -Filter "*.java" -Recurse

Write-Host "Removiendo BOM de archivos Java..."

$fileCount = 0

foreach ($file in $javaFiles) {
    $content = Get-Content -Path $file.FullName -Raw
    
    if ($content.Length -gt 0 -and [int][char]$content[0] -eq 0xFEFF) {
        $content = $content.Substring(1)
        
        # Crear encoding UTF-8 sin BOM
        $utf8NoBom = New-Object System.Text.UTF8Encoding $false
        [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        
        Write-Host "  Corregido: $($file.Name)"
        $fileCount++
    }
}

Write-Host ""
Write-Host "Archivos corregidos: $fileCount"
