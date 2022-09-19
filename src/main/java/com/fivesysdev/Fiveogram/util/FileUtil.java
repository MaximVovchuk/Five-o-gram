package com.fivesysdev.Fiveogram.util;

import com.fivesysdev.Fiveogram.exceptions.Status408FileException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static String getSuffix(String fileName) throws Status408FileException {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        }catch (StringIndexOutOfBoundsException ex){
            throw new Status408FileException();
        }
    }

    public static String getFileName(String fileOriginName) throws Status408FileException {
        return getUUID() + getSuffix(fileOriginName);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();//.replace("-","");
    }
}

