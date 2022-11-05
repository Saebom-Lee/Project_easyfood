package com.easyfood.community.dtos.recipe;

import com.easyfood.community.entities.recipe.CommentEntity;

public class CommentSearchDto extends CommentEntity {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public CommentSearchDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
