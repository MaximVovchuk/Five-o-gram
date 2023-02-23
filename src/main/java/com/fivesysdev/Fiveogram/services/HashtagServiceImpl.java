package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Hashtag;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.repositories.HashtagRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.HashtagService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository hashtagRepository;

    public HashtagServiceImpl(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Override
    public void save(Hashtag hashtag) {
        hashtagRepository.save(hashtag);
    }

    @Override
    public void saveAllHashtagsFromPost(Post post) {
        String[] texts = post.getText().split("#");
        String[] words = Arrays.copyOfRange(texts, 1, texts.length);
        List<String> hashtags = new ArrayList<>();
        for (String word : words) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (c == ' ') {
                    hashtags.add(sb.toString());
                    break;
                }
                sb.append(c);
            }
        }
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
