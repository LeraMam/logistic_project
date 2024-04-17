package com.valeria.demo.controllers;

import com.valeria.demo.services.UserService;
/*import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ViewController {
    @GetMapping("/about")
    public ModelAndView aboutView(){
        String name = "About company";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageTitle", name);
        modelAndView.setViewName("about_company");
        return modelAndView;
    }
   @GetMapping("/tariffs")
    public ModelAndView tariffsView(){
        String name = "Point tariffs";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageTitle", name);
        modelAndView.setViewName("tariffs");
        return modelAndView;
    }
    @GetMapping("/account")
    public ModelAndView accountView(){
        String name = "Account";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageTitle", name);
        modelAndView.setViewName("account");
        return modelAndView;
    }
    @GetMapping("/search")
    public ModelAndView countView(){
        String name = "Search";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageTitle", name);
        modelAndView.setViewName("search");
        return modelAndView;
    }
    @GetMapping("/login")
    public ModelAndView loginView(){
        String name = "Login";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageTitle", name);
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @GetMapping("/register")
    public ModelAndView registerView(){
        String name = "Registration";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageTitle", name);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @GetMapping("/orders")
    public ModelAndView orderView(){
        String name = "Orders";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageTitle", name);
        modelAndView.setViewName("orders");
        return modelAndView;
    }
}
