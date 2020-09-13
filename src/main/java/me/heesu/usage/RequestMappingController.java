package me.heesu.usage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestMappingController {
    /**
     * http 요청 매핑
     * 요청 메서드별 처리
     * 방법1. 어노테이션 기반으로 @GetMapping, @PostMapping
     * 방법2. @RequestMapping 어노테이션에 method 값 지정
     *    -> 동일한 매핑이 있는 경우, 가장 구체적으로 매팡되는 핸들러 선택
     */
    //@RequestMapping(value = "/hello", method = RequestMethod.GET)
    @GetHelloMapping
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
     */

    @RequestMapping("/test/**")
    @ResponseBody
    public String url1(){
        return "url1";
    }

    @RequestMapping("/heesu")
    @ResponseBody
    public String heesu(){
        return "heesu";
    }


    /**
     * 특정한 타입의 요청만 처리하는 핸들러 매핑
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
        return "onlyJson plz.";
    }


    /**
     * http요청 헤더와 파라미터를 핸들러 매핑
     */
    @RequestMapping(value = "/httpReqHeader", headers = HttpHeaders.AUTHORIZATION + "=" + "123", params = "name=heesu")
    @ResponseBody
    @GetMapping
    public String httpHeaderWithAuth(){
        return "AUTHORIZATION plz.";
    }
}
