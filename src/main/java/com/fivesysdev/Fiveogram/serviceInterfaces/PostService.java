package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.dto.PostResponseDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.User;

import java.util.List;

public interface PostService {
    List<PostResponseDTO> findAll(User user);

    Post save(String username, PostDTO postDTO) throws Status441FileIsNullException, Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequestException, Status437UserNotFoundException;

    PostResponseDTO findPostById(long id) throws Status435PostNotFoundException;

    Post editPost(String username, PostDTO postDTO, long id) throws Status441FileIsNullException, Status433NotYourPostException, Status435PostNotFoundException, Status437UserNotFoundException, Status446MarksBadRequestException;

    List<Post> deletePost(String username, long id) throws Status433NotYourPostException, Status435PostNotFoundException;


    Post reportPost(String text, long id) throws Status435PostNotFoundException;

    void banPost(Long id);
}
