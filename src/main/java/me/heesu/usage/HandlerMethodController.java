package me.heesu.usage;

import me.heesu.demospringmvc.DomainObj;
import org.h2.engine.Domain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("domain") // 해당 클래스안에서 모델에 들어가는 "domain" attribute를 세션에 셋팅
public class HandlerMethodController {
    /**
     * uri 패턴에서 핸들러에 값 매핑
     * - @PathVariable
     * - @MatrixVariable (추가적으로 pathMatch 관련 config 변경이 필요
     *
     * - 요청에서 {id} 값을 String으로 보내도 알아서 Integer로 변환
     */
    @GetMapping(value = "/objs/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DomainObj getObj(@PathVariable Integer id,      // pathVariable 이 필수로 들어와야 함
                            @MatrixVariable String name) {
        DomainObj obj = new DomainObj();
        obj.setId(id);
        obj.setName(name);

        return obj;
    }

    /**
     * 요청 매개변수 (?쿼리스트링 혹은 요청 본문에 form으로 받는 파라미터)
     *  복함적인 객체가 아닌 단순타입은 해당 어노테이션을 통해서 매핑 가능
     *
     * - Map<String, String> 으로도 요청 파라미터를 매핑해서 쓸 수 있음
     */
    @PostMapping(value = "/objs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DomainObj getObjPost(@RequestParam String name,
                                @RequestParam(required = false) Integer limitEntry) {
        DomainObj obj = new DomainObj();
        obj.setName(name);
        if(limitEntry != null) obj.setLimitEntry(limitEntry);
        return obj;
    }

    /**
     * form submit 핸들링
     *  - html form 서브밋 된 데이터를 @RequestParam으로 핸들러에서 받을 수 있음
     *
     * @SessionAttributes
     *  - 모델 정보를 HTTP 세션에 자동으로 저장
     *   왜사용하는가?
     *    - form을 여러 화면으로 나누어서 처리할때 직전단계의 값을 세션에 넣어서 뷰 별로 재사용 가능 (멀티폼)
     *    - 장바구니 같이 정보가 남아있어야하는 경우
     *   메서드 아규먼트로 사용하려면 SessionStatus를 사용
     *
     *   더 로우레벨로 사용하려면 HttpSession을 아규먼트로 받아서 사용해도 됌
     */
    @GetMapping("/objs/form")
    public String objForm(Model model) {
        DomainObj d = new DomainObj();
        d.setLimitEntry(50);

        model.addAttribute("domain", d);

        return "/domain/form";
    }

    /**
     * 멀티폼 서브밋 처리를 통한 session 사용
     */
    @GetMapping("/objs/form/name")
    public String objNameForm(Model model) {
        DomainObj d = new DomainObj();
        model.addAttribute("domain", d); // 세션에 해당 model 객체가 들어감
        return "/domain/form-name";
    }
    @GetMapping("/objs/form/limit")
    public String objLimitForm(@ModelAttribute("domain") DomainObj domain, Model model) {
        model.addAttribute("domain", domain); // 세션에 있는 domain객체를 @ModelAttribute를 통해서 꺼내고 model에 셋팅
        return "/domain/form-limit";
    }
    @PostMapping("/objs/form/name")
    public String objectFormNameSubmit(@Validated @ModelAttribute("domain") DomainObj domain,
                            BindingResult bindingResult,
                            Model model,
                            SessionStatus sessionStatus
    ){
        if(bindingResult.hasErrors()){
            return "/domain/form-name";
        }
        return "redirect:/objs/form/limit";
    }
    @PostMapping("/objs/form/limit")
    public String objectFormLimitSubmit(@Validated @ModelAttribute("domain") DomainObj domain,
                                       BindingResult bindingResult,
                                       Model model,
                                       SessionStatus sessionStatus
    ){
        if(bindingResult.hasErrors()){
            return "/domain/form-limit";
        }
        // 메서드내애서 아규먼트로 SessionStatus를 받아서 특정시점에 세션만료 등 처리 가능
        sessionStatus.setComplete();
        return "redirect:/domain/list";
    }


    /**
     * form submit 에러 핸들링
     * @Validated, BindingResult를 이용한 에러 핸들링
     */
    @PostMapping("/objects")
    public String createObj(@Validated @ModelAttribute("domain") DomainObj domain,
                            BindingResult bindingResult,
                            Model model,
                            SessionStatus sessionStatus
                            ){
        if(bindingResult.hasErrors()){
            return "/domain/form-name";
        }

        // 메서드내애서 아규먼트로 SessionStatus를 받아서 특정시점에 세션만료 등 처리 가능
        sessionStatus.setComplete();

        List<DomainObj> domainObjs = new ArrayList<>();
        domainObjs.add(domain);
        model.addAttribute("domainList", domainObjs);

        return "redirect:/domain/list"; // 요청처리를 GET "/domain/list"에 위임
    }
    // 중복 폼 서브밋 방지를 위해서 Post / Redirect / Get 패턴을 이용해서 브라우저의 리프레시 액션에 대응
    @GetMapping("/domain/list")
    public String getDomainObjects(Model model,
                                   @SessionAttribute("visitTime")LocalDateTime visitTime){
        //HttpSession 대신 @SessionAttribute를 사용하면 자동 형변환 처리를 해줌
        System.out.println(visitTime);

        List<DomainObj> domainObjs = new ArrayList<>();
        DomainObj obj = new DomainObj();
        obj.setName("heesu");
        domainObjs.add(obj);
        model.addAttribute("domainList", domainObjs);

        return "/domain/list";
    }

    /**
     * @ModelAttribute 를 사용하면 요청 파라미터를 각각 받을 필요없이 복합객체에 매핑해서 가져올 수 있음
     *  - 복합객체에 바인딩 시 실패하면 400 바인딩에러를 내림 , BindingResult로 익셉션 핸들링 가능
     *  - 바인딩 이후 검증작업은 @Valid를 통해서 처리 가능, 검증실패시 BindingResult에 결과를 담아줌
     *
     * @Validated 는 @Valid를 포함하면서 특정 validation group에 대한 체크를 적용할 수 있다.
     */
    @PostMapping("/objs/model")
    @ResponseBody
    public DomainObj objModelAttribute(@Validated @ModelAttribute DomainObj reqObj, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println("####################");
            bindingResult.getAllErrors().forEach(err -> {
                System.out.println(err.toString());
            });
        }

        return reqObj;
    }

}