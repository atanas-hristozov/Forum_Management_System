package com.example.forum_management_system.helpers;

import com.example.forum_management_system.models.Comment;
import com.example.forum_management_system.models.CommentDto;
import com.example.forum_management_system.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final CommentService commentService;
    @Autowired
    public CommentMapper(CommentService commentService) {
        this.commentService = commentService;
    }
    public Comment fromDto(int id, CommentDto dto){
        Comment comment = fromDto(dto);
        comment.setId(id);
        Comment repositoryComment = commentService.get(id);
        comment.setAuthor(repositoryComment.getAuthor());
        return comment;
    }
    public Comment fromDto(CommentDto dto){
        Comment comment = new Comment();
        comment.setText(dto.getText());
        return comment;
    }
}
