package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status432NotYourCommentException;
import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status448TextIsNullException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.SponsoredPost;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.serviceInterfaces.PostService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private PostService postService;

    @Mock
    private SponsoredPostRepository sponsoredPostRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void testSave_shouldSaveCommentAndSendNotification() throws Status435PostNotFoundException, Status448TextIsNullException {
        String username = "user1";
        long postId = 1;
        String text = "Comment text";
        User user = new User();
        Post post = new Post();
        post.setId(postId);
        post.setAuthor(user);
        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(user);
        comment.setId(1);
        comment.setPost(post);
        SponsoredPost sponsoredPost = new SponsoredPost();
        User sponsor = new User();
        sponsoredPost.setSponsor(sponsor);

        when(postService.findPostById(postId)).thenReturn(post);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(sponsoredPostRepository.findByPost(post)).thenReturn(sponsoredPost);
        when(sponsoredPostRepository.existsByPost(post)).thenReturn(true);

        Comment result = commentService.save(username, postId, text);

        assertNotNull(result);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(notificationService, times(1)).sendNotification(any());
        verify(sponsoredPostRepository, times(1)).existsByPost(post);
        verify(sponsoredPostRepository, times(1)).findByPost(post);
        verify(userService, times(1)).findUserByUsername(username);
        verify(postService, times(1)).findPostById(postId);
    }

    @Test
    void testSave_shouldThrowStatus435PostNotFoundException() throws Status435PostNotFoundException {
        when(postService.findPostById(1)).thenReturn(null);
        assertThrows(Status435PostNotFoundException.class, () -> commentService.save("user1", 1, "Comment text"));
    }

    @Test
    void testSave_shouldThrowStatus448TextIsNullException() {
        assertThrows(Status448TextIsNullException.class, () -> commentService.save("user1", 1, null));
    }

    @Test
    void testEdit() throws Status432NotYourCommentException, Status434CommentNotFoundException {
        Comment comment = new Comment();
        User author = new User();
        comment.setAuthor(author);

        when(commentRepository.findCommentById(1L)).thenReturn(comment);
        when(userService.findUserByUsername("testuser")).thenReturn(author);

        Comment newComment = commentService.editComment("testuser", 1L, "newText");

        assertEquals(newComment.getText(), "newText");
    }

    @Test
    void testEditThrows434() {
        when(commentRepository.findCommentById(1L)).thenReturn(null);

        assertThrows(Status434CommentNotFoundException.class,
                () -> commentService.editComment("testuser", 1L, "newText"));
    }

    @Test
    void testEditThrows432() {
        Comment comment = new Comment();
        User author = new User();
        comment.setAuthor(author);

        when(commentRepository.findCommentById(1L)).thenReturn(comment);
        when(userService.findUserByUsername("testuser")).thenReturn(new User());

        assertThrows(Status432NotYourCommentException.class,
                () -> commentService.editComment("testuser", 1L, "newText"));
    }

    @Test
    void testDeleteCommentWithCommentAuthor() throws Status432NotYourCommentException, Status434CommentNotFoundException {
        User postAuthor = new User();
        Post post = new Post();
        post.setAuthor(postAuthor);
        Comment comment = new Comment();
        comment.setPost(post);
        User commentAuthor = new User();
        comment.setAuthor(commentAuthor);

        when(commentRepository.findCommentById(1L)).thenReturn(comment);
        when(userService.findUserByUsername("testuser")).thenReturn(commentAuthor);

        commentService.deleteComment("testuser", 1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void testDeleteCommentWithPostAuthor() throws Status432NotYourCommentException, Status434CommentNotFoundException {
        User postAuthor = new User();
        Post post = new Post();
        post.setAuthor(postAuthor);
        Comment comment = new Comment();
        comment.setPost(post);
        User commentAuthor = new User();
        comment.setAuthor(commentAuthor);

        when(commentRepository.findCommentById(1L)).thenReturn(comment);
        when(userService.findUserByUsername("testuser")).thenReturn(postAuthor);

        commentService.deleteComment("testuser", 1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void testDeleteCommentThrows432() {
        User postAuthor = new User();
        Post post = new Post();
        post.setAuthor(new User());
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(new User());

        when(commentRepository.findCommentById(1L)).thenReturn(comment);
        when(userService.findUserByUsername("testuser")).thenReturn(postAuthor);

        assertThrows(Status432NotYourCommentException.class,
                () -> commentService.deleteComment("testuser", 1L));
    }

    @Test
    void testDeleteThrows434() {
        when(commentRepository.findCommentById(1L)).thenReturn(null);

        assertThrows(Status434CommentNotFoundException.class,
                () -> commentService.deleteComment("testuser", 1L));
    }
}