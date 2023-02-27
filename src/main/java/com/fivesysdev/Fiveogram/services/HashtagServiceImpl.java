package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Hashtag;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.repositories.HashtagRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.HashtagService;
import com.fivesysdev.Fiveogram.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository hashtagRepository;

    @Override
    public void save(Hashtag hashtag) {
        hashtagRepository.save(hashtag);
    }

    @Override
    public void saveAllHashtagsFromPost(Post post) {
        String[] texts = post.getText().split("#");
        List<String> hashtags = StringUtil.get(texts);
        for (String hashtagText : hashtags) {
            Hashtag hashtag = new Hashtag(hashtagText, post);
            save(hashtag);
        }
    }

    @Override
    public void deleteAllHashtagsFromPost(Post post) {
        hashtagRepository.deleteAllByPost(post);
    }


    @Override
    public List<Post> getPostsByHashtags(List<String> hashtags) {
        if (hashtags.isEmpty()) return new ArrayList<>();
        List<Hashtag> foundHashtags = new ArrayList<>();
        for (String hashtag : hashtags) {
            foundHashtags.addAll(hashtagRepository.findAllByContent(hashtag));
        }
        Set<Post> result = new HashSet<>();
        foundHashtags.stream().map(Hashtag::getPost).forEach(post -> {
            if (post.getHashtags().stream().map(Hashtag::getContent).collect(Collectors.toSet()).containsAll(hashtags)) {
                result.add(post);
            }
        });
        return result.stream().toList();
    }
}
