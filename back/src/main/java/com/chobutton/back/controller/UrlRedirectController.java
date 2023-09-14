package com.chobutton.back.controller;

import com.chobutton.back.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;


@Controller
@AllArgsConstructor
public class UrlRedirectController {

    private UrlService urlService;

    @RequestMapping(value = "/shortnee/{shortenUrl}", method = RequestMethod.GET)
    public String redirectToOriginUrl(@PathVariable String shortenUrl){
        String originUrl = urlService.urlDecoding(shortenUrl);
            return "redirect:" + originUrl;
    }

}
