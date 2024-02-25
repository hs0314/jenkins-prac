package me.heesu.demospringmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.heesu.demospringmvc.domain.DomainObj;
import me.heesu.demospringmvc.domain.Person;
import me.heesu.demospringmvc.repository.PersonRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.oxm.Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest //@WebMvcTest와 다르게 웹 관련 bean뿐만 아니라 모든 bean을 등록
@AutoConfigureMockMvc
public class SampleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    Marshaller marshaller;

    @Test
    public void staticResourcesTest() throws Exception {
        this.mockMvc.perform(get("/mobile/index.html"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(Matchers.containsString("hello mobile index")))
        .andExpect(header().exists(HttpHeaders.CACHE_CONTROL));

    }

    @Test
    public void test1() throws Exception {
        this.mockMvc.perform(get("/test/heesu"))
                .andDo(print())
                .andExpect(content().string("test heesu"));
    }

    @Test
    public void test2() throws Exception {
        Person p = new Person();
        p.setName("heesu");
        Person savedPerson = personRepository.save(p);
        // 도메인 클래스의 경우 spring-data-jpa 가 자동으로 컨버터를 등록해주기 때문에 따로 포매터, 컨버터를 만들지 않아도 된다.

        this.mockMvc.perform(get("/test2")
                .param("id", savedPerson.getId().toString()))
                .andDo(print())
                .andExpect(content().string("test2 heesu"));
    }

    @Test
    public void jsonMessage() throws Exception {
        Person p = new Person();
        p.setId(1000L);
        p.setName("heesu");

        String jsonString = mapper.writeValueAsString(p);

        this.mockMvc.perform(get("/jsonMessage")
                    .content(jsonString)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void getStringFormHttpBodyTest() throws Exception{
        this.mockMvc.perform(get("/getStr").content("hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    public void getJsonTest() throws Exception{
        DomainObj e = new DomainObj();
        e.setName("heesu");
        e.setLimitEntry(5);
        e.setStartDate(LocalDate.now());

        String jsonStr = mapper.writeValueAsString(e);

        this.mockMvc.perform(get("/getJson")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonStr))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("heesu"));
    }

    @Test
    public void getXmlTest() throws Exception{
        DomainObj e = new DomainObj();
        e.setName("heesu");
        e.setLimitEntry(5);
        //e.setStartTime(LocalDateTime.now().to);

        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        marshaller.marshal(e, result); // xml 변환

        String xmlStr = writer.toString();

        this.mockMvc.perform(get("/getJson")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML)
                .content(xmlStr))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("domainObj/name").string("heesu"));
    }
}
