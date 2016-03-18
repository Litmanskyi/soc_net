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
import java.net.URL;
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

    private static final int RELATION_MIN = 5;
    private static final int RELATION_MAX = 15;
    private static final int BANNED_PERCENT = 10;

    @Autowired
    private UserService userService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private PostService postService;

    @Autowired
    private ConfProperties confProperties;

    @Autowired
    @Qualifier("currentUserDetailsService")
    UserDetailsService userDetailsService;

    private Path testDataPath;

    @PostConstruct
    public void initPath() {
        testDataPath = Paths.get(classLoader.getResource(confProperties.getTestDataResources()).getPath());
    }

    public void addTestData() throws IOException {
        Files.walkFileTree(testDataPath, new TestDataVisitor());

        LOGGER.info("!!!!!!!!!! INITIALIZE TEST DATA FINISHED!!!!!!!!");

        confProperties.setAllowTestInitialize(false);
    }

    private class TestDataVisitor extends SimpleFileVisitor<Path> {

        File usersFile;
        File postsFile;

        User[] users;
        Post[] posts;

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

                users = objectMapper.readValue(usersFile, User[].class);
                posts = objectMapper.readValue(postsFile, Post[].class);

                //todo create method createUsers(User[] users) and same for posts
                List<User> userList;

                userList = createUsers(users);

                createPosts(posts, userList);

                createRelations(userList);

            }
            return FileVisitResult.CONTINUE;
        }
    }

    private List<User> createUsers(User[] users) {
        List<User> persistUsers = new ArrayList<>();
        for (User user : users) {
            persistUsers.add(userService.createUser(user));
        }
        return persistUsers;
    }

    private void createPosts(Post[] posts, List<User> userList) {
        for (Post post : posts) {
            User userSender = userList.get(random.nextInt(userList.size()));
            User userReceiver = userList.get(random.nextInt(userList.size()));
            login(userSender.getEmail());
            post.setWall(userReceiver.getWall());
            post.setCreator(userSender);
            postService.addPostToUserWall(userReceiver.getId(), post);
        }
    }

    private void createRelations(List<User> userList) {
        for (User user : userList) {
            login(user.getEmail());//todo +++ use random - wrong

            int quantity = RELATION_MIN + random.nextInt(RELATION_MAX - RELATION_MIN + 1);
            String targetUserId = userList.get(random.nextInt(userList.size())).getId();
            int lucky_number = random.nextInt(100);

            for (int i = 0; i < quantity; i++) {
                try {
                    if (lucky_number < BANNED_PERCENT) {
                        relationService.addFriend(targetUserId);
                    } else {
                        relationService.addToBlacklist(targetUserId);
                    }
                } catch (Exception e) {
                    System.out.println("Exception" + e.getMessage());
                }
            }
        }
    }

    private void login(String email) {
        CurrentUser currentUser = (CurrentUser) userDetailsService.loadUserByUsername(email);
        Authentication authenticate = new UsernamePasswordAuthenticationToken(currentUser,
                currentUser.getPassword(),
                currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }

}
