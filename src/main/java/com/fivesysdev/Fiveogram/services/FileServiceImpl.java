package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.FileException;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.repositories.PictureRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class FileServiceImpl implements FileService {
    private final PictureRepository pictureRepository;

    public FileServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public Picture saveFile(MultipartFile file) throws FileException {
        String fileName = FileUtil.getFileName(file.getOriginalFilename());
        String filePath = "C:/Users/tutil/IdeaProjects/Five-o-gram-pictures/";
        String fullPath = "C:/Users/tutil/IdeaProjects/Five-o-gram-pictures/" + fileName;

        System.out.println("Токен файла: " + fileName);
        System.out.println("Имя файла: " + file.getOriginalFilename());
        System.out.println("FullPath: " + fullPath);

        Picture picture = new Picture();

        picture.setPath(fullPath);
        picture.setToken(fileName);
        picture.setCreated(LocalDateTime.now());
        pictureRepository.save(picture);
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picture;
    }
}
