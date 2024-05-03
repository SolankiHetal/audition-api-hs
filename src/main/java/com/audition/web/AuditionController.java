package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuditionController {

    private final AuditionService auditionService;

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam("userId") final Integer userId) {
        return auditionService.getPosts(userId);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostsById(@PathVariable("id") final Integer postId) {
        return auditionService.getPostById(postId);
    }

    @RequestMapping(value = "posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostWithComments(@PathVariable("id") final Integer postId) {
        return auditionService.getPostWithComments(postId);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Comment> getComments(@RequestParam("postId") final Integer postId) {
        return auditionService.getCommentsByPostId(postId);
    }

}
