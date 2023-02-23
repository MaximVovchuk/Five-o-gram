package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Hashtag;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.repositories.HashtagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HashtagServiceImplTest {
    @InjectMocks
    private HashtagServiceImpl hashtagService;

    @Mock
    private HashtagRepository hashtagRepository;

    @Test
    public void testSave() {
        Hashtag hashtag = new Hashtag("testHashtag", new Post());
        hashtagService.save(hashtag);
        verify(hashtagRepository, times(1)).save(hashtag);
    }

    @Test
    public void testSaveAllHashtagsFromPost() {
        Post post = new Post();
        post.setText("This is a #test post #with hashtags.");
        hashtagService.saveAllHashtagsFromPost(post);
        verify(hashtagRepository, times(2)).save(any());
    }

    @Test
    public void testDeleteAllHashtagsFromPost() {
        Post post = new Post();
        hashtagService.deleteAllHashtagsFromPost(post);
        verify(hashtagRepository, times(1)).deleteAllByPost(post);
    }
    @Test
    public void testGetPostsByHashtags() {
        // Create sample hashtags and posts
        Hashtag hashtag1 = new Hashtag();
        hashtag1.setContent("food");
        Hashtag hashtag2 = new Hashtag();
        hashtag2.setContent("travel");
        Hashtag hashtag3 = new Hashtag();
        hashtag3.setContent("foodie");
        Hashtag hashtag4 = new Hashtag();
        hashtag4.setContent("travel");
        Post post1 = new Post();
        post1.setText("I love #food and #travel");
        post1.setHashtags(List.of(hashtag1,hashtag2));
        hashtag1.setPost(post1);
        hashtag2.setPost(post1);
        Post post2 = new Post();
        post2.setText("Exploring new #foodie places is my hobby");
        post2.setHashtags(List.of(hashtag3));
        hashtag3.setPost(post2);
        Post post3 = new Post();
        post3.setText("I can't wait to #travel again");
        post3.setHashtags(List.of(hashtag4));
        hashtag4.setPost(post3);

        // Mock the repository method
        when(hashtagRepository.findAllByContent("food")).thenReturn(List.of(hashtag1));
        when(hashtagRepository.findAllByContent("travel")).thenReturn(List.of(hashtag4,hashtag2));

        // Test the service method
        List<String> queryHashtags = new ArrayList<>();
        queryHashtags.add("food");
        queryHashtags.add("travel");
        List<Post> result = hashtagService.getPostsByHashtags(queryHashtags);

        // Verify the results
        assertEquals(1, result.size());
        assertEquals(post1, result.get(0));
    }
}