package com.easyfood.community.entities.recipe;

import java.util.Date;
import java.util.Objects;

public class CommentEntity {

    public static final String ATTRIBUTE_NAME = "recipeComment";
    public static final String ATTRIBUTE_NAME_PLURAL = "recipeComments";
    public static final String ATTRIBUTE_COMMENTS_PLURAL = "comments";

    public static final CommentEntity build() {
        return new CommentEntity();
    }

    private int index;
    private int boardIndex;
    private int commentIndex;
    private int commentOrder;
    private int commentDepth;
    private String userEmail;
    private Date createdAt = new Date();
    private String content;

    public CommentEntity(){

    }

    public CommentEntity(int index, int boardIndex, int commentIndex, int commentOrder, int commentDepth, String userEmail, Date createdAt, String content) {
        this.index = index;
        this.boardIndex = boardIndex;
        this.commentIndex = commentIndex;
        this.commentOrder = commentOrder;
        this.commentDepth = commentDepth;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public CommentEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public CommentEntity setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
        return this;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public CommentEntity setCommentIndex(int commentIndex) {
        this.commentIndex = commentIndex;
        return this;
    }

    public int getCommentOrder() {
        return commentOrder;
    }

    public CommentEntity setCommentOrder(int commentOrder) {
        this.commentOrder = commentOrder;
        return this;
    }

    public int getCommentDepth() {
        return commentDepth;
    }

    public CommentEntity setCommentDepth(int commentDepth) {
        this.commentDepth = commentDepth;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public CommentEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public CommentEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
