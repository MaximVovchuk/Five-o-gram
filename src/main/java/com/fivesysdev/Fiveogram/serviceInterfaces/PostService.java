package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;

import java.util.List;

public interface PostService {
    List<Post> findAll(User user);

    Post save(String username, PostDTO postDTO) throws Status441FileIsNullException, Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequest, Status437UserNotFoundException;

    Post findPostById(long id) throws Status435PostNotFoundException;

    Post editPost(String username, PostDTO postDTO, long id) throws Status441FileIsNullException, Status433NotYourPostException, Status435PostNotFoundException, Status437UserNotFoundException, Status446MarksBadRequest;

    List<Post> deletePost(String username, long id) throws Status433NotYourPostException, Status435PostNotFoundException;


    Post reportPost(String text, long id) throws Status435PostNotFoundException;

    void banPost(Long id);
}
