package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.FileException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file) throws FileException;

    void deleteFile(String path);
}
