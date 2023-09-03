package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.entity.Url;
import com.chobutton.back.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlServiceImpl implements UrlService{

    UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }

    // UrlEntity를 불러와 UrlDTO형대로 변환하는 로직 추가
    @Override
    public List<UrlDTO> findAll() {
        List<Url> urlList = urlRepository.findAll();
        List<UrlDTO> urlDTOList = fromUrlEntityForFindAll(urlList);
        return urlDTOList;
    }

    // user별 등록한 URL을 조회하기 위한 기능
    @Override
    public List<UrlDTO> findAllByUser_Id(int userId) {
        List<Url> urlList = urlRepository.findAllByUser_Id(userId);
        List<UrlDTO> urlDTOList = fromUrlEntityForFindAll(urlList);
        return urlDTOList;
    }

    // originUrl로 접속시 DB에 해당하는 데이터의 PK를 불러와 인코딩을 하기 위한 기능
    @Override
    public UrlDTO findByOriginUrl(String originUrl) {
        return null;
    }

    @Override
    public void save(UrlDTO urlDTO) {

    }

    @Override
    public void update(UrlDTO urlDTO) {

    }

    @Override
    public void deleteById(int id) {

    }



    // DTO 변환 메서드

    // Entity를 DTO로 변환하여 불러오는 메서드 정의
    public static UrlDTO fromUrlEntityForFind(Url url){
        return UrlDTO.builder()
                .id(url.getId())
                .userId(url.getUser().getId())
                .originUrl(url.getOriginUrl())
                .requestCount(url.getRequestCount())
                .build();
    }
    // Entity List를 DTO로 변환하여 불러오는 메서드 정의
    public static List<UrlDTO> fromUrlEntityForFindAll(List<Url> urlList){
        return urlList.stream()
                .map(UrlServiceImpl::fromUrlEntityForFind)
                .collect(Collectors.toList());
    }
    // Entity를 DTO로 변환하여 불러오는 메서드 정의
    public static UrlDTO fromUrlEntityForEncoding(Url url){
        return UrlDTO.builder()
                .id(url.getId())
                .build();
    }

    // UrlResponseDTO.Save 객체를 Entity로 변환해주는 메서드

    // UserRepository 구현후 기능 추가
//    public Url toUrlEntityForSave(UrlDTO urlDTO){
//
//        return new Url(userRepositoy.findById(urlDTO.getUserId()), urlDTO.getOriginUrl());
//    }


}
