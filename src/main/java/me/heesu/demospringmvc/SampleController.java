package me.heesu.demospringmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {


    /*

    */
    @GetMapping("/test/{name}")
    public String test(@PathVariable("name")Person person){
        // name을 Person의 name에 매핑하기 위해서는 formatter설정이 필요
        // formatter는 문자열과 객체의 변환에 대한 처리
        return "test "+person.getName();
    }

    @GetMapping("/test2")
    public String test2(@RequestParam("id")Person person){
        return "test2 "+person.getName();
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
