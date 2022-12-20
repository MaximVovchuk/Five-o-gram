package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Hashtag;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.repositories.HashtagRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.HashtagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        String[] words = Arrays.copyOfRange(texts,1,texts.length);
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
        List<Post> posts = hashtagRepository.findAllByContent(hashtags.get(0)).stream()
                .map(Hashtag::getPost).toList();
        if (hashtags.size() == 1) return posts;
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            List<String> hashtagTexts = post.getHashtags().stream().map(Hashtag::getContent).toList();
            if (new HashSet<>(hashtagTexts).containsAll(hashtags)) {
                result.add(post);
            }
        }
        return result;
    }
}
