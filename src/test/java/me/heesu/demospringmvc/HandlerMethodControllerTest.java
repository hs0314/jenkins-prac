package me.heesu.demospringmvc;

import me.heesu.demospringmvc.domain.DomainObj;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HandlerMethodControllerTest {

    @Autowired
    MockMvc mockMvc;

    MockHttpSession session;

    @Before
    public void setup(){
        // 세션 초기화
        session = new MockHttpSession();
    }

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
                    .param("name", "heesu"))
                    //.param("limitEntry", "99"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("heesu"));
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

        ResultActions result = mockMvc.perform(post("/domain")
                    .param("name", "heesu")
                    .param("limitEntry", "-1")
                    .session(session))
                .andDo(print())
                .andExpect(status().isBadRequest());

        ModelAndView mav = result.andReturn().getModelAndView();
        Map<String, Object> model = mav.getModel();
        System.out.print(model.size());
    }

    @Test
    public void sessionAttributesTest() throws Exception {
        MockHttpServletRequest req = mockMvc.perform(get("/domain/form"))
                .andDo(print())
                .andExpect(view().name("/domain/form"))
                .andExpect(model().attributeExists("domain"))
                .andExpect(request().sessionAttribute("domain", notNullValue()))
                .andReturn().getRequest();

        Object domain = req.getSession().getAttribute("domain");
        System.out.println(domain);
    }

    @Test
    public void sessionAttributeAndFlashAttributesTest() throws Exception {
        DomainObj domain = new DomainObj();
        domain.setName("name");
        domain.setLimitEntry(100);

        mockMvc.perform(
                get("/domain/form")
                    .sessionAttr("visitTime", LocalDateTime.now())
                    .flashAttr("newDomain", domain))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getDomainListTest() throws Exception {
        DomainObj domain = new DomainObj();
        domain.setName("test");
        domain.setLimitEntry(1);

        mockMvc.perform(get("/domain/list")
        .sessionAttr("visitTime", LocalDateTime.now())
        .flashAttr("newDomain", domain))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//p").nodeCount(4)); //<p>태그 갯수
    }

    @Test
    public void fileUploadTest() throws Exception {
        MockMultipartFile testFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "hello files".getBytes());

        this.mockMvc.perform(multipart("/file").file(testFile))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

}