package com.socnet.utility;

import com.socnet.Application;
import com.socnet.configuration.ConfProperties;
import com.socnet.entity.enumaration.FileType;
import lombok.Cleanup;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Calendar;

public class FileUtils {

    private static final String FILE_DOESN_T_EXIST = "File doesn't exist!";
    private static final String FILE_IS_EMPTY = "You failed to upload  because the multipartFile was empty";
    private static final String INCORRECT_FORMAT_FILE = "Incorrect format file!";
    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * @return path to multipartFile
     */
    public static String uploadFile(MultipartFile multipartFile, FileType fileType) {
        validateFile(multipartFile, fileType);

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException(FILE_IS_EMPTY);
        }

        ConfProperties confProperties = Application.getBean(ConfProperties.class);

        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());

        String userDir = Paths.get(fileName, fileType.name() + "/").toString();

        //todo ++ refactor
        File pathDir = new File(confProperties.getUploadPath() + userDir);

        if (pathDir.mkdirs()) {
            logger.info("Path directory: '" + pathDir.getPath() + "'was created");
        }

        File file = new File(pathDir.getPath() + "/" + fileName);

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

    private static void validateFile(MultipartFile file, FileType fileType) {
        if (ValidateFile.isValidFile(file, fileType)) { // todo ++ move to constants and create method public static boolean isValidFile(File file, Type type -- image, video, ect)
            throw new IllegalArgumentException(INCORRECT_FORMAT_FILE);
        }
    }
}
