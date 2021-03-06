package com.socnet.configuration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("props")

@Getter
@Setter
public class ConfProperties {

    @Value("${test.data.initializer}")
    private boolean allowTestInitialize;

    @Value("${test.data.testDataResources}")
    private String testDataResources;

    @Value("${path.upload.dir}")
    private String uploadPath;

}