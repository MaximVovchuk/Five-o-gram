package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.FileException;
import com.fivesysdev.Fiveogram.models.Picture;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.util.Context;
import com.fivesysdev.Fiveogram.util.FileUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    private final Environment environment;

    public FileServiceImpl(Environment environment) {
        this.environment = environment;
    }

    public String saveFile(MultipartFile file) throws FileException {
        String fileName = FileUtil.getFileName(file.getOriginalFilename());
        String filePath = "C:/Users/tutil/IdeaProjects/Five-o-gram-pictures/"
                + Context.getUserFromContext().getUsername() + "/";
        String fullPath = "C:/Users/tutil/IdeaProjects/Five-o-gram-pictures/"
                + Context.getUserFromContext().getUsername() + "/" + fileName;

        Picture picture = new Picture();
        picture.setPath(fullPath);
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String port = environment.getProperty("local.server.port");
        String hostName = "localhost";
//            String hostName = InetAddress.getLocalHost().getHostName();

        return String.format("http://%s:%s/%s/%s", hostName, port,
                Context.getUserFromContext().getUsername(), fileName);
    }
}
