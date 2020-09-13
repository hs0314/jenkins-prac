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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RequestMappingControllerTest {
    @Autowired
    MockMvc mockMvc;

    /**
     * @WebMvcTest 어노테이션을 통해서 MockMvc빈을 얻어올 수 있고 특정 http요청에 대한 테스트를 진행할 수 있음
     */
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
                .andExpect(handler().methodName("heesu"))  //url1 핸들러로 매핑되지 않는다
                ;
    }


    @Test
    public void onlyJsonTest() throws Exception {
        mockMvc.perform(get("/onlyJson")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }


    @Test
    public void httpHeaderTest() throws Exception {
        mockMvc.perform(get("/httpReqHeader")
                .header(HttpHeaders.AUTHORIZATION, "123")
                .param("name", "heesu"))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

}
