package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AuditionIntegrationClient {

    private final RestTemplate restTemplate;

    private static final String ERROR = "Unknown Error message";

    @Value("${downstream.baseUrl}")
    private String baseUrl;


    public List<AuditionPost> getPosts(final int userId) {
        try {
            final AuditionPost[] response = restTemplate.getForObject(baseUrl + "/posts?userId={userId}",
                AuditionPost[].class, userId);
            return Arrays.asList(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with userId " + userId, e.getStatusCode().value(), e);
            } else {
                throw new SystemException(ERROR, e.getStatusCode().value(), e);
            }
        }
    }

    public AuditionPost getPostById(final Integer id) {
        try {
            return restTemplate.getForObject(baseUrl + "/posts/{id}", AuditionPost.class, id);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, e.getStatusCode().value(), e);
            } else {
                throw new SystemException(ERROR, e.getStatusCode().value(), e);
            }
        }
    }

    public AuditionPost getPostWithComments(final Integer postId) {

        try {
            //get post by postId
            final AuditionPost post = getPostById(postId);

            final Comment[] comments = restTemplate.getForObject(baseUrl + "/posts/{postId}/comments", Comment[].class,
                postId);
            post.setComments(Arrays.asList(comments));

            return post;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + postId, e.getStatusCode().value(), e);
            } else {
                throw new SystemException(ERROR, e.getStatusCode().value(), e);
            }
        }
    }

    public List<Comment> getCommentsByPostId(final Integer postId) {
        try {
            final Comment[] response = restTemplate.getForObject(baseUrl + "/comments?postId={postId}", Comment[].class,
                postId);
            return Arrays.asList(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find comments with Post Id " + postId, e.getStatusCode().value(), e);
            } else {
                throw new SystemException(ERROR, e.getStatusCode().value(), e);
            }
        }
    }
}
