package me.heesu.demospringmvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HandlerMethodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void deleteEvent() throws Exception {
        mockMvc.perform(get("/objs/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                ;
    }

    @Test
    public void getNamePost() throws Exception {
        mockMvc.perform(post("/objs")
                    .param("name", "heesu")
                    .param("limit", "99"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("heesu"))
        ;
    }
}