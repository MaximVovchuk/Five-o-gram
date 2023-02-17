package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status438PostAlreadyLikedException;
import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.LikeRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private SponsoredPostRepository sponsoredPostRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    @Test
    void likePost_postNotFound_shouldThrowException() throws Status435PostNotFoundException {
        when(postService.findPostById(anyLong())).thenReturn(null);

        assertThrows(Status435PostNotFoundException.class, () ->
                likeService.likePost("user1", 1L)
        );
    }

    @Test
    void likePost_postAlreadyLiked_shouldThrowException() throws Status435PostNotFoundException {
        Post post = new Post();
        User user = new User();

        when(postService.findPostById(anyLong())).thenReturn(post);
        when(userService.findUserByUsername(anyString())).thenReturn(user);
        when(likeRepository.existsByPostAndWhoLikes(any(), any())).thenReturn(true);

        assertThrows(Status438PostAlreadyLikedException.class, () ->
                likeService.likePost("user1", 1L)
        );
    }

    @Test
    void likePost_validInput_shouldReturnPost() throws Status435PostNotFoundException, Status438PostAlreadyLikedException {
        Post post = new Post();
        User user = new User();

        when(postService.findPostById(1L)).thenReturn(post);
        when(userService.findUserByUsername("username")).thenReturn(user);
        when(likeRepository.existsByPostAndWhoLikes(post, user)).thenReturn(false);
        when(sponsoredPostRepository.existsByPost(post)).thenReturn(false);

        assertEquals(post, likeService.likePost("username", 1L));

        verify(likeRepository).save(any());
        verify(notificationService).sendNotification(any());
    }

    @Test
    public void unlikePost_withPostAndUsername_returnPost() throws Exception {
// Given
        String username = "JohnDoe";
        Post post = new Post();
        post.setId(1L);
        when(postService.findPostById(1L)).thenReturn(post);
        User user = new User();
        user.setUsername(username);
        when(userService.findUserByUsername(username)).thenReturn(user);

        Post result = likeService.unlikePost(username, 1L);

        assertEquals(post, result);
        verify(likeRepository).deleteByPostAndWhoLikes(post, user);
    }

    @Test
    public void unlikePost_withNonExistingPost_throwsException() throws Exception {

        String username = "JohnDoe";
        when(postService.findPostById(1L)).thenReturn(null);
        assertThrows(Status435PostNotFoundException.class,
                () -> likeService.unlikePost(username, 1L));
    }

    @Test
    public void findAllPostLikes_withPost_returnsLikes() throws Exception {
        Post post = new Post();
        post.setId(1L);
        when(postService.findPostById(1L)).thenReturn(post);
        Set<Like> likes = new HashSet<>();
        Like like = new Like();
        likes.add(like);
        when(likeRepository.findAllByPost(post)).thenReturn(likes);
        Set<Like> result = likeService.findAllPostLikes(1L);

        assertEquals(likes, result);
        verify(likeRepository).findAllByPost(post);
    }

    @Test
    public void findAllPostLikes_withNonExistingPost_throwsException() throws Exception {
        when(postService.findPostById(1L)).thenReturn(null);
        assertThrows(Status435PostNotFoundException.class,
                () -> likeService.findAllPostLikes(1L));
    }
}