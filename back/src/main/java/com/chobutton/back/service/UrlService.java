package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UrlService {

    Optional<List<UrlDTO.Find>> findAll();

    // originUrl로 접속시 DB에 해당하는 데이터의 PK를 불러와 인코딩을 하기 위한 기능
    Optional<List<UrlDTO.Find>> findAllByUser_Id(int userId);

    // user별 등록한 URL을 조회하기 위한 기능
    Optional<UrlDTO.FindForEncoding> findByOriginUrl(String originUrl);



}
