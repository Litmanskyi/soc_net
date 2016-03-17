package com.socnet.utility;

import com.socnet.Application;
import com.socnet.configuration.ConfProperties;
import lombok.Cleanup;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {

    public static final String FILE_DOESN_T_EXIST = "File doesn't exist!";
    public static final String FILE_IS_EMPTY = "You failed to upload  because the multipartFile was empty";
    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * @return path to multipartFile
     */
    public static String uploadFile(String dir, String fileName, MultipartFile multipartFile) {

        ConfProperties confProperties = Application.getBean(ConfProperties.class);

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException(FILE_IS_EMPTY);
        }

        logger.info(dir);
        //todo ++ refactor
        File pathDir = new File(confProperties.getUploadPath() + dir);

        if (pathDir.mkdirs()) {
            logger.info("Path directory: '" + pathDir.getPath() + "'was created");
        }

        File file = new File(pathDir.getPath()+"/" + fileName);

        try {
            @Cleanup BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            FileCopyUtils.copy(multipartFile.getInputStream(), stream);
            return file.getPath();
        } catch (Exception e) {
            throw new IllegalStateException("You failed to upload  => " + e.getMessage());
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException(FILE_DOESN_T_EXIST);
        }
        file.delete();
    }
}
