package com.socnet.utility;

import com.socnet.Application;
import lombok.Cleanup;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {

    public static final String FILE_DOESN_T_EXIST = "File doesn't exist!";
    public static final String FILE_IS_EMPTY = "You failed to upload  because the file was empty";
    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * @return path to file
     */
    public static String uploadFile(String dir, String fileName, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException(FILE_IS_EMPTY);
        }

        logger.info(dir);
        //todo refactor
        File f = new File(Application.ROOT + dir);

        if (f.mkdirs()) {
            logger.info("Path directory: '" + f.getPath() + "'was created");
        }

        f = new File(f.getPath()+"/" + fileName);

        try {
            @Cleanup BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f));
            FileCopyUtils.copy(file.getInputStream(), stream);
            return f.getPath();
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
