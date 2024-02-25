package me.heesu.usage;

import me.heesu.demospringmvc.validator.DomainValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * @ExceptionHandler, @InitBinder, @ModelAttribute
 * 위와 같은 처리를 모든 컨트롤러에서 공통적으로 사용하고 싶을때 @ControllerAdvice를 적용한 클래스에 로직을 구현
 *  - 필요한 컨트롤러에만 적용 가능
 */
//@ControllerAdvice(assignableTypes = DomainApiController.class)
@ControllerAdvice(basePackages = "me.heesu.usage")
public class GlobalController {

    /**
     * REST API 에서 @ExceptionHandler는 보통 response 타입을 ResponseEntity를 사용
     *  -> http요청에 대한 응답코드, body message 등을 원하는 형태로 내릴 수 있음
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity errorHandler(){
        return ResponseEntity.badRequest().body("error caused by ...");
    }


    /**
     * @InitBinder
     * - 필수로 WebDataBinder를 아규먼트로 가지고 모든 메서드 이전에 해당 메서드가 먼저 호출됌
     *
     *  setDisallowedFields() - 특정 필드값은 데이터가 넘어와도 바인딩 시키지 않음
     *  setAllowedFields()
     *  addValidators()
     */
    @InitBinder("domain") // 해당 modelAttribute를 바인딩 받을때에만 initBinder를 호출할 수 있음
    public void initDomainBinder(WebDataBinder webDataBinder){
        webDataBinder.setDisallowedFields("id"); // id는 보통 repository에서 insert시 셋팅
        webDataBinder.addValidators(new DomainValidator());
    }

    /**
     * @ModelAttribute
     * 동일 클래스내에서 모든 메서드가 공통적으로 참고해야하는 model 정보가 있다면
     * 해당 어노테이션을 메서드 레벨에 붙이고 공통되는 model attribute 값을 셋팅해서 사용 (중복코드 제거)
     *
     * 다른 메서드가 실행되지 않아도 미리 실행하고 model에 해당 attribute값을 셋팅
     */
    @ModelAttribute
    public void commonSetting(Model model){
        model.addAttribute("categories",  List.of("cate1", "cate2", "cate3"));
    }
}
