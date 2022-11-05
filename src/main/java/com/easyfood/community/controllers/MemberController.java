package com.easyfood.community.controllers;

import com.easyfood.community.entities.member.UserEntity;
import com.easyfood.community.enums.CommonResult;
import com.easyfood.community.interfaces.IResult;
import com.easyfood.community.services.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller(value = "com.easyfood.community.controllers.MemberController")
@RequestMapping(value = "/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin(ModelAndView modelAndView) {
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.POST, produces = "application/json")
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

    @RequestMapping(value = "userRegister", method = RequestMethod.GET)
    public ModelAndView getUserRegister(ModelAndView modelAndView) {

        modelAndView.setViewName("member/userRegister");
        return modelAndView;
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRegister(@RequestParam(value = "policyMarketing", required = true) boolean policyMarketing,
                                   UserEntity user) {
        user.setCreatedAt(new Date())
                .setEmailAuth(false)
                .setPolicyPrivacyAt(new Date())
                .setPolicyTermsAt(new Date());
        IResult result;
        result = this.memberService.createUser(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }



}
