# Script para remover tildes y reemplazar ni por ni en todos los archivos Java

$sourceDir = ".\src"

# Obtener todos los archivos Java
$javaFiles = Get-ChildItem -Path $sourceDir -Filter "*.java" -Recurse

Write-Host "Procesando $($javaFiles.Count) archivos Java..."

$fileCount = 0

# Crear encoding UTF-8 sin BOM
$utf8NoBom = New-Object System.Text.UTF8Encoding $false

foreach ($file in $javaFiles) {
    $content = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    $originalContent = $content
    
    # Aplicar reemplazos
    $content = $content -replace [char]0x00E1, 'a'
    $content = $content -replace [char]0x00E9, 'e'
    $content = $content -replace [char]0x00ED, 'i'
    $content = $content -replace [char]0x00F3, 'o'
    $content = $content -replace [char]0x00FA, 'u'
    $content = $content -replace [char]0x00C1, 'A'
    $content = $content -replace [char]0x00C9, 'E'
    $content = $content -replace [char]0x00CD, 'I'
    $content = $content -replace [char]0x00D3, 'O'
    $content = $content -replace [char]0x00DA, 'U'
    $content = $content -replace [char]0x00F1, 'ni'
    $content = $content -replace [char]0x00D1, 'Ni'
    $content = $content -replace [char]0x2013, '-'
    
    # Si hubo cambios, guardar el archivo
    if ($content -ne $originalContent) {
        [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        Write-Host "  Modificado: $($file.Name)"
        $fileCount++
    }
}

Write-Host ""
Write-Host "========================================"
Write-Host "RESUMEN:"
Write-Host "  Archivos procesados: $($javaFiles.Count)"
Write-Host "  Archivos modificados: $fileCount"
Write-Host "========================================"
