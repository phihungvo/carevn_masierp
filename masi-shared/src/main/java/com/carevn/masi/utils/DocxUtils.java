package com.carevn.masi.utils;

import com.deepoove.poi.XWPFTemplate;

import java.io.*;
import java.util.Map;
import java.util.UUID;

public class DocxUtils {
    public static String renderTemplate(InputStream templateStream, Map<String, Object> data) throws IOException {
        var docx = XWPFTemplate.compile(templateStream);
        var timestamp = System.currentTimeMillis();
        var template = docx.render(data);
        var fileName = "uploaded-files/" + timestamp + "-" + UUID.randomUUID().toString() + ".docx";
        var fileOutputStream = new FileOutputStream(fileName);
        template.write(fileOutputStream);
        fileOutputStream.close();
        return fileName;
    }

    public static String renderTemplate(InputStream templateStream, Map<String, Object> data, FileManager fileManager)
            throws IOException {
        var docx = XWPFTemplate.compile(templateStream);
        var template = docx.render(data);
        var fileName = fileManager.getFilePath(UUID.randomUUID().toString() + ".docx").toString();
        var fileOutputStream = fileManager.getFileOutputStream(fileName);
        template.write(fileOutputStream);
        fileOutputStream.close();

        return fileName;
    }
}
