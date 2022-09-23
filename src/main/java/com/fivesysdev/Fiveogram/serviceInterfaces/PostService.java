package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status403NotYourPostException;
import com.fivesysdev.Fiveogram.exceptions.Status404PostNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status404SponsorNotFoundException;
import com.fivesysdev.Fiveogram.exceptions.Status408FileException;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<Post> findAll(User user);

    ResponseEntity<Post> save(String name, List<MultipartFile> multipartFiles, Long sponsorId) throws Status408FileException, Status404SponsorNotFoundException;

    Post findPostById(long id) throws Status404PostNotFoundException;

    ResponseEntity<Post> editPost(long id, String text, List<MultipartFile> multipartFiles) throws Status408FileException, Status403NotYourPostException, Status404PostNotFoundException;

    ResponseEntity<List<Post>> deletePost(long id) throws Status403NotYourPostException, Status404PostNotFoundException;


}
