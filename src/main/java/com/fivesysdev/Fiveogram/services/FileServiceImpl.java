package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FileServiceImpl implements FileService {
    @Value("${file.storage.prefix}")
    private String image;
    @Value("${file.storage.mapping}")
    private String foldPath;
    private final Environment environment;

    public FileServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String saveFile(User user, MultipartFile file) throws Status441FileException {
        String fileName = FileUtil.getFileName(file.getOriginalFilename());
        String filePath = "C:/Users/tutil/IdeaProjects/Five-o-gram-pictures/"
                + user.getUsername() + "/";
        String fullPath = "C:/Users/tutil/IdeaProjects/Five-o-gram-pictures/"
                + user.getUsername() + "/" + fileName;

        Picture picture = new Picture();
        picture.setPath(fullPath);
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String port = environment.getProperty("local.server.port");
        String hostName = "localhost";

        return String.format("http://%s:%s%s/%s/%s", hostName, port, image,
                user.getUsername(), fileName);
    }

    @Override
    public void deleteFile(String path) {
        String imgPath = path.substring(28);
        String folderPath = foldPath.substring(8);
        File targetFile = new File(folderPath + imgPath);
        targetFile.delete();
    }
}
