package com.example.forum_management_system.controllers.mvc;

import com.example.forum_management_system.models.Post;
import com.example.forum_management_system.models.PostFilterOptions;
import com.example.forum_management_system.models.User;
import com.example.forum_management_system.models.postDtos.PostDtoHome;
import com.example.forum_management_system.models.postDtos.PostFilterOptionsDto;
import com.example.forum_management_system.services.CommentService;
import com.example.forum_management_system.services.PostService;
import com.example.forum_management_system.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/forum")
public class ForumMvcController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    @Autowired
    public ForumMvcController(PostService service, CommentService commentService, UserService userService) {
        this.postService = service;
        this.commentService = commentService;
        this.userService = userService;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        if(session.getAttribute("currentUser") != null){
            Object currentUser = session.getAttribute("currentUser");
            User user = userService.getByName(currentUser.toString());
            return user.isAdmin();
        }
        return false;
    }

    @GetMapping
    public String showForumPage(@ModelAttribute("filterOptions") PostFilterOptionsDto filterOptionsDto, Model model){
        PostFilterOptions postFilterOptions = new PostFilterOptions(
                filterOptionsDto.getPostTitle(),
                filterOptionsDto.getPostAuthor(),
                filterOptionsDto.getSortPostsBy(),
                filterOptionsDto.getSortOrder());

        List<Post> posts = postService.getAllFromPostFilter(postFilterOptions);
        /*List<Post> posts = postService.get(null,null,null,null);*/
        int postsCount = postService.showPostsCount();
        int usersCount = userService.showUsersCount();
        model.addAttribute("posts", posts);
        model.addAttribute("postsCount", postsCount);
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("comments", commentService);
        model.addAttribute("postsFilter", postFilterOptions);
        return "Forum";
    }
}
