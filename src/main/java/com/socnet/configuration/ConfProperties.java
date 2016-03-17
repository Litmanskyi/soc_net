package com.socnet.configuration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * Created by anton on 22.02.16.
*/



@Service("props")

@Getter
@Setter
public class ConfProperties {

    @Value("${test.data.initializer}")
    private boolean allowTestInitialize;

    @Value("${test.data.testDataResources}")
    private String testDataResources;

}