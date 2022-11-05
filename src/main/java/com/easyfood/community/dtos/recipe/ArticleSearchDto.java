package com.easyfood.community.dtos.recipe;

import com.easyfood.community.entities.recipe.ArticleEntity;

public class ArticleSearchDto extends ArticleEntity {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public ArticleSearchDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public ArticleSearchDto setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
