package com.makersacademy.acebook.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommentTest {

    private User user = new User("Username");
    private Comment comment = new Comment("This is a comment", 1L, user);

    @Test
    public void commentHasContent() {
        assertThat(comment.getContent(), containsString("This is a comment"));
    }
}
