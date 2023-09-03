package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UrlService {

    List<UrlDTO> findAll();

    // originUrl로 접속시 DB에 해당하는 데이터의 PK를 불러와 인코딩을 하기 위한 기능
    List<UrlDTO> findAllByUser_Id(int userId);

    // user별 등록한 URL을 조회하기 위한 기능
    UrlDTO findByOriginUrl(String originUrl);

    public void save(UrlDTO urlDTO);


    public void update(UrlDTO urlDTO);

    public void deleteById(int id);
}
