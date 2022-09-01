package com.fivesysdev.Fiveogram.serviceInterfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file);
}
