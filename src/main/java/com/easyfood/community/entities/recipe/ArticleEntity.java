package com.easyfood.community.entities.recipe;

import java.util.Date;
import java.util.Objects;

public class ArticleEntity {
    public static final String ATTRIBUTE_NAME = "recipeArticle";
    public static final String ATTRIBUTE_NAME_PLURAL = "recipeArticles";

    public static ArticleEntity build() {
        return new ArticleEntity();
    }

    private int index;
    private String userEmail;
    private Date createdAt;
    private int capacity;
    private String title;
    private String content;
    private byte[] coverImage;
    private String coverImageMime;

    public ArticleEntity() {

    }

    public ArticleEntity(int index, String userEmail, Date createdAt, int capacity, String title, String content, byte[] coverImage, String coverImageMime) {
        this.index = index;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.capacity = capacity;
        this.title = title;
        this.content = content;
        this.coverImage = coverImage;
        this.coverImageMime = coverImageMime;
    }

    public int getIndex() {
        return index;
    }

    public ArticleEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ArticleEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ArticleEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArticleEntity setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public ArticleEntity setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public String getCoverImageMime() {
        return coverImageMime;
    }

    public ArticleEntity setCoverImageMime(String coverImageMime) {
        this.coverImageMime = coverImageMime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleEntity that = (ArticleEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
