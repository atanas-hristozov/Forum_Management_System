package com.example.forum_management_system.models;

import com.example.forum_management_system.exceptions.TextLengthException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "comments")
public class Comment {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "text")
    private String text;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post_id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    public Comment() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        /*if (text.isEmpty()){
            throw new TextLengthException();
        }*/
        this.text = text;
    }

    public Post getPost_id() {
        return post_id;
    }

    public void setPost_id(Post post_id) {
        this.post_id = post_id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
