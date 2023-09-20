package com.chobutton.back.controller;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/url")
public class UrlController {

    private UrlService urlService;

    // 관리자용 기능 -----------------------------------------------------------------------------------
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin/all", method = RequestMethod.GET)
    public ResponseEntity<List<UrlDTO>> findAllUrlsForAdmin(){
        List<UrlDTO> urlList = urlService.findAll();
        return ResponseEntity.ok(urlList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin/all/{userEmail}", method = RequestMethod.GET)
    public ResponseEntity<List<UrlDTO>> findAllByUserEmailForAdmin(@PathVariable String userEmail){
        List<UrlDTO> urlList = urlService.findAllByUserEmail(userEmail);
        return ResponseEntity.ok(urlList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin/update", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateUrlForAdmin(@RequestBody UrlDTO urlDTO){
        urlService.update(urlDTO);
        return ResponseEntity.ok("등록된 URL 수정이 완료되었습니다.");
    }

    // 사용자용 기능 -----------------------------------------------------------------------------------

    // URL등록후 바로 단축된 URL을 확인할수 있도록 body에 단축된 URL을 같이 리턴함
    // 비회원도 사용 가능
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ResponseEntity<String> createShortenUrl(@RequestBody UrlDTO urlDTO){
        String shortenUrl = urlService.save(urlDTO);
        return ResponseEntity.ok(shortenUrl);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user/all/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<UrlDTO>> findAllByUserIdForUser(@PathVariable int userId){
        List<UrlDTO> urlList = urlService.findAllByUserId(userId);
        return ResponseEntity.ok(urlList);
    }

    // 공용 기능 -------------------------------------------------------------------------------------
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable int id){
        urlService.deleteById(id);
        return ResponseEntity.ok("등록된 URL이 삭제되었습니다.");
    }
}
