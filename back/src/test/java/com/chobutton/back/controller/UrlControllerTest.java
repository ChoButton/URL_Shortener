package com.chobutton.back.controller;

import com.chobutton.back.service.UrlService;
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
}
