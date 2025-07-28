package com.carevn.masi.utils;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;

public class FileManager {
    private final String UPLOAD_DIR;

    public FileManager(String upload_dir) throws IOException {
        UPLOAD_DIR = "uploaded-files/" + upload_dir;
        makeSureUploadDirectoryExist();
    }

    private void makeSureUploadDirectoryExist() throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));

    }

    public Mono<String> saveFile(String rawFileName, byte[] fileContent) throws IOException {
        Long currentTime = System.currentTimeMillis();
        String fileName = String.format("%s/%d-%s", UPLOAD_DIR, currentTime, rawFileName);
        return Mono.fromCallable(() -> {
            Files.write(Paths.get(fileName), fileContent);
            return fileName;
        });
    }
 

    public FileOutputStream getFileOutputStream(String absolutePath) throws IOException {
        Path path = Paths.get(absolutePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return new FileOutputStream(path.toAbsolutePath().toFile());
    }

    public byte[] getFileBase64(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] fileBytes = fileInputStream.readAllBytes();
            fileInputStream.close();
            return fileBytes;
        } catch (Exception ignored) {
            return null;
        }

    }

    public Path getFilePath(String filePath) {
        Long currentTime = System.currentTimeMillis();
        return Paths.get(String.format("%s/%d-%s", UPLOAD_DIR, currentTime, filePath));
    }

    private Mono<Void> deleteFile(String filePath) {
        return Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException ignored) {
            }
        });
    }

    public void deleteManyFiles(Predicate<Tuple2<String, Long>> predicate) {
        try {
            var files = Files.list(Paths.get(UPLOAD_DIR))
                    .filter(file -> !Files.isDirectory(file))
                    .map(path -> {
                        String fileName = path.getFileName().toString();
                        String[] parts = fileName.split("-");
                        Long timestamp = NumberUtils.toLong(parts[0], 0);
                        return Tuples.of(path.toString(), timestamp);
                    })
                    .filter(predicate)
                    .toList();
            files.forEach(file -> {
                try {
                    Files.deleteIfExists(Paths.get(file.getT1()));
                } catch (IOException e) {
                    System.err.println("Failed to delete file: " + file.getT1());
                }
            });
        } catch (IOException ignored) {
        }
    }

}
