package me.heesu.demospringmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SampleController {

    @Autowired
    SampleService sampleService;


    @GetMapping("/getStr")
    @ResponseBody
    public String getStr(@RequestBody String body){
        return body;
    }

    @GetMapping("getJson")
    @ResponseBody
    public DomainObj getJson(@RequestBody DomainObj e){
        return e;
    }
}
