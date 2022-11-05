package com.easyfood.community.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.easyfood.community.controllers.ConsultingController")
@RequestMapping(value = "/consulting")
public class ConsultingController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("consulting/index");
        return modelAndView;
    }
}
