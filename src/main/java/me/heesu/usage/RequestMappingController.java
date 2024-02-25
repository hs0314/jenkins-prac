package me.heesu.usage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RequestMappingController {

    /**
     * http 요청 매핑
     * 요청 메서드별 처리
     * 방법1. 어노테이션 기반으로 @GetMapping, @PostMapping
     * 방법2. @RequestMapping 어노테이션에 method 값 지정
     *    -> 동일한 매핑이 있는 경우, 가장 구체적으로 매팡되는 핸들러 선택
     */
    @RequestMapping(value = "/hello")
    //@GetHelloMapping
    @ResponseBody
    public String hello(){
        return "hello";
    }

    /**
     * URI 패턴 매핑
     * - 여러 url에 대한 핸들러 매핑을 하나의 메서드에 매핑할 수 있음
     * - 정규표현식, 요청식별자로 url 매핑 가능
     *
     * @GetMapping("/test/**")  // /test/author/heesu
     * @GetMapping("/test/?")   // /test/1
     * @GetMapping({"/url1", "url2"})
     *
     * @GetMapping("/test.json") // 확장자 매핑은 RFD 보안이슈로 해당 매핑을 스프링부트는 지원하지 않음
     */

    /*
    @RequestMapping("/**")
    @ResponseBody
    public String url1(){
        return "url1";
    }
     */

    //정규식 -> name으로 받음
    @RequestMapping("/{name:[a-z]+}")
    @ResponseBody
    public String heesu(@PathVariable String name){
        return name;
    }


    /**
     * 특정한 타입의 요청만 처리하는 핸들러 매핑
     * contents-type => 요청 바디 타입       => consumes와 매핑
     * accept        => 받고자하는 결과 타입   => produces와 매핑
     *
     * - consumes로 넘겨주는 문자열이 요청의 contents-type과 매핑되는지 확인
     *   요청 url에 대한 미디어 타입이 다르면 415 status로 응답을 내림
     * - produces에서 명시한 미디어 타입과 요청의 accept header type이 맞아야 함 (다를 경우 406 내림)
     */
    @RequestMapping(value = "/onlyJson",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ResponseBody
    public String onlyJson(){
        return "onlyJson plz";
    }


    /**
     * http요청 헤더와 파라미터를 핸들러 매핑
     * - headers의 값과 요청헤더의 해당 값이 맞지 않다면 404
     * - params의 값과 요청파라미터의 해당 값이 맞지 않다면 400
     */
    @RequestMapping(value = "/httpReqHeader",
                    headers = HttpHeaders.AUTHORIZATION + "=" + "123",
                    params = "name=heesu")
    @ResponseBody
    public String httpHeaderWithAuth(){
        return "AUTHORIZATION plz";
    }
}
