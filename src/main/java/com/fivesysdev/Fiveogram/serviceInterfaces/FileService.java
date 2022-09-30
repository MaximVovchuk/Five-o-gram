package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file) throws Status441FileException;

    void deleteFile(String path);
}
