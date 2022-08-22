package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Picture;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Picture saveFile(MultipartFile file);
}
