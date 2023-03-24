package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.dto.MarkDTO;
import com.fivesysdev.Fiveogram.dto.PostDTO;
import com.fivesysdev.Fiveogram.exceptions.*;
import com.fivesysdev.Fiveogram.models.*;
import com.fivesysdev.Fiveogram.repositories.*;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.HashtagService;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostReportRepository postReportRepository;
    @Mock
    private SponsoredPostRepository sponsoredPostRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private MarkRepository markRepository;
    @Mock
    private FileService fileService;
    @Mock
    private HashtagService hashtagService;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void testFindAll() {
        User user = new User();
        List<Post> expectedPosts = new ArrayList<>();
        expectedPosts.add(new Post());
        expectedPosts.add(new Post());
        when(postRepository.findAllByAuthor(user)).thenReturn(expectedPosts);

        List<Post> actualPosts = postService.findAll(user);
        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    public void testSave() throws Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequestException, Status441FileIsNullException, Status437UserNotFoundException {
        User user = new User();
        user.setUsername("testuser");
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile mock = mock(MultipartFile.class);
        multipartFiles.add(mock);
        PostDTO postDTO = PostDTO.builder().text("test post").multipartFiles(multipartFiles).build();

        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(fileService.saveFile(user, mock)).thenReturn("/file");

        Post post = postService.save("testuser", postDTO);
        assertNotNull(post);
        assertEquals("test post", post.getText());
    }

    @Test
    public void testSaveWithGoodSponsor() throws Status436SponsorNotFoundException, Status443DidNotReceivePictureException, Status446MarksBadRequestException, Status441FileIsNullException, Status437UserNotFoundException {
        User user = new User();
        user.setUsername("testuser");
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile mock = mock(MultipartFile.class);
        multipartFiles.add(mock);
        PostDTO postDTO = PostDTO.builder().text("test post").sponsorId(1L).multipartFiles(multipartFiles).build();

        when(userRepository.findUserById(1L)).thenReturn(new User());
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(fileService.saveFile(user, mock)).thenReturn("/file");

        Post post = postService.save("testuser", postDTO);
        assertNotNull(post);
        assertEquals("test post", post.getText());
        verify(sponsoredPostRepository).save(any());
    }

    @Test
    public void testSaveThrows436() {
        User user = new User();
        user.setUsername("testuser");
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile mock = mock(MultipartFile.class);
        multipartFiles.add(mock);
        PostDTO postDTO = PostDTO.builder().text("test post").sponsorId(1L).multipartFiles(multipartFiles).build();

        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(userRepository.findUserById(1L)).thenReturn(null);

        assertThrows(Status436SponsorNotFoundException.class,
                () -> postService.save("testuser", postDTO));
    }

    @Test
    public void testSaveThrows443() {
        User user = new User();
        user.setUsername("testuser");
        PostDTO postDTO = PostDTO.builder().text("test post").multipartFiles(new ArrayList<>()).build();


        when(userRepository.findUserByUsername("testuser")).thenReturn(user);

        assertThrows(Status443DidNotReceivePictureException.class,
                () -> postService.save("testuser", postDTO));
    }

    @Test
    public void testFindPostById() throws Status435PostNotFoundException {
        Post post = new Post();
        post.setText("test post");

        when(postRepository.findPostById(1L)).thenReturn(post);

        Post foundPost = postService.findPostById(1L);
        assertEquals(foundPost, post);
    }

    @Test
    public void testFindPostByIdThrows435() {
        when(postRepository.findPostById(1L)).thenReturn(null);

        assertThrows(Status435PostNotFoundException.class, () -> postService.findPostById(1L));
    }

    @Test
    public void testEditPost() throws Status435PostNotFoundException, Status441FileIsNullException, Status433NotYourPostException {
        User user = new User();
        user.setUsername("testuser");

        Post post = new Post();
        post.setText("test post");
        post.setAuthor(user);

        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile file = mock(MultipartFile.class);
        multipartFiles.add(file);
        PostDTO postDTO = PostDTO.builder().text("edited test post").multipartFiles(multipartFiles).build();

        when(postRepository.findPostById(1L)).thenReturn(post);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(fileService.saveFile(user, file)).thenReturn("/picture");

        Post editedPost = postService.editPost("testuser", postDTO, 1L);
        assertNotNull(editedPost);
        assertEquals("edited test post", editedPost.getText());
        assertEquals(user, editedPost.getAuthor());
        assertEquals("/picture", editedPost.getPictures().get(0).getPath());
    }

    @Test
    public void testEditPostThrowsStatus433() {
        User user = new User();
        user.setUsername("testuser");

        Post post = new Post();
        post.setText("test post");
        post.setAuthor(new User());

        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile file = mock(MultipartFile.class);
        multipartFiles.add(file);
        PostDTO postDTO = PostDTO.builder().text("test post").multipartFiles(multipartFiles).build();


        when(postRepository.findPostById(1L)).thenReturn(post);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);


        assertThrows(Status433NotYourPostException.class,
                () -> postService.editPost("testuser", postDTO, 1L));
    }

    @Test
    public void testEditUserThrows435() {
        when(postRepository.findPostById(1L)).thenReturn(null);
        assertThrows(Status435PostNotFoundException.class,
                () -> postService.editPost("test", PostDTO.builder().build(), 1L));
    }

    @Test
    public void testDeletePost() throws Status435PostNotFoundException, Status433NotYourPostException {
        User user = new User();
        Post post = new Post();
        post.setAuthor(user);

        when(postRepository.findPostById(1L)).thenReturn(post);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(sponsoredPostRepository.existsByPost(post)).thenReturn(false);

        postService.deletePost("testuser", 1L);
        verify(postRepository).deleteById(1L);
    }

    @Test
    public void testDeletePostThrows435() {
        when(postRepository.findPostById(1L)).thenReturn(null);

        assertThrows(Status435PostNotFoundException.class,
                () -> postService.deletePost("testuser", 1L));
    }

    @Test
    public void testDeletePostThrows433() {
        User user = new User();
        Post post = new Post();
        post.setAuthor(user);

        when(postRepository.findPostById(1L)).thenReturn(post);
        when(userRepository.findUserByUsername("testuser")).thenReturn(new User());

        assertThrows(Status433NotYourPostException.class,
                () -> postService.deletePost("testuser", 1L));
    }

    @Test
    public void testDeleteSponsoredPost() throws Status435PostNotFoundException, Status433NotYourPostException {
        User user = new User();
        Post post = new Post();
        post.setAuthor(user);

        when(postRepository.findPostById(1L)).thenReturn(post);
        when(userRepository.findUserByUsername("testuser")).thenReturn(user);
        when(sponsoredPostRepository.existsByPost(post)).thenReturn(true);

        postService.deletePost("testuser", 1L);
        verify(sponsoredPostRepository).deleteByPost(post);
    }

    @Test
    public void testBanPost() {
        postService.banPost(1L);
        verify(postRepository).deleteById(1L);
    }

    @Test
    public void testAddMarks() throws Exception {
        String username = "test_user";
        Picture picture = new Picture();
        Post post = new Post();
        User author = new User();
        author.setUsername(username);
        post.setAuthor(author);
        picture.setPost(post);
        MarkDTO markDTO = MarkDTO.builder().height(200).width(100).photoId(1L).username(username).build();
        List<MarkDTO> markDTOs = new ArrayList<>();
        markDTOs.add(markDTO);
        when(pictureRepository.findById(anyLong())).thenReturn(Optional.of(picture));
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        Post result = postService.addMarks(username, markDTOs);

        assertEquals(post, result);
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(markRepository, times(1)).save(any());
        verify(notificationService, times(1)).sendNotification(any());
    }

    @Test
    public void testAddMarksThrows449() {
        String username = "test_user";
        MarkDTO markDTO = MarkDTO.builder().photoId(1L).build();
        List<MarkDTO> markDTOs = new ArrayList<>();
        markDTOs.add(markDTO);
        when(pictureRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(Status449PictureNotFoundException.class,
                () -> postService.addMarks(username, markDTOs));
    }

    @Test
    public void testAddMarksThrows433() {
        // Arrange
        String username = "test_user";
        Picture picture = new Picture();
        Post post = new Post();
        User otherUser = new User();
        otherUser.setUsername("otherUser");
        post.setAuthor(otherUser);
        picture.setPost(post);
        MarkDTO markDTO = MarkDTO.builder().photoId(1L).username(username).build();
        List<MarkDTO> markDTOs = new ArrayList<>();
        markDTOs.add(markDTO);
        when(pictureRepository.findById(anyLong())).thenReturn(Optional.of(picture));

        assertThrows(Status433NotYourPostException.class, () -> postService.addMarks(username, markDTOs));
    }

    @Test
    public void testAddMarksThrows437() {
        String username = "test_user";
        Picture picture = new Picture();
        Post post = new Post();
        User author = new User();
        author.setUsername(username);
        post.setAuthor(author);
        picture.setPost(post);
        MarkDTO markDTO = MarkDTO.builder().photoId(1L).username("invalid_user").build();
        List<MarkDTO> markDTOs = new ArrayList<>();
        markDTOs.add(markDTO);
        when(pictureRepository.findById(anyLong())).thenReturn(Optional.of(picture));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        assertThrows(Status437UserNotFoundException.class, () -> postService.addMarks(username, markDTOs));
    }

    @Test
    public void testAddMarksWithEmptyList() throws Exception {
        String username = "test_user";
        List<MarkDTO> markDTOs = new ArrayList<>();

        Post result = postService.addMarks(username, markDTOs);

        assertNull(result);
        verify(pictureRepository, times(0)).findById(anyLong());
        verify(userRepository, times(0)).existsByUsername(anyString());
        verify(markRepository, times(0)).save(any());
        verify(notificationService, times(0)).sendNotification(any());
    }

    @Test
    void testGetRecommendations() {
        // Given
        String username = "user1";
        User user = new User();
        user.setUsername(username);
        Hashtag hashtag = new Hashtag();
        hashtag.setContent("hashtag");
        Post post1 = new Post();
        post1.setHashtags(List.of(hashtag));
        Post post2 = new Post();
        post2.setHashtags(List.of(hashtag));
        Like like = new Like(post1, user);
        post1.setLikesList(List.of(like));
        post2.setLikesList(new ArrayList<>());
        when(userRepository.findUserByUsername(username)).thenReturn(user);
        when(likeRepository.findAllByWhoLikes(user)).thenReturn(Collections.singletonList(like));
        when(hashtagService.getPostsByHashtags(List.of(hashtag.getContent()))).thenReturn(List.of(post1, post2));

        // When
        Set<Post> result = postService.getRecommendations(username);

        // Then
        assertThat(result).containsOnly(post1);
    }

    @Test
    void testGetRecommendationsWithEmptyResult() {
        // Given
        String username = "user2";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findUserByUsername(username)).thenReturn(user);
        when(likeRepository.findAllByWhoLikes(user)).thenReturn(Collections.emptyList());

        // When
        Set<Post> result = postService.getRecommendations(username);

        // Then
        assertThat(result).isEmpty();
    }
}