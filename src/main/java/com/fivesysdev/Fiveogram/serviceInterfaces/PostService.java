package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.FileException;
import com.fivesysdev.Fiveogram.exceptions.NotYourPostException;
import com.fivesysdev.Fiveogram.exceptions.PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.SponsorNotFoundException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<Post> findAll(User user);

    ResponseEntity<Post> save(String name, List<MultipartFile> multipartFiles, Long sponsorId) throws FileException, SponsorNotFoundException;

    Post findPostById(long id) throws PostNotFoundException;
    ResponseEntity<Post>editPost(long id, String text, List<MultipartFile> multipartFiles) throws FileException, NotYourPostException, PostNotFoundException;
    ResponseEntity<List<Post>> deletePost(long id) throws NotYourPostException, PostNotFoundException;


}
