package me.heesu.demospringmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.oxm.Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SampleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    Marshaller marshaller;

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
        e.setStartTime(LocalDateTime.now());

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
        marshaller.marshal(e, result);

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
