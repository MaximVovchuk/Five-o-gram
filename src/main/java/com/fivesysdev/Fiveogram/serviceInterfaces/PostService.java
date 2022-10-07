package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status433NotYourPostException;
import com.fivesysdev.Fiveogram.exceptions.Status435PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status436SponsorNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<Post> findAll(User user);

    ResponseEntity<Post> save(String username,String name, List<MultipartFile> multipartFiles, Long sponsorId) throws Status441FileException, Status436SponsorNotFoundException;

    Post findPostById(long id) throws Status435PostNotFoundException;

    ResponseEntity<Post> editPost(String username, long id, String text, List<MultipartFile> multipartFiles) throws Status441FileException, Status433NotYourPostException, Status435PostNotFoundException;

    ResponseEntity<List<Post>> deletePost(String username, long id) throws Status433NotYourPostException, Status435PostNotFoundException;


}
