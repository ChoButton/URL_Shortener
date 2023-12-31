package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UrlService {

    List<UrlDTO> findAll();

    // user별 등록한 URL을 조회하기 위한 기능
    List<UrlDTO> findAllByUserId(int userId);

    // 관리자의 유저별 등록 URL검색 기능을 위해 유저의 email을 통해 조회하는 기능
    List<UrlDTO> findAllByUserEmail(String email);

    // originUrl로 접속시 DB에 해당하는 데이터의 PK를 불러와 인코딩후 shortenUrl을 리턴하는 기능
    String urlEncoding(String originUrl);

    // shortenUrl이 전달될 경우 originUrl을 리턴해주는 기능
    String urlDecoding(String shortenUrl);

    public String save(UrlDTO urlDTO);

    public void update(UrlDTO urlDTO);

    public void deleteById(int id);
}
