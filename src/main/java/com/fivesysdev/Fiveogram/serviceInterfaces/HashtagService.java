package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.models.Hashtag;
import com.fivesysdev.Fiveogram.models.Post;

import java.util.List;

public interface HashtagService {
    void save(Hashtag hashtag);
    void saveAllHashtagsFromPost(Post post);
    void deleteAllHashtagsFromPost(Post post);
    List<Post> getPostsByHashtags(List<String> hashtags);

}
