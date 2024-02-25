package me.heesu.demospringmvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.heesu.demospringmvc.domain.DomainObj;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DomainApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createDomainTest() throws Exception {

        DomainObj domain = new DomainObj();
        domain.setName("heesu");
        domain.setLimitEntry(-29);

        String jsonStr = objectMapper.writeValueAsString(domain);

        mockMvc.perform(
                    post("/api/domain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr)
                        .accept(MediaType.APPLICATION_JSON)) // 요청본문 domain obj (생성)
                .andDo(print())
                .andExpect(status().isBadRequest());
                //.andExpect(jsonPath("name").value("heesu"))
                //.andExpect(jsonPath("limitEntry").value(29));



    }
}