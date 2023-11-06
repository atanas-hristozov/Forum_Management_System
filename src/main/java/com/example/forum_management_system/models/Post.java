package com.example.forum_management_system.models;

import com.example.forum_management_system.exceptions.TextLengthException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    public static final int TITLE_MIN_LENGTH = 16;
    public static final int TITLE_MAX_LENGTH = 64;
    public static final int CONTENT_MIN_LENGTH = 32;
    public static final int CONTENT_MAX_LENGTH = 8192;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;
    @Column(name = "title")
    @Size(min = 16, max = 64, message = "Title length should be between 16 and 64")
    private String title;
    @Column(name = "content")
    @Size(min = 32, max = 8192, message = "The content must be between 32 symbols and 8192 symbols.")
    private String content;

    @Column(name = "timestamp_created")
    private Timestamp timestamp;
    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL)
    private List<Comment> comments;
    @ManyToMany(mappedBy = "likedPosts", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
    private Set<User> likedByUsers;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "posts_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        checkTitleLength(title);
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        checkContentLength(content);
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getLikedByUsers() {
        if(likedByUsers==null)
            return new HashSet<>();
        return likedByUsers;
    }

    public void setNewLike(User userLike) {
        if (likedByUsers==null || likedByUsers.isEmpty()) {
            this.likedByUsers = new HashSet<>();
        }
        likedByUsers.add(userLike);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private void checkTitleLength(String text) {
        if (text.length() < TITLE_MIN_LENGTH || text.length() > TITLE_MAX_LENGTH)
            throw new TextLengthException(TITLE_MIN_LENGTH, TITLE_MAX_LENGTH);
    }
    private void checkContentLength(String text) {
        if (text.length() < CONTENT_MIN_LENGTH || text.length() > CONTENT_MAX_LENGTH)
            throw new TextLengthException(CONTENT_MIN_LENGTH, CONTENT_MAX_LENGTH);
    }
}
