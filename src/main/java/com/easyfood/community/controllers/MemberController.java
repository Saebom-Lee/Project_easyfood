package com.easyfood.community.controllers;

import com.easyfood.community.entities.member.UserEntity;
import com.easyfood.community.enums.CommonResult;
import com.easyfood.community.interfaces.IResult;
import com.easyfood.community.services.MemberService;
import com.easyfood.community.utils.CryptoUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 로그인 화면
    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin(ModelAndView modelAndView) {
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    // 로그인
    @RequestMapping(value = "userLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserLogin(HttpSession session,
                                UserEntity user) {
        user.setName(null)
                .setContact(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setCreatedAt(null)
                .setEmailAuth(false);
        IResult result = this.memberService.loginUser(user);
        if (result == CommonResult.SUCCESS) {
            session.setAttribute(UserEntity.ATTRIBUTE_NAME, user);
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    // 로그아웃
    @RequestMapping(value = "userLogout", method = RequestMethod.GET)
    public ModelAndView getUserLogout(ModelAndView modelAndView,
                                      HttpSession session) {
        session.removeAttribute(UserEntity.ATTRIBUTE_NAME);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "userEmail", method = RequestMethod.GET)
    public ModelAndView getUserEmail(ModelAndView modelAndView) {
        modelAndView.setViewName("member/userEmail");
        return modelAndView;
    }

    // 회원가입 시 이메일 중복 여부
    @RequestMapping(value = "userEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserEmail(@RequestParam(value = "email") String email) {
        JSONObject responseJson = new JSONObject();
        IResult result = this.memberService.checkEmail(email);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.GET)
    public ModelAndView getUserRegister(ModelAndView modelAndView) {

        modelAndView.setViewName("member/userRegister");
        return modelAndView;
    }

    // 회원가입
    // policyMarketing(마케팅 정보 활용 동의)은 선택사항
    @RequestMapping(value = "userRegister", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserRegister(@RequestParam(value = "policyMarketing", required = true) boolean policyMarketing,
                                   @RequestParam(value = "name") String name,
                                   UserEntity user) {
        user.setCreatedAt(new Date())
                .setEmailAuth(false)
                .setPolicyPrivacyAt(new Date())
                .setPolicyTermsAt(new Date())
                .setPolicyMarketingAt(policyMarketing ? new Date() : null)
                .setName(name);
        IResult result;
        result = this.memberService.createUser(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userSecession", method = RequestMethod.GET)
    public ModelAndView getUserSecession(ModelAndView modelAndView) {

        modelAndView.setViewName("member/userSecession");
        return modelAndView;
    }

    // 회원탈퇴
    @RequestMapping(value = "userSecession", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserSecession(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                    @RequestParam(value = "password") String password) {

        JSONObject responseJson = new JSONObject();
        // 비밀번호 일치 여부
        if (!CryptoUtils.hashSha512(password).equals(user.getPassword())) {
            responseJson.put(IResult.ATTRIBUTE_NAME, "warn");
            return responseJson.toString();
        }
        IResult result = this.memberService.secessionUser(user.getEmail());
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }
}
