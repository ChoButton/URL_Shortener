package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.entity.Url;
import com.chobutton.back.entity.User;
import com.chobutton.back.repository.UrlRepository;
import com.chobutton.back.repository.UserRepository;
import com.chobutton.back.util.Base56Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlServiceImpl implements UrlService{

    UrlRepository urlRepository;
    UserRepository userRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository,
                          UserRepository userRepository){
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public List<UrlDTO> findAll() {
        List<Url> urlList = urlRepository.findAll();
        List<UrlDTO> urlDTOList = fromUrlEntityForFindAll(urlList);
        return urlDTOList;
    }

    // user의 id를 이용하여 등록한 URL을 조회하기 위한 기능
    @Transactional
    @Override
    public List<UrlDTO> findAllByUserId(int userId) {
        List<Url> urlList = urlRepository.findAllByUserId(userId);
        List<UrlDTO> urlDTOList = fromUrlEntityForFindAll(urlList);
        return urlDTOList;
    }

    // 관리자의 유저별 등록 URL검색 기능을 위해 유저의 email을 통해 조회하는 기능
    @Transactional
    @Override
    public List<UrlDTO> findAllByUserEmail(String email){
        User user = userRepository.findByEmail(email);
        int userId = user.getId();
        List<Url> urlList = urlRepository.findAllByUserId(userId);
        List<UrlDTO> urlDTOList = fromUrlEntityForFindAll(urlList);
        return urlDTOList;
    }


    // originUrl입력시 DB에 해당하는 데이터의 PK를 불러와 인코딩을 하기 위한 기능
    @Transactional
    @Override
    public String urlEncoding(String originUrl) {
        Url url = urlRepository.findByOriginUrl(originUrl);
        UrlDTO urlDTO = fromUrlEntityForFind(url);
        int originUrlId = urlDTO.getId();
        String shortUrl = Base56Util.base56Encoding(originUrlId);
        return shortUrl;
    }

    // shortenUrl이 전달될때 디코딩하여 Id값을 리턴후 해당 Id값으로 originUrl을 리턴해주는 기능
    @Transactional
    @Override
    public String urlDecoding(String shortenUrl) {
        int originUrlId = Base56Util.base56Decoding(shortenUrl);
        String originUrl = urlRepository.findById(originUrlId).get().getOriginUrl();
        // originUrl로 접속시 접속수 +1
        urlRepository.findById(originUrlId).get().incrementRequestCount();
        return originUrl;
    }

    //URL 등록후 바로 단축된URL을 확인할수 있도록 단축된 URL리턴
    @Transactional
    @Override
    public String save(UrlDTO urlDTO) {
        urlRepository.save(toEntityForSave(urlDTO));
        List<Url> urlList = urlRepository.findAllByUserId(urlDTO.getUserId());
        int urlId = urlList.get(urlList.size()-1).getId();
        return Base56Util.base56Encoding(urlId);
    }

    @Override
    public void update(UrlDTO urlDTO) {
        Url url = urlRepository.findById(urlDTO.getId()).get();
        url.updateOriginUrl(urlDTO.getOriginUrl());
    }

    @Override
    public void deleteById(int id) {
        urlRepository.deleteById(id);
    }



    // DTO 변환 메서드

    // Entity를 DTO로 변환하여 불러오는 메서드 정의
    private static UrlDTO fromUrlEntityForFind(Url url){
        return UrlDTO.builder()
                .id(url.getId())
                .userId(url.getUserId())
                .originUrl(url.getOriginUrl())
                .requestCount(url.getRequestCount())
                .shortenUrl("localhost:8080/shortnee/" + Base56Util.base56Encoding(url.getId()))
                .build();
    }
    // Entity List를 DTO로 변환하여 불러오는 메서드 정의
    private static List<UrlDTO> fromUrlEntityForFindAll(List<Url> urlList){
        return urlList.stream()
                .map(UrlServiceImpl::fromUrlEntityForFind)
                .collect(Collectors.toList());
    }
    // Entity를 DTO로 변환하여 불러오는 메서드 정의
    private static UrlDTO fromUrlEntityForEncoding(Url url){
        return UrlDTO.builder()
                .id(url.getId())
                .build();
    }

     //UrlDTO 객체를 Entity로 변환해주는 메서드
    private static Url toEntityForSave(UrlDTO urlDTO){
        return Url.builder()
                .userId(urlDTO.getUserId())
                .originUrl(urlDTO.getOriginUrl())
                .requestCount(0)
                .build();
    }
}
