package com.easyfood.community.controllers;

import com.easyfood.community.dtos.recipe.ArticleSearchDto;
import com.easyfood.community.dtos.recipe.CommentSearchDto;
import com.easyfood.community.entities.member.UserEntity;
import com.easyfood.community.entities.recipe.ArticleEntity;
import com.easyfood.community.entities.recipe.CommentEntity;
import com.easyfood.community.entities.recipe.ImageEntity;
import com.easyfood.community.enums.CommonResult;
import com.easyfood.community.interfaces.IResult;
import com.easyfood.community.services.MemberService;
import com.easyfood.community.services.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Controller(value = "com.easyfood.community.controllers.RecipeController")
@RequestMapping(value = "/recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final MemberService memberService;

    @Autowired
    public RecipeController(RecipeService recipeService, MemberService memberService) {
        this.recipeService = recipeService;
        this.memberService = memberService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("recipe/index");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postIndex(@RequestParam(value = "lastArticleId") int lastArticleId)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseJson = new JSONObject();
        ArticleSearchDto[] articles = this.recipeService.searchArticles(lastArticleId);
        for (ArticleSearchDto article : articles) {
            article.setContent(article.getContent().replaceAll("<[^>]*>", "")
                    .replaceAll("&[^;]*;", ""));
        }
        responseJson.put(ArticleEntity.ATTRIBUTE_NAME_PLURAL, new JSONArray(objectMapper.writeValueAsString(articles)));
        return responseJson.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchIndex(@RequestParam(value = "lastArticleId") int lastArticleId,
            @RequestParam(value = "keyword") String keyword) throws JsonProcessingException {
        if(keyword == null){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseJson = new JSONObject();
        ArticleSearchDto[] articles = this.recipeService.searchByContent(keyword, lastArticleId);
        for (ArticleSearchDto article : articles) {
            article.setContent(article.getContent().replaceAll("<[^>]*>", "")
                    .replaceAll("&[^;]*;", ""));
        }
        responseJson.put(ArticleEntity.ATTRIBUTE_NAME_PLURAL, new JSONArray(objectMapper.writeValueAsString(articles)));
        return responseJson.toString();
    }

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                 ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        modelAndView.setViewName("recipe/write");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
                            @RequestParam(value = "coverImageFile") MultipartFile coverImageFile,
                            ArticleEntity article) throws IOException {
        article.setIndex(-1)
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setCoverImage(coverImageFile.getBytes())
                .setCoverImageMime(coverImageFile.getContentType());
        IResult result = this.recipeService.putArticle(article);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("id", article.getIndex());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "cover-image/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCoverImage(@PathVariable(value = "id") int id) {
        ArticleEntity article = this.recipeService.getArticle(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String[] mimeArray = article.getCoverImageMime().split("/"); // "image/png"
        String mimeType = mimeArray[0]; // "image"
        String mimeSubType = mimeArray[1]; // "png"
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(article.getCoverImage().length);
        headers.setContentType(new MediaType(mimeType, mimeSubType, StandardCharsets.UTF_8));
        return new ResponseEntity<>(article.getCoverImage(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "image/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable(value = "id") int id) {
        ImageEntity image = this.recipeService.getImage(id);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        String[] mimeArray = image.getMime().split("/");
        String mimeType = image.getMime().split("/")[0];
        String mimeSubType = image.getMime().split("/")[1];
        headers.setContentLength(image.getData().length);
        headers.setContentType(new MediaType(mimeType, mimeSubType, StandardCharsets.UTF_8));
        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postImage(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
                            @RequestParam(value = "upload") MultipartFile upload) throws IOException {
        ImageEntity image = ImageEntity.build()
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setName(upload.getOriginalFilename())
                .setMime(upload.getContentType())
                .setData(upload.getBytes());
        IResult result = this.recipeService.uploadImage(image);
        JSONObject responseJson = new JSONObject();
        if (result == CommonResult.SUCCESS) {
            responseJson.put("url", String.format("http://localhost:8080/recipe/image/%d", image.getIndex()));
        } else {
            JSONObject errorJson = new JSONObject();
            errorJson.put("message", "이미지 업로드에 실패하였습니다. 잠시 후 다시 시도해 주세요.");
            responseJson.put("error", errorJson);
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.GET)
    public ModelAndView getRead(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                @PathVariable(value = "id") int id,
                                ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        modelAndView.setViewName("recipe/read");
        return modelAndView;
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRead(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                           @PathVariable(value = "id") int id, HttpServletResponse response) throws JsonProcessingException {
        ArticleEntity article = this.recipeService.getArticle(id);
        if (article == null) {
            response.setStatus(404);
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseJson = new JSONObject(objectMapper.writeValueAsString(article));
        UserEntity articleUser = this.memberService.getUser(article.getUserEmail());
        responseJson.put("writeName", user.getName());
        responseJson.put("userName", articleUser.getName());
        responseJson.put("mine", user != null && user.equals(articleUser));
        return responseJson.toString();
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteRead(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                             @PathVariable(value = "id") int id) {
        JSONObject responseJson = new JSONObject();
        ArticleEntity article = this.recipeService.getArticle(id);
        if (article == null) {
            responseJson.put(IResult.ATTRIBUTE_NAME, CommonResult.FAILURE);
            return responseJson.toString();
        }
        if (user == null || !user.getEmail().equals(article.getUserEmail())) {
            responseJson.put(IResult.ATTRIBUTE_NAME, "warn");
            return responseJson.toString();
        }
        IResult result = this.recipeService.deleteArticle(id);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "comment/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postComment(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                              CommentEntity comment,
                              @PathVariable(value = "id") int id) {
        comment.setIndex(-1)
                .setBoardIndex(id)
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date());
        JSONObject responseJson = new JSONObject();
        IResult result = this.recipeService.putComment(comment);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("commentId", comment.getIndex());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "comment/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchComment(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                               @PathVariable(value = "id") int id, HttpServletResponse response,
                               @RequestParam(value = "commentId") int commentId
    ) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseJson = new JSONObject();
        CommentSearchDto[] comments = this.recipeService.searchComments();
        for (CommentSearchDto comment : comments) {
            comment.setContent(comment.getContent().replaceAll("<[^>]*>", "")
                    .replaceAll("&[^;]*;", ""));
        }

        responseJson.put(CommentEntity.ATTRIBUTE_NAME_PLURAL, new JSONArray(objectMapper.writeValueAsString(comments)));
        responseJson.put("writeName", user.getName());
        return responseJson.toString();
    }

    @RequestMapping(value = "comment", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteComment(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                @RequestParam(value = "commentId") int commentId
    ) {
        JSONObject responseJson = new JSONObject();
        CommentEntity comment = this.recipeService.getComment(commentId);
        if (comment == null) {
            responseJson.put(IResult.ATTRIBUTE_NAME, CommonResult.FAILURE);
            return responseJson.toString();
        }
        IResult result = this.recipeService.deleteComment(commentId);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "comment/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchComment(@RequestParam(value = "commentId") int commentId,
                               @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                               @PathVariable(value = "id") int id, CommentEntity comment){
        comment.setIndex(commentId)
                .setBoardIndex(id)
                .setUserEmail(user.getEmail());
        IResult result = this.recipeService.modifyComment(comment);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView getModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                  @PathVariable(value = "id") int id,
                                  ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        modelAndView.setViewName("recipe/modify");
        return modelAndView;
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                              @PathVariable(value = "id") int id, HttpServletResponse response) throws JsonProcessingException {
        ArticleEntity article = this.recipeService.getArticle(id);
        if (article == null) {
            response.setStatus(404);
            return null;
        }
        if (user == null || !user.getEmail().equals(article.getUserEmail())) {
            response.setStatus(403);
            return null;
        }
        article.setCoverImage(null)
                .setCoverImageMime(null);
        return new ObjectMapper().writeValueAsString(article);
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
                             @PathVariable(value = "id") int id,
                             @RequestParam(value = "coverImageFile", required = false) MultipartFile coverImageFile,
                             ArticleEntity article) throws IOException {
        article.setIndex(id)
                .setUserEmail(user.getEmail())
                .setCoverImage(coverImageFile == null ? null : coverImageFile.getBytes())
                .setCoverImageMime(coverImageFile == null ? null : coverImageFile.getContentType());
        IResult result = this.recipeService.modifyArticle(article);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

}
