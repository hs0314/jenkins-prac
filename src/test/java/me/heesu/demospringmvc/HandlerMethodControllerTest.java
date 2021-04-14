package me.heesu.demospringmvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HandlerMethodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getObjTest() throws Exception {
        mockMvc.perform(get("/objs/1;name=heesu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("heesu"));
    }

    @Test
    public void postObjTest() throws Exception {
        mockMvc.perform(post("/objs")
                    .param("name", "heesu")
                    .param("limitEntry", "99"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("heesu"))
        ;
    }

    @Test
    public void getObjModelAttributeTest() throws Exception {
        mockMvc.perform(post("/objs/model")
                    .param("name", "heesu")
                    .param("limitEntry", "-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("heesu"));
    }

    @Test
    public void getObjFormErrorTest() throws Exception {
        ResultActions result = mockMvc.perform(post("/objects")
                    .param("name", "heesu")
                    .param("limitEntry", "-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());

        ModelAndView mav = result.andReturn().getModelAndView();
        Map<String, Object> model = mav.getModel();
        System.out.print(model.size());
    }

    @Test
    public void sessionAttributesTest() throws Exception {
        MockHttpServletRequest req = mockMvc.perform(get("/objs/form"))
                .andDo(print())
                .andExpect(view().name("/domain/form"))
                .andExpect(model().attributeExists("domain"))
                .andExpect(request().sessionAttribute("domain", notNullValue()))
                .andReturn().getRequest();

        Object domain = req.getSession().getAttribute("domain");
        System.out.println(domain);
    }
}