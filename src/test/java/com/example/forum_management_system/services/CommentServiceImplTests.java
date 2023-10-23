package com.example.forum_management_system.services;

import com.example.forum_management_system.CommentPostHelpers;
import com.example.forum_management_system.UserHelpers;
import com.example.forum_management_system.exceptions.AuthorizationException;
import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;




import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    /*@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    */
    @Test
    public void testGetComment() {
        Comment comment = CommentPostHelpers.createComment();
        when(commentRepository.get(1)).thenReturn(comment);
        Comment result = commentService.get(1);
        assertEquals(comment, result);
    }

    @Test
    public void testCreateComment() {
        User user = UserHelpers.createMockUser();
        Post post = CommentPostHelpers.CreateMockPost();
        Comment comment = CommentPostHelpers.createComment();
        commentService.create(comment, post, user);
        verify(commentRepository).create(comment);
    }
/*
    @Test
    public void testUpdateCommentByAuthor() {
        // Create a mock user
        User user = UserHelpers.createMockUser();

        // Create a mock post
        Post post = CommentPostHelpers.CreateMockPost();

        // Create a mock comment
        Comment comment = CommentPostHelpers.createComment();

        // Set the author of the comment to be the same as the user
        comment.setAuthor(user);

        // Mock the behavior of the CommentRepository
        when(commentRepository.get(1)).thenReturn(comment);

        // Call the update method
        commentService.update(comment, post, user);

        // Verify that the update method of the commentRepository was called with the correct comment
        verify(commentRepository).update(comment);
    }

    @Test(expected = AuthorizationException.class)
    public void testUpdateCommentByNonAuthor() {
        User user = UserHelpers.createMockUser();
        Post post = CommentPostHelpers.CreateMockPost();
        Comment comment = CommentPostHelpers.createComment();

        when(commentRepository.get(1)).thenReturn(comment);

        commentService.update(comment, post, user);

    }*/

    @Test
    public void testDeleteCommentByAuthor() {
        User user = UserHelpers.createMockUser();
        Comment comment = CommentPostHelpers.createComment();
        when(commentRepository.get(1)).thenReturn(comment);

        commentService.delete(1, user);
        verify(commentRepository).delete(1);
    }

    @Test
    public void testDeleteCommentByAdmin() {
        User user = UserHelpers.createMockUser();
        Post post = CommentPostHelpers.CreateMockPost();
        Comment comment = CommentPostHelpers.createComment();
        when(commentRepository.get(1)).thenReturn(comment);
        commentService.delete(1, user);
        verify(commentRepository).delete(1);
    }
/*
    @Test(expected = AuthorizationException.class)
    public void testDeleteCommentByNonAuthorOrNonAdmin() {
        Comment comment = new Comment();
        comment.setAuthor(new User());
        User user = new User();
        user.setId(2);
        when(commentRepository.get(1)).thenReturn(comment);

        commentService.delete(1, user);
    }
*/
    @Test
    public void testGetAllCommentsFromPost() {
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.getAllCommentsFromPost(1)).thenReturn(comments);

        List<Comment> result = commentService.getAllCommentsFromPost(1);
        assertEquals(comments, result);
    }
}
