package com.example.forum_management_system.models;

import java.util.Optional;

public class PostCommentFilterOptions {

    private Optional<String> sortPosts;
    private Optional<String> filterPosts;
    private Optional<String> filterComments;
    private Optional<String> sortComments;

    public PostCommentFilterOptions(String sortAllPosts,
                                    String filterAllPosts,
                                    String filterAllComments,
                                    String sortAllComments) {
        this.sortPosts = Optional.ofNullable(sortAllPosts);
        this.filterPosts = Optional.ofNullable(filterAllPosts);
        this.filterComments = Optional.ofNullable(filterAllComments);
        this.sortComments = Optional.ofNullable(sortAllComments);
    }

    public Optional<String> getSortAllPosts() {
        return sortPosts;
    }

    public Optional<String> getFilterPosts() {
        return filterPosts;
    }

    public Optional<String> getFilterComments() {
        return filterComments;
    }

    public Optional<String> getSortComments() {
        return sortComments;
    }
}
