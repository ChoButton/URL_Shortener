package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.entity.Url;
import com.chobutton.back.repository.UrlRepository;
import com.chobutton.back.util.Base56Util;
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
    public String urlEncoding(String originUrl) {
        Url url = urlRepository.findByOriginUrl(originUrl);
        UrlDTO urlDTO = fromUrlEntityForFind(url);
        int originUrlId = urlDTO.getId();
        String shortUrl = Base56Util.base56Encoding(originUrlId);
        return shortUrl;
    }

    // shortenUrl이 전달될때 디코딩하여 Id값을 리턴해주는 메서드
    @Override
    public String urlDecoding(String shortenUrl) {
        int originUrlId = Base56Util.base56Decoding(shortenUrl);
        String originUrl = urlRepository.findById(originUrlId).get().getOriginUrl();
        return originUrl;
    }

    @Override
    public void save(UrlDTO urlDTO) {

    }

    @Override
    public void update(UrlDTO urlDTO) {

    }

    @Override
    public void deleteById(int id) {
        urlRepository.deleteById(id);
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

    // UrlDTO 객체를 Entity로 변환해주는 메서드
    // User의 FindById가 필요하기 때문에 추후 구현
//    public static Url toEntityForSave(UrlDTO urlDTO){
//        return Url.builder()
//                .user(urlDTO.getUserId())
//                .originUrl(urlDTO.getOriginUrl())
//                .requestCount(0)
//                .build();
//    }

}
