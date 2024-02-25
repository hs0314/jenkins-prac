package me.heesu.demospringmvc;

import me.heesu.demospringmvc.domain.DomainObj;
import me.heesu.demospringmvc.domain.Person;
import me.heesu.demospringmvc.formatter.PersonFormatter;
import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {


    /**
     * formatter를 이용한 String -> obj 컨버팅
     * @see PersonFormatter
     */
    @GetMapping("/test/{name}")
    public String test(@PathVariable("name") Person person){
        // name을 Person의 name에 매핑하기 위해서는 formatter설정이 필요
        // formatter는 문자열과 객체의 변환에 대한 처리
        return "test "+person.getName();
    }

    /**
     * spring-data-jpa 를 이용해서 객체의 id를 이용해서 매핑되는 도메인 객체로 컨버팅
     */
    @GetMapping("/test2")
    public String test2(@RequestParam("id") Person person){
        return "test2 "+person.getName();
    }

    ////// HTTP converter (json, xml)
    // @ResponseBody, @RequestBody을 이용해서 요청, 응답 본문에 메세지 작성 시 사용
    // Springboot는 기본적으로 jackson2 디펜던시를 가지고 json 컨버터가 셋팅되어있음
    @GetMapping("/jsonMessage")
    @ResponseBody // RestController에는 자동으로 모든 메서드에 해당 어노테이션을 붙임
    public Person jsonMessage(@RequestBody Person person){
        return person;
    }

    @GetMapping("/getStr")
    @ResponseBody
    public String getStr(@RequestBody String body){
        return body;
    }

    @GetMapping("/getJson")
    @ResponseBody
    public DomainObj getJson(@RequestBody DomainObj e){
        return e;
    }
}
