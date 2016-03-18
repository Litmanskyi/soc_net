package com.socnet.utility;

import com.socnet.entity.enumaration.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class ValidateFile {
    private final static String[] imageFormat = {
            "image/jpeg",
            "image/png",
            "image/pjpeg"
    };
    public static boolean isValidFile(MultipartFile file, FileType type) {
        if(type==FileType.IMAGE){
            return !Arrays.asList(imageFormat).contains(file.getContentType());
        }else
            throw new IllegalArgumentException();
    }
}
