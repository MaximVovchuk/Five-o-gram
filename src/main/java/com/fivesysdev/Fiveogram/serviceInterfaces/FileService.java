package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(User user, MultipartFile file) throws Status441FileIsNullException;

    void deleteFile(String path);
}
