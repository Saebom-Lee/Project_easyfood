package com.easyfood.community.services;

import com.easyfood.community.dtos.recipe.ArticleSearchDto;
import com.easyfood.community.dtos.recipe.CommentSearchDto;
import com.easyfood.community.entities.recipe.ArticleEntity;
import com.easyfood.community.entities.recipe.CommentEntity;
import com.easyfood.community.entities.recipe.ImageEntity;
import com.easyfood.community.enums.CommonResult;
import com.easyfood.community.interfaces.IResult;
import com.easyfood.community.mappers.IRecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RecipeService {

    private final IRecipeMapper recipeMapper;

    @Autowired
    public RecipeService(IRecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }

    public ImageEntity getImage(int index) {
        return this.recipeMapper.selectImageByIndex(index);
    }

    public IResult uploadImage(ImageEntity image) {
        return this.recipeMapper.insertImage(image) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public ArticleSearchDto[] searchArticles(int lastArticleIndex) {
        return this.recipeMapper.selectArticlesForSearch(lastArticleIndex);
    }

    public ArticleSearchDto[] searchByContent(String keyword, int lastArticleIndex) {
        return this.recipeMapper.selectByContent(keyword, lastArticleIndex);
    }

    public CommentSearchDto[] searchComments() {
        return this.recipeMapper.selectCommentsForSearch();
    }

    public ArticleEntity getArticle(int index) {
        return this.recipeMapper.selectArticleByIndex(index);
    }

    public CommentEntity getComment(int commentId) {
        return this.recipeMapper.selectCommentByIndex(commentId);
    }

    public IResult putArticle(ArticleEntity article) {
        return this.recipeMapper.insertArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult modifyArticle(ArticleEntity article) {
        ArticleEntity oldArticle = this.recipeMapper.selectArticleByIndex(article.getIndex());
        if (oldArticle == null) {
            return CommonResult.FAILURE;
        }
        if (!article.getUserEmail().equals(oldArticle.getUserEmail())) {
            return CommonResult.FAILURE;
        }
        if (article.getCoverImage() == null) {
            article.setCoverImage(oldArticle.getCoverImage())
                    .setCoverImageMime(oldArticle.getCoverImageMime());
        }
        article.setIndex(oldArticle.getIndex())
                .setUserEmail(oldArticle.getUserEmail())
                .setCreatedAt(oldArticle.getCreatedAt());
        return this.recipeMapper.updateArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult modifyComment(CommentEntity comment) {
        CommentEntity oldComment = this.recipeMapper.selectCommentByIndex(comment.getIndex());
        if (oldComment == null) {
            return CommonResult.FAILURE;
        }
        comment.setIndex(oldComment.getIndex())
                .setUserEmail(oldComment.getUserEmail())
                .setCreatedAt(oldComment.getCreatedAt());
        return this.recipeMapper.updateComment(comment) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult deleteArticle(int index) {
        return this.recipeMapper.deleteArticle(index) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult deleteComment(int index) {
        return this.recipeMapper.deleteComment(index) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult putComment(CommentEntity comment){
        return this.recipeMapper.insertComment(comment) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
}
