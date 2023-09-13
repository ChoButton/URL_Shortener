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
        List<UrlDTO> urlList = urlService.findAllByUserId(userId);

        //then
        assertThat(urlList.size()).isEqualTo(3);
        assertThat(urlList.get(0).getOriginUrl()).isEqualTo(index0Url);
        assertThat(urlList.get(2).getOriginUrl()).isEqualTo(index2Url);
    }

    @Test
    @Transactional
    @DisplayName("http://originURL2.com로 조회할경우 base52인코딩을 실시하여 인코딩된 값을 리턴한다")
    public void urlEncodingTest(){
        //given
        String originUrl = "http://originURL2.com";
        String result = "j";

        //when
        String shortenUrl = urlService.urlEncoding(originUrl);

        //then
        assertThat(shortenUrl).isEqualTo(result);
    }

    @Test
    @Transactional
    @DisplayName("shortenUrl j가 전달될 경우 http://originURL2.com을 리턴한다.")
    public void urlDecodingTest(){
        //given
        String originUrl = "http://originURL2.com";
        String shortenUrl = "j";

        //when
        String redirectUrl = urlService.urlDecoding(shortenUrl);

        //then
        assertThat(redirectUrl).isEqualTo(originUrl);
    }

    @Test
    @Transactional
    @DisplayName("id 2번이 삭제될경우 전체 데이터의 크기는 4개일것이다.")
    public void deleteByIdTest(){
        //given
        int id = 2;

        //when
        urlService.deleteById(id);
        List<UrlDTO> urlList = urlService.findAll();

        //then
        assertThat(urlList.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    @DisplayName("새로운 URL이 등록될경우 전체 데이터의 갯수는 6개일 것이고," +
            "새로 입력한 데이터는 마지막 인덱스에 있을것이다.")
    public void saveTest(){
        //given
        String originUrl = "http://originURL6.com";
        int userId = 3;

        //when
        UrlDTO url = UrlDTO.builder()
                .originUrl(originUrl)
                .userId(userId)
                .build();

        urlService.save(url);

        List<UrlDTO> urlList = urlService.findAll();

        //then
        assertThat(urlList.size()).isEqualTo(6);
        assertThat(urlList.get(urlList.size()-1).getOriginUrl()).isEqualTo(originUrl);
        assertThat(urlList.get(urlList.size()-1).getUserId()).isEqualTo(userId);
    }

    @Test
    @Transactional
    @DisplayName("UrlDTO 형태로 전달된 4번 Url의 originUrl을 변경할경우 변경된 사항이 반영되며," +
            "Id는 변경전이랑 같을것이다.")
    public void updateTest(){
        //given
        int id = urlService.findAll().get(3).getId();
        String newUrl = "http://newUrl.com";
        UrlDTO urlDTO = urlService.findAll().get(3);
        urlDTO.setOriginUrl(newUrl);

        //when
        urlService.update(urlDTO);
        UrlDTO updatedUrlDTO = urlService.findAll().get(3);

        //then
        assertThat(updatedUrlDTO.getOriginUrl()).isEqualTo(newUrl);
        assertThat(updatedUrlDTO.getId()).isEqualTo(id);
    }

    @Test
    @Transactional
    @DisplayName("test1@test.com 이메일로 url을 조회할경우 1번유저가 등록한 url 3개가 조회될 것이다.")
    public void findAllByUserEmailTest(){
        //given
        String email = "test1@test.com";

        //when
        List<UrlDTO> urlDTOList = urlService.findAllByUserEmail(email);

        //then
        assertThat(urlDTOList.size()).isEqualTo(3);
    }
}
