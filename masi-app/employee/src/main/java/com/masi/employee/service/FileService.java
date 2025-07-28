package com.masi.employee.service;

import com.masi.employee.domain.Documentary;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {

    private final String UPLOAD_DIR = "uploaded-files/documentaries";
    private static final Logger log = LoggerFactory.getLogger(FileService.class);


    public boolean checkFileExists(String nameFile) {
        Path path = Paths.get(UPLOAD_DIR, nameFile);
        return Files.exists(path);
    }
    public Path getFilePath(String nameFile) {
        return Paths.get(UPLOAD_DIR, nameFile);
    }

    private void createDirectoryIfNotExists() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            log.info("Directory created or already exists: {}", UPLOAD_DIR);
        } catch (Exception e) {
            log.error("Failed to create directory: {}", UPLOAD_DIR, e);
        }
    }


    private String getFileName(String fileNameUpload) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "_" + fileNameUpload;
    }

    public byte[] getFileByte(String fileName) {
        File file = new File(UPLOAD_DIR + File.separator + fileName);
        if (file.exists() && file.isFile()) {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }

    }

    public String saveBase64AsFile(String base64Data, String fileNameUpload) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String fileName = getFileName(fileNameUpload);
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, decodedBytes);
        return fileName;
    }


    public byte[] saveBase64AsFileByte(String base64Data) throws IOException {
        String base64String = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        return decodedBytes;
    }

    public String getFile(String fileName) {
        File file = new File(UPLOAD_DIR + File.separator + fileName);
        if (file.exists() && file.isFile()) {
            byte[] fileContent = null;
            try {
                fileContent = Files.readAllBytes(Path.of(file.getAbsolutePath()));
            } catch (IOException e) {
                return null;
            }
            return Base64.getEncoder().encodeToString(fileContent);
        } else {
            return null;
        }
    }


}
