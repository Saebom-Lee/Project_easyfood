package com.easyfood.community.mappers;

import com.easyfood.community.dtos.recipe.ArticleSearchDto;
import com.easyfood.community.dtos.recipe.CommentSearchDto;
import com.easyfood.community.entities.recipe.ArticleEntity;
import com.easyfood.community.entities.recipe.CommentEntity;
import com.easyfood.community.entities.recipe.ImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IRecipeMapper {

    int insertImage(ImageEntity image);

    int insertArticle(ArticleEntity article);

    int insertComment(CommentEntity comment);

    int selectCountTotal(ArticleEntity article);

    ArticleEntity selectArticleByIndex(@Param(value = "index") int index);

    CommentEntity selectCommentByIndex(@Param(value = "commentId") int commentId);

    ArticleSearchDto[] selectArticlesForSearch(@Param(value = "lastArticleIndex") int lastArticleIndex);

    ArticleSearchDto[] selectByContent(@Param(value = "keyword") String keyword, @Param(value = "lastArticleIndex") int lastArticleIndex);

    CommentSearchDto[] selectCommentsForSearch();

    ImageEntity selectImageByIndex(@Param(value = "index") int index);

    int updateArticle(ArticleEntity article);

    int updateComment(CommentEntity comment);

    int deleteArticle(@Param(value = "index") int index);

    int deleteComment(@Param(value = "index") int index);


}