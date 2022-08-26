package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Like;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.LikeRepository;
import com.fivesysdev.Fiveogram.repositories.PostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.LikeService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void likePost(Post post, User whoLikes) {
        if (!likeRepository.existsByPostAndWhoLikes(post, whoLikes))
            likeRepository.save(new Like(post, whoLikes));
    }

    @Override
    public Map<String, String> unlikePost(long id) {
        Post post = postRepository.findPostById(id);
        if (post == null) {
            return Map.of("Message", "Post not found");
        }
        Like like = likeRepository.findByPostAndWhoLikes(post, Context.getUserFromContext());
        if (like == null) {
            return Map.of("Message", "You didn`t like this post");
        }
        likeRepository.delete(like);
        return Map.of("Message", "ok");
    }
}
