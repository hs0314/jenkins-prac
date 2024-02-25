package me.heesu.usage;

import me.heesu.demospringmvc.domain.DomainObj;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class DomainApiController {

    /**
     * ***요청 관련
     * @RequestBody 를 통해서는 http요청 본문에 대한 내용만 핸들러 메서드에서 사용할 수 있지만
     * HttpEntity를 이용하면 header에 대한 내용도 가져올 수 있음
     *
     * @RequestBody를 통해서 http요청 본문을 가져오는 경우, @Valid, BindingResult 등을 통해서
     * 매핑되는 객체에 대한 벨리데이션 핸들링이 가능
     *
     *
     * ***응답 관련
     * @ResponseBody 해당 메서드 어노테이션 사용 시, 메서드에서 리턴하는 값을 응답 본문에 HttpMessageConverter를 이용해서 담아줌
     *  요청의 accept 헤더값을 통해서 클라이언트가 원하는 응답 타입을 알 수 있음
     * @RestController를 사용한 클래스 내부의 메서드는 기본적으로 모든 메서드가 @ResponseBody를 붙임
     *
     * ResponseEntity의 경우
     */
    @PostMapping("/api/domain")
    public ResponseEntity<DomainObj> createDomain(@RequestBody @Valid DomainObj domain,
                                                  BindingResult bindingResult){
        /* @RequestBody를 통해서 http요청을 컨버팅할 수 있는 적당한 HttpMessageConverter를
         가져오고 핸들러 어뎁터가 DomainObj 객체에 맞도록 아규먼트를 리졸빙 한다.
         */

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(domain);
    }

}
