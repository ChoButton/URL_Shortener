package com.chobutton.back.controller;

import com.chobutton.back.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlRedirectControllerTest {
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

    @Test
    @Transactional
    @DisplayName("shortenUrl인 Z로 접속할경우 등록된 URL중 첫번째인" +
            "http://originURL1.com이 리턴되며 정상적으로 리다이렉트된다.")
    public void redirectToOriginUrlTest() throws Exception{
        //given
        String shortenUrl = "/shortnee/Z";
        String originUrl = "http://originURL1.com";

        //when
        final ResultActions result = mockMvc.perform(get(shortenUrl));

        //then
        result
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", originUrl));
    }

}
