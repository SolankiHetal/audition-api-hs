package com.audition;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource(properties = "downstream.baseUrl=https://jsonplaceholder.typicode.com")
class AuditionApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPosts() throws Exception {
        Integer userId = 2;
        mockMvc.perform(get("/posts?userId={userId}", userId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].userId").value(userId))
            .andExpect(jsonPath("$[0].id").value(11))
            .andExpect(jsonPath("$[0].title").value("et ea vero quia laudantium autem"));
    }

    @Test
    void testGetPostsInvalidInput() throws Exception {
        String userId = "abc";
        mockMvc.perform(get("/posts?userId={userId}", userId))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Failed to convert 'userId' with value: 'abc'"))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    void testGetPostsWithId() throws Exception {
        Integer postId = 6;
        mockMvc.perform(get("/posts/{id}", postId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.id").value(postId))
            .andExpect(jsonPath("$.title").value("dolorem eum magni eos aperiam quia"));

    }

    @Test
    void testGetPostsWithComments() throws Exception {
        String postId = "5";
        mockMvc.perform(get("/posts/{id}/comments", postId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.id").value(postId))
            .andExpect(jsonPath("$.title").value("nesciunt quas odio"))
            .andExpect(jsonPath("$.comments[0].postId").value(postId))
            .andExpect(jsonPath("$.comments[0].id").value(21))
            .andExpect(jsonPath("$.comments[0].name").value("aliquid rerum mollitia qui a consectetur eum sed"))
            .andExpect(jsonPath("$.comments[0].email").value("Noemie@marques.me"))
            .andExpect(jsonPath("$.comments[1].postId").value(postId))
            .andExpect(jsonPath("$.comments[1].id").value(22))
            .andExpect(jsonPath("$.comments[1].name").value("porro repellendus aut tempore quis hic"))
            .andExpect(jsonPath("$.comments[1].email").value("Khalil@emile.co.uk"));
    }

    @Test
    void testGetComments() throws Exception {
        Integer postId = 2;
        mockMvc.perform(get("/comments?postId={postId}", postId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].postId").value(postId))
            .andExpect(jsonPath("$[0].id").value(6))
            .andExpect(jsonPath("$[0].email").value("Presley.Mueller@myrl.com"))
            .andExpect(jsonPath("$[1].postId").value(postId))
            .andExpect(jsonPath("$[1].id").value(7))
            .andExpect(jsonPath("$[1].email").value("Dallas@ole.me"));
    }

}
