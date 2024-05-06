package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AuditionIntegrationClientTest {

    @InjectMocks
    private AuditionIntegrationClient client;

    @Mock
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";


    @Test
    void testGetPosts_success() {
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        AuditionPost post1 = AuditionPost.builder()
            .userId(2)
            .id(11)
            .title("Title 1")
            .build();

        AuditionPost[] posts = {post1};

        when(restTemplate.getForObject(eq(BASE_URL + "/posts?userId={userId}"),
            eq(AuditionPost[].class),
            anyInt()
        )).thenReturn(posts);

        List<AuditionPost> result = client.getPosts(2);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals(11, result.get(0).getId());
    }


    @Test
    void testGetPosts_notFound() {
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        Mockito.when(restTemplate.getForObject(
            eq(BASE_URL + "/posts?userId={userId}"),
            eq(AuditionPost[].class),
            anyInt()
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        SystemException exception = assertThrows(SystemException.class, () -> client.getPosts(2));
        assertEquals("Cannot find a Post with userId 2", exception.getMessage());
    }

    @Test
    void testGetPosts_serverError() {
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        Mockito.when(restTemplate.getForObject(
            eq(BASE_URL + "/posts?userId={userId}"),
            eq(AuditionPost[].class),
            anyInt()
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        SystemException exception = assertThrows(SystemException.class, () -> client.getPosts(2));
        assertEquals("Unknown Error message", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
    }
}