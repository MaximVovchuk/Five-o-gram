package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status434CommentNotFoundException;
import com.fivesysdev.Fiveogram.models.Comment;
import com.fivesysdev.Fiveogram.models.CommentLike;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.models.notifications.CommentLikeNotification;
import com.fivesysdev.Fiveogram.repositories.CommentLikeRepository;
import com.fivesysdev.Fiveogram.repositories.CommentRepository;
import com.fivesysdev.Fiveogram.repositories.UserRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceImplTest {
    @InjectMocks
    private CommentLikeServiceImpl commentLikeService;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @Test
    void setLike_ShouldThrowStatus434CommentNotFoundException_WhenCommentIsNull() {
        // Arrange
        when(commentRepository.findCommentById(anyLong())).thenReturn(null);

        // Act & Assert
        assertThrows(Status434CommentNotFoundException.class, () -> commentLikeService.setLike("user", 1L));
    }

    @Test
    void setLike_ShouldReturnPost_WhenCommentIsNotNull() throws Status434CommentNotFoundException {
        // Arrange
        Comment comment = new Comment();
        comment.setPost(new Post());
        User user = new User();
        when(commentRepository.findCommentById(anyLong())).thenReturn(comment);
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);
        when(commentLikeRepository.existsByAuthorAndComment(user, comment)).thenReturn(true);

        // Act
        Post result = commentLikeService.setLike("user", 1L);

        // Assert
        assertNotNull(result);
        assertEquals(comment.getPost(), result);
        verify(commentLikeRepository, times(0)).save(any());
    }

    @Test
    void testSetLike() throws Status434CommentNotFoundException {
        // Arrange
        Comment comment = new Comment();
        User user = new User();
        when(commentRepository.findCommentById(anyLong())).thenReturn(comment);
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);
        when(commentLikeRepository.existsByAuthorAndComment(any(User.class), any(Comment.class))).thenReturn(false);

        // Act
        commentLikeService.setLike("user", 1L);

        // Assert
        verify(commentLikeRepository, times(1)).save(any(CommentLike.class));
        verify(notificationService, times(1)).sendNotification(any(CommentLikeNotification.class));
    }

    @Test
    void testDeleteLike() throws Status434CommentNotFoundException {
        Comment comment = new Comment();
        Post post = new Post();
        User user = new User();
        comment.setAuthor(user);
        comment.setPost(post);

        when(commentRepository.findCommentById(1L)).thenReturn(comment);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);

        Post resultPost = commentLikeService.deleteLike("testuser", 1L);
        verify(commentLikeRepository).deleteByAuthorAndComment(user, comment);
        assertEquals(resultPost, post);
    }

    @Test
    void testDeleteLikeThrows434() {
        when(commentRepository.findCommentById(1L)).thenReturn(null);

        assertThrows(Status434CommentNotFoundException.class,
                () -> commentLikeService.deleteLike("testuser", 1L));
    }
}