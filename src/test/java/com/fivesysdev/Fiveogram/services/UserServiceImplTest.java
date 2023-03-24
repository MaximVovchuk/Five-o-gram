package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.dto.UserDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.AvatarRepository;
import com.fivesysdev.Fiveogram.repositories.MarkRepository;
import com.fivesysdev.Fiveogram.repositories.SubscriptionRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.roles.Role;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostService postService;
    @Mock
    private FileService fileService;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private AvatarRepository avatarRepository;
    @Mock
    private MarkRepository markRepository;
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testFindUserById() throws Status437UserNotFoundException {
        User user = new User();
        when(userRepository.findUserById(1L)).thenReturn(user);

        User result = userService.findUserById(1L);

        assertEquals(user, result);
        verify(userRepository).findUserById(1L);
    }

    @Test
    public void testFindUserByIdNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(null);
        assertThrows(Status437UserNotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    public void testSetAvatar() throws Status441FileIsNullException {
        User user = new User();
        user.setUsername("testuser");
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(fileService.saveFile(user, multipartFile)).thenReturn("testuri");

        User result = userService.setAvatar("testuser", multipartFile);

        assertEquals(user, result);
        assertEquals("testuri", result.getAvatars().get(result.getAvatars().size() - 1).getPath());
        verify(userRepository).findUserByUsername("testuser");
        verify(fileService).saveFile(user, multipartFile);
        verify(avatarRepository).save(any(Avatar.class));
    }

    @Test()
    public void testSetAvatarNullFile() {
        assertThrows(Status441FileIsNullException.class,
                () -> userService.setAvatar("testuser", null));
    }

    @Test
    public void testGetFriendsList() throws Status437UserNotFoundException {
        User user = new User();
        user.setId(1L);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(subscriptionRepository.findAllByOwner_Id(user.getId()))
                .thenReturn(Arrays.asList(new Subscription(user, new User()),
                        new Subscription(user, new User())));
        when(userRepository.existsById(user.getId())).thenReturn(true);
        List<User> friends = userService.getFriendsList("testuser");

        assertEquals(2, friends.size());
        verify(userRepository).findUserByUsername("testuser");
        verify(subscriptionRepository).findAllByOwner_Id(user.getId());
    }

    @Test
    public void testGetFriendsListUserNotFound() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        assertThrows(Status437UserNotFoundException.class,
                () -> userService.getFriendsList("testuser"));
    }

    @Test
    public void testEditMe() throws Status439UsernameBusyException {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("oldpassword");
        user.setRole(Role.USER);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.encode("newpassword")).thenReturn("newpassword");
        when(jwtUtil.generateToken("newusername", List.of(user.getRole()))).thenReturn("jwt");
        UserDTO userDTO = UserDTO.builder().username("newusername").name("newname")
                .surname("newsurname").password("newpassword").build();

        String jwt = userService.editMe("testuser", userDTO);

        assertEquals("jwt", jwt);
        assertEquals("newusername", user.getUsername());
        assertEquals("newname", user.getName());
        assertEquals("newsurname", user.getSurname());
        assertEquals("newpassword", user.getPassword());
        verify(userRepository).findUserByUsername("testuser");
        verify(passwordEncoder).encode("newpassword");
        verify(jwtUtil).generateToken("newusername", List.of(user.getRole()));
    }

    @Test
    public void testEditMeUsernameBusy() {
        when(userRepository.existsByUsername("newusername")).thenReturn(true);
        UserDTO userDTO = UserDTO.builder().username("newusername").build();
        assertThrows(Status439UsernameBusyException.class,
                () -> userService.editMe("testuser", userDTO));
    }

    @Test
    public void testDeleteAvatar() throws Status447NotYourAvatarException, Status450AvatarNotFoundException {
        User user = new User();
        user.setUsername("testuser");
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        avatar.setUser(user);
        user.addAvatar(avatar);
        when(avatarRepository.existsById(1L)).thenReturn(true);
        when(avatarRepository.getById(1L)).thenReturn(avatar);
        userService.deleteAvatar("testuser", 1L);
        verify(avatarRepository).existsById(1L);
        verify(avatarRepository).getById(1L);
        verify(avatarRepository).deleteById(1L);
    }

    @Test
    public void testDeleteAvatarNotYourAvatar() {
        User user = new User();
        user.setUsername("testuser");
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        avatar.setUser(user);
        when(avatarRepository.existsById(1L)).thenReturn(true);
        when(avatarRepository.getById(1L)).thenReturn(avatar);
        assertThrows(Status447NotYourAvatarException.class,
                () -> userService.deleteAvatar("notmyusername", 1L));
    }

    @Test()
    public void testDeleteAvatarAvatarNotFound() {
        when(avatarRepository.existsById(1L)).thenReturn(false);
        assertThrows(Status450AvatarNotFoundException.class,
                () -> userService.deleteAvatar("testuser", 1L));
    }

    @Test
    public void testGetUserSubscriptions() throws Status437UserNotFoundException {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser1");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        when(userRepository
                .existsById(1L)).thenReturn(true);
        when(subscriptionRepository.findAllByOwner_Id(user1.getId()))
                .thenReturn(List.of(new Subscription(user1, user2)));
        List<User> subscriptions = userService.getUserSubscriptions(1L);
        assertEquals(1, subscriptions.size());
        assertEquals("testuser2", subscriptions.get(0).getUsername());
        verify(userRepository).existsById(1L);
        verify(subscriptionRepository).findAllByOwner_Id(user1.getId());
    }

    @Test
    public void testGetUserSubscriptionsUserNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(null);
        assertThrows(Status437UserNotFoundException.class,
                () -> userService.getUserSubscriptions(1L));
    }

    @Test
    public void testGetUserSubscribers() throws Status437UserNotFoundException {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser1");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        when(subscriptionRepository.findAllByFriend_id(user1.getId()))
                .thenReturn(List.of(new Subscription(user2, user1)));
        when(userRepository.existsById(1L)).thenReturn(true);
        List<User> subscribers = userService.getUserSubs(1L);
        assertEquals(1, subscribers.size());
        assertEquals("testuser2", subscribers.get(0).getUsername());
        verify(userRepository).existsById(1L);
        verify(subscriptionRepository).findAllByFriend_id(user1.getId());
    }

    @Test()
    public void testGetUserSubscribersUserNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(null);
        assertThrows(Status437UserNotFoundException.class,
                () -> userService.getUserSubs(1L));
    }


    @Test()
    public void testSetAvatarFileIsNull() {
        assertThrows(Status441FileIsNullException.class,
                () -> userService.setAvatar("testuser", null));
    }

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        User foundUser = userService.findUserByUsername("testuser");
        assertEquals(user, foundUser);
        verify(userRepository).findUserByUsername("testuser");
    }

    @Test
    public void testSearchByUsernameStartsWith() {
        User user1 = new User();
        user1.setUsername("testuser1");
        User user2 = new User();
        user2.setUsername("testuser2");
        when(userRepository.findByUsernameStartsWith("test")).thenReturn(Arrays.asList(user1, user2));
        List<User> foundUsers = userService.searchByUsernameStartsWith("test");
        assertEquals(2, foundUsers.size());
        assertEquals(user1, foundUsers.get(0));
        assertEquals(user2, foundUsers.get(1));
        verify(userRepository).findByUsernameStartsWith("test");
    }

    @Test
    public void testGetPostsWhereImMarked() {
        User user = new User();
        user.setUsername("testuser");
        Post post1 = new Post();
        Post post2 = new Post();
        Mark mark1 = new Mark(0, 0, user.getUsername()
                , new Picture(post1, "post1", new ArrayList<>()));
        Mark mark2 = new Mark(0, 0, user.getUsername(),
                new Picture(post2, "post2", new ArrayList<>()));
        when(markRepository.findByUsername("testuser")).thenReturn(Arrays.asList(mark1, mark2));
        Set<Post> posts = userService.getPostsWhereImMarked("testuser");
        assertEquals(2, posts.size());
        assertTrue(posts.contains(post1));
        assertTrue(posts.contains(post2));
        verify(markRepository).findByUsername("testuser");
    }
    @Test
    public void testGetFeed_Success() throws Status442NoFeedPostsException, Status437UserNotFoundException {
        User friend1 = new User();
        friend1.setUsername("friend1");
        User friend2 = new User();
        friend2.setUsername("friend2");
        User testuser = new User();
        testuser.setId(1L);
        testuser.setUsername("testuser");
        when(subscriptionRepository.findAllByOwner_Id(1)).thenReturn(Arrays.asList(
                new Subscription(testuser, friend1),
                new Subscription(testuser, friend2)
        ));

        Post post1 = new Post();
        post1.setId(1L);
        post1.setAuthor(friend1);
        post1.setPubDate(LocalDateTime.now());
        Post post2 = new Post();
        post2.setId(2L);
        post2.setPubDate(LocalDateTime.now());
        post2.setAuthor(friend1);
        Post post3 = new Post();
        post3.setPubDate(LocalDateTime.now());
        post3.setId(3L);
        post3.setAuthor(friend2);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(postService.findAll(friend1)).thenReturn(Arrays.asList(post1, post2));
        when(postService.findAll(friend2)).thenReturn(List.of(post3));
        when(userRepository.findUserByUsername("testuser")).thenReturn(testuser);
        List<Post> expected = Arrays.asList(post1, post2, post3);
        List<Post> actual = userService.getFeed("testuser");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFeed_NoFeedPosts() {
        User testuser = new User();
        testuser.setId(1L);
        testuser.setUsername("testuser");
        when(subscriptionRepository.findAllByOwner_Id(1)).thenReturn(new ArrayList<>());
        when(userRepository.findUserByUsername("testuser")).thenReturn(testuser);
        when(userRepository.existsById(1L)).thenReturn(true);

        assertThrows(Status442NoFeedPostsException.class, () -> userService.getFeed("testuser"));
    }

    @Test
    public void testGetFeed_UserNotFound() {
        User testuser = new User();
        testuser.setId(1L);
        testuser.setUsername("testuser");
        when(subscriptionRepository.findAllByOwner_Id(1)).thenReturn(new ArrayList<>());
        when(userRepository.findUserByUsername("testuser")).thenReturn(testuser);

        assertThrows(Status437UserNotFoundException.class, () -> userService.getFeed("testuser"));
    }
}