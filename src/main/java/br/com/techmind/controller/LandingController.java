package br.com.techmind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    @GetMapping({"/landing", "/landing/"})
    public String landingRedirect() {
        return "forward:/landing/index.html";
    }
}
