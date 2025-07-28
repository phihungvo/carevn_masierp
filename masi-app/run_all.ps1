# Danh sách các thư mục cần xử lý
$directories = @("employee", "logistics", "production", "sale", "utility")

# Duyệt qua từng thư mục và chạy ./mvnw
foreach ($dir in $directories) {
    if (Test-Path $dir) {
        Write-Host "Đang xử lý thư mục: $dir"
        Set-Location $dir
        
        # Chạy ./mvnw và lưu ID tiến trình
        $process = Start-Process -FilePath "./mvnw" -PassThru
        

 
        
        Set-Location ..
    } else {
        Write-Host "Thư mục $dir không tồn tại"
    }
}
