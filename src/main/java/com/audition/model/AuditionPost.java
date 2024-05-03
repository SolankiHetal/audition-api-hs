package com.audition.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<Comment> comments;
}
