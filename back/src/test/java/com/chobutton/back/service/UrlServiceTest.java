package com.chobutton.back.service;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.entity.Url;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UrlServiceTest {

    @Autowired
    UrlService urlService;

    @Test
    @Transactional
    @DisplayName("전체글 조회시 데이터의 갯수는 5개일것이다.")
    public void finaAllTest(){
        //given
        int result = 5;

        //when
        List<UrlDTO> urlList = urlService.findAll();

        //then
        assertThat(urlList.size()).isEqualTo(result);
    }

    @Test
    @Transactional
    @DisplayName("uersId 1번으로 조회시 총 3개 인덱스의 데이터가 조회되며" +
            "0번째 인덱스의 originUrl은 http://originURL1.com" +
            "2번째 인덱스의 originUrl은 http://originURL3.com일것이다.")
    public void findAllByUser_IdTest(){
        //given
        int userId = 1;
        String index0Url = "http://originURL1.com";
        String index2Url = "http://originURL3.com";

        //when
        List<UrlDTO> urlList = urlService.findAllByUser_Id(userId);

        //then
        assertThat(urlList.size()).isEqualTo(3);
        assertThat(urlList.get(0).getOriginUrl()).isEqualTo(index0Url);
        assertThat(urlList.get(2).getOriginUrl()).isEqualTo(index2Url);
    }

    @Test
    @Transactional
    @DisplayName("http://originURL2.com로 조회할경우 id가 2인 데이터가 조회된다")
    public void findByOriginUrlTest(){
        //given
        String originUrl = "http://originURL2.com";

        //when
        UrlDTO url = urlService.findByOriginUrl(originUrl);

        //then
        assertThat(url.getId()).isEqualTo(2);
    }
}
