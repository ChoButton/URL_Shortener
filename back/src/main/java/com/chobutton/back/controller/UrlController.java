package com.chobutton.back.controller;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/url")
public class UrlController {

    private UrlService urlService;

    // 관리자용 기능 ------------------------------------------------------------
    @RequestMapping(value = "/admin/all", method = RequestMethod.GET)
    public ResponseEntity<List<UrlDTO>> findAllUrlsForAdmin(){
        List<UrlDTO> urlList = urlService.findAll();
        return ResponseEntity.ok(urlList);
    }

    @RequestMapping(value = "/admin/all/{userEmail}", method = RequestMethod.GET)
    public ResponseEntity<List<UrlDTO>> findAllByUserEmailForAdmin(@PathVariable String userEmail){
        List<UrlDTO> urlList = urlService.findAllByUserEmail(userEmail);
        return ResponseEntity.ok(urlList);
    }

}
