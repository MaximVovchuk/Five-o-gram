package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<Post> findAll(User user);

    Post save(String username, PostDTO postDTO) throws Status441FileIsNullException, Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequest;

    Post findPostById(long id) throws Status435PostNotFoundException;

    Post editPost(String username, long id, String text, List<MultipartFile> multipartFiles) throws Status441FileIsNullException, Status433NotYourPostException, Status435PostNotFoundException;

    List<Post> deletePost(String username, long id) throws Status433NotYourPostException, Status435PostNotFoundException;


    Post reportPost(String text,long id) throws Status435PostNotFoundException;

    void banPost(Long id);
}
