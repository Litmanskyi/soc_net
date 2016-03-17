package com.socnet.service.impl;

import com.socnet.entity.User;
import com.socnet.service.UserService;
import com.socnet.utility.AuthenticatedUtils;
import com.socnet.utility.FileUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthenticatedUtils.class, FileUtils.class})
public class AvatarServiceImplTest {

    public static final String PATH = "src";
    private static User user;
    private static final String USER_ID = "1";

    @Mock
    private UserServiceImpl userService;
/*
    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setId(USER_ID);
        PowerMockito.mockStatic(AuthenticatedUtils.class);
        BDDMockito.given(AuthenticatedUtils.getCurrentAuthUser()).willReturn(user);
        Mockito.when(userService.findUserById(USER_ID)).thenReturn(user);

        PowerMockito.mockStatic(FileUtils.class);
        BDDMockito.given(FileUtils.uploadFile(dir, fileName, file)).willReturn(PATH);
    }*/
}