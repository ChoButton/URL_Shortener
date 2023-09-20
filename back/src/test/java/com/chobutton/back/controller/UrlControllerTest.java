package com.chobutton.back.controller;

import com.chobutton.back.dto.UrlDTO;
import com.chobutton.back.service.UrlService;
import com.chobutton.back.util.Base56Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UrlService urlService;

    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // 관리자용 기능 테스트 --------------------------------------------------------------------------------------------
    @Test
    @Transactional
    @DisplayName("/url/admin/all 경로에 GET 요청을 보낼경우 전체 URL리스트를 불러온며," +
            "0번째 인덱스의 URL주소는 http://originURL1.com 일것이며," +
            "4번째 인덱스의 URL주소는 http://originURL5.com 일것이다.")
    public void findAllUrlsForAdminTest() throws Exception{ //mockMvc의 예외를 던져주기 위한 Exception
        // given
        String url = "/url/admin/all";
        String index0Url = "http://originURL1.com";
        String index4Url = "http://originURL5.com";

        //when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].originUrl").value(index0Url))
                .andExpect(jsonPath("$[4].originUrl").value(index4Url));
    }

    @Test
    @Transactional
    @DisplayName("/url/admin/all/{userEmail} 경로에 GET 요청을 보낼경우 {userEmail}에 해당하는" +
            "URL리스트를 불러온며, 0번째 인덱스의 URL주소는 http://originURL1.com 일것이며," +
            "2번째 인덱스의 URL주소는 http://originURL3.com 일것이다.")
    public void findAllByUserEmailForAdminTest() throws Exception{
        //given
        String url = "/url/admin/all/test1@test.com";
        String index0Url = "http://originURL1.com";
        String index2Url = "http://originURL3.com";

        //when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].originUrl").value(index0Url))
                .andExpect(jsonPath("$[2].originUrl").value(index2Url));
    }

    @Test
    @Transactional
    @DisplayName("기존에 등록된 Url을 수정할경우 수정사항이 반영되며 id값은 변하지 않는다.")
    public void updateUrlForAdminTest() throws Exception{
        //given
        String url = "/url/admin/update";
        int id = 1;
        String updateOriginUrl = "www.update.com";
        int userId = 1;
        int urlId = urlService.findAllByUserId(userId).get(0).getId();

        UrlDTO urlDTO = UrlDTO.builder()
                .id(id)
                .originUrl(updateOriginUrl)
                .userId(userId)
                .build();

        final String requestBody = objectMapper.writeValueAsString(urlDTO);

        //when
        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        UrlDTO updatedUrlDTO = urlService.findAllByUserId(userId).get(0);

        //then
        assertThat(updatedUrlDTO.getOriginUrl()).isEqualTo(updateOriginUrl);
        assertThat(updatedUrlDTO.getId()).isEqualTo(urlId);
    }

    // 사용자용 기능 테스트 --------------------------------------------------------------------------------------------
    @Test
    @Transactional
    @DisplayName("새로운 Url을 등록할경우 단축된 Url이 리턴되며," +
            "리턴된 Url이 전체 Url리스트의 마지막 인덱스의 Id를" +
            "인코딩 한 값과 일치한다.")
    public void createShortenUrlTest() throws Exception{
        //given
        String originUrl = "http://originURL6.com";
        int userId = 3;
        String url = "/url/user/create";

        UrlDTO urlDTO = UrlDTO.builder()
                .userId(userId)
                .originUrl(originUrl)
                .build();

        final String requestBody = objectMapper.writeValueAsString(urlDTO);

        //when
        ResultActions result = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        String shortenUrl = result.andReturn().getResponse().getContentAsString();

        List<UrlDTO> urlDTOList = urlService.findAll();
        int urlId = urlDTOList.get(urlDTOList.size()-1).getId();
        String encodUrl = Base56Util.base56Encoding(urlId);

        //then
        assertThat(shortenUrl).isEqualTo(encodUrl);
    }

    @Test
    @Transactional
    @DisplayName("/url/admin/all/{userId} 경로에 GET 요청을 보낼경우 {userId}에 해당하는" +
            "URL리스트를 불러온며, 0번째 인덱스의 URL주소는 http://originURL1.com 일것이며," +
            "2번째 인덱스의 URL주소는 http://originURL3.com 일것이다.")
    public void findAllByUserIdForUserTest() throws Exception{
        //given
        String url = "/url/user/all/1";
        String index0Url = "http://originURL1.com";
        String index2Url = "http://originURL3.com";

        //when
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].originUrl").value(index0Url))
                .andExpect(jsonPath("$[2].originUrl").value(index2Url));
    }

    // 공용 기능 테스트 --------------------------------------------------------------------------------------------
    @Test
    @Transactional
    @DisplayName("1번 url을 삭제할 경우 해당 id값으로 단축url이 접속이 되지 않는다.")
    public void deleteByIdTest() throws Exception {
        // given
        String url = "/url/delete/1";
        String shortenUrl = "/shortnee/Z";

        //when
        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        //then
        mockMvc.perform(get(shortenUrl))
                .andExpect(status().isNotFound());
    }
}
