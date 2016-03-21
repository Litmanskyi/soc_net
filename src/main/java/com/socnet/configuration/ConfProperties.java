package com.socnet.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter

@Component("props")
public class ConfProperties {

    @Value("${test.data.initializer}")
    private boolean allowTestInitialize;

    @Value("${test.data.testDataResources}")
    private String testDataResources;

    @Value("${path.upload.dir}")
    private String uploadPath;

}