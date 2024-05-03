package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditionService {

    private final AuditionIntegrationClient auditionIntegrationClient;


    public List<AuditionPost> getPosts(final Integer userId) {
        return auditionIntegrationClient.getPosts(userId);
    }

    public AuditionPost getPostById(final Integer postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    public AuditionPost getPostWithComments(final Integer postId) {
        return auditionIntegrationClient.getPostWithComments(postId);
    }

    public List<Comment> getCommentsByPostId(final Integer postId) {
        return auditionIntegrationClient.getCommentsByPostId(postId);
    }
}
