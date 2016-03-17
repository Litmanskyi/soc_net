package com.socnet.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socnet.authentication.CurrentUser;
import com.socnet.configuration.ConfProperties;
import com.socnet.entity.Post;
import com.socnet.entity.User;
import com.socnet.service.PostService;
import com.socnet.service.RelationService;
import com.socnet.service.UserService;
import com.socnet.service.WallService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TestDataInitializer {

    private static final String FILE_EXIST_MESSAGE = "%s in folder %s does not exist";
    private static final Logger LOGGER = Logger.getLogger(TestDataInitializer.class.getName());
    private final ClassLoader classLoader = getClass().getClassLoader();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();

    @Autowired
    private UserService userService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private PostService postService;

    @Autowired
    private WallService wallService;

    private Path testDataPath;

    @Autowired
    private ConfProperties confProperties;



    @PostConstruct
    public void initPath() {
        testDataPath = Paths.get(classLoader.getResource(confProperties.getTestDataResources()).getPath());
    }

    public void addTestData() throws IOException {

        // todo need fetch absolute path without classLoader!
//        testDataPath = Paths.get(confProperties.getTestDataResources());

        Files.walkFileTree(testDataPath, new TestDataVisitor());

        LOGGER.info("!!!!!!!!!! INITIALIZE TEST DATA FINISHED!!!!!!!!");

        confProperties.setAllowTestInitialize(false);
    }

    private class TestDataVisitor extends SimpleFileVisitor<Path> {

        File usersFile;
        File postsFile;

        User[] userList;
        Post[] postList;

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

            usersFile = new File(dir.toString(), "users.json");
            postsFile = new File(dir.toString(), "posts.json");

            if (!dir.equals(testDataPath)) {

                if (!usersFile.exists()) {
                    throw new IllegalArgumentException(String.format(FILE_EXIST_MESSAGE, usersFile.getName(), dir.getFileName()));
                }
                if (!postsFile.exists()) {
                    throw new IllegalArgumentException(String.format(FILE_EXIST_MESSAGE, postsFile.getName(), dir.getFileName()));
                }

                userList = objectMapper.readValue(usersFile, User[].class);
                postList = objectMapper.readValue(postsFile, Post[].class);

                //todo create method createUsers(User[] users) and same for posts
                List<User> users = new ArrayList<>();
                for (User user : userList) {
                    users.add(userService.createUser(user));
                }

                for (Post post : postList) {
                    User userWall = users.get(random.nextInt(users.size() - 1));
                    User userPosted = users.get(random.nextInt(users.size() - 1));
                    login(userPosted.getEmail());
                    post.setWall(userWall.getWall());
                    post.setCreator(userPosted);
                    postService.addPostToUserWall(userWall.getId(), post);
                }
                for (User user : userList) {
                    login(user.getEmail());//todo use random
                    for (int i = 0; i < 8; i++) {
                        try {
                            int r = random.nextInt(5 - 1 + 1) + 1;
                            String uId = users.get(random.nextInt(users.size() - 1)).getId();
                            if (r >= 1 && r <= 4) {
                                relationService.addFriend(uId);
                            } else if (r == 5) {
                                relationService.addToBlacklist(uId);
                            }
                        } catch (Exception e) {
                            System.out.println("Exception" + e.getMessage());
                        }
                    }
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }

    @Autowired
    @Qualifier("currentUserDetailsService")
    UserDetailsService userDetailsService;

    public void login(String email) {
        CurrentUser currentUser = (CurrentUser) userDetailsService.loadUserByUsername(email);
        Authentication authenticate = new UsernamePasswordAuthenticationToken(currentUser,
                currentUser.getPassword(),
                currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }

}
