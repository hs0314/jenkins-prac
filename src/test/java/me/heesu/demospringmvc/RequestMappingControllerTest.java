package me.heesu.demospringmvc;

import me.heesu.usage.RequestMappingController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest // @WebMvcTest 어노테이션을 통해서 MockMvc빈을 얻어올 수 있고 특정 http요청에 대한 테스트를 진행할 수 있음
public class RequestMappingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void helloTest() throws Exception{
        mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect((status().isOk()))
                .andExpect(content().string("hello"))
                ;

    }

    @Test
    public void handlerTest() throws Exception{
        mockMvc.perform(get("/heesu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("heesu"))
                .andExpect(handler().handlerType(RequestMappingController.class))
                .andExpect(handler().methodName("heesu"));
    }


    @Test
    public void onlyJsonTest() throws Exception {
        mockMvc.perform(get("/onlyJson")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("onlyJson plz"))
        ;
    }


    @Test
    public void httpHeaderTest() throws Exception {
        mockMvc.perform(get("/httpReqHeader")
                .header(HttpHeaders.AUTHORIZATION, "123")
                .param("name", "heesu"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void httpHeadMethodTest() throws Exception {
        mockMvc.perform(head("/hello")
                        .param("name", "heesu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("")); // head method는 응답본문이 없음

    }

    @Test
    public void httpOptionsMethodTest() throws Exception {
        mockMvc.perform(options("/hello")
                        .param("name", "heesu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.ALLOW))
                // 순서 상관없이 값 존재여부를 판단하기 위해서 아래와같이 테스트코드 작성
                .andExpect(header().stringValues(HttpHeaders.ALLOW, hasItems(
                        containsString("GET"),
                        containsString("POST"),
                        containsString("PUT"),
                        containsString("PATCH"),
                        containsString("DELETE"),
                        containsString("HEAD"),
                        containsString("OPTIONS"))));
    }



}
