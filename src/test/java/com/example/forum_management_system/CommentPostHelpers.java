package com.example.forum_management_system;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.User;

import java.sql.Timestamp;

public class CommentPostHelpers {

    public static Comment createComment(){
        User user = UserHelpers.createMockUser();
        /*User user = new User();
        user.setId(1);
        user.setUsername("SuperAdmin");
        user.setPassword("123456");
        user.setFirstName("SuperAdminFirstName");
        user.setLastName("SuperAdminLastName");
        user.setEmail("SuperAdminEmail");
        user.setAdmin(true);

        Post post = new Post();
        post.setId(1);
        post.setTitle("Title");
        post.setContent("Content");
        post.setLikes(1);
        post.setDislikes(1);
        post.setCreator(user);
        post.setTimestamp(Timestamp.valueOf("2018-07-14 10:12:34"));
        */
        Post post = CreateMockPost();
        Comment mockComment = new Comment();
        mockComment.setId(1);
        mockComment.setText("This is a test");
        mockComment.setPost_id(post);
        mockComment.setAuthor(user);
        return mockComment;
    }
    public static Post CreateMockPost(){
        User user = UserHelpers.createMockUser();
        Post post = new Post();
        post.setId(1);
        post.setTitle("Title");
        post.setContent("Content");
        post.setLikes(1);
        post.setDislikes(1);
        post.setCreator(user);
        post.setTimestamp(Timestamp.valueOf("2018-07-14 10:12:34"));
        return post;
    }
}
