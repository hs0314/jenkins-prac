package me.heesu.usage;

import me.heesu.demospringmvc.domain.DomainObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("domain") // 해당 클래스안에서 모델에 들어가는 "domain" attribute를 세션에 셋팅
public class HandlerMethodController {

    @Autowired
    ResourceLoader resourceLoader; // file download

    @InitBinder
    public void initDomainBinder(WebDataBinder webDataBinder){
        webDataBinder.setDisallowedFields("id");
    }


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
    @GetMapping("/domain/form")
    public String domainForm(Model model, HttpSession httpSession) {
        DomainObj domain = new DomainObj();
        domain.setLimitEntry(50);

        model.addAttribute("domain", domain);
        //httpSession.setAttribute("domain", domain);

        return "/domain/form";
    }

    /**
     * domain list 조회
     */
    @GetMapping("/domain/list")
    public String getDomainList(Model model,
                                @SessionAttribute("visitTime")LocalDateTime visitTime){
        System.out.println(visitTime); // @SessionAttribute 확인용
        List<DomainObj> domainObjs = new ArrayList<>();

        DomainObj domain = new DomainObj();
        domain = (DomainObj) model.asMap().get("newDomain");

        DomainObj defaultDomain = new DomainObj();
        defaultDomain.setName("test");
        defaultDomain.setLimitEntry(5);

        domainObjs.add(defaultDomain);

        //FlashAttributes 를 통해서 session에서 셋팅된 domain이 없으면 add하지 않음
        if(domain != null) {
            domainObjs.add(domain);
        }
        model.addAttribute("domainList", domainObjs);

        return "/domain/list";
    }

    /**
     * domain 생성
     *  - 브라우저 리프레시를 통한 중복 폼 서브밋 방지를 위해서 POST요청 이후 Redirect해서 GET으로 list 조회
     */
    @PostMapping("/domain")
    public String createDomain(@Validated @ModelAttribute("domain") DomainObj domain,
                            BindingResult bindingResult,
                            Model model){

        if(bindingResult.hasErrors()){
            return "/domain/form-name";
        }

        // 메서드내애서 아규먼트로 SessionStatus를 받아서 특정시점에 세션만료 등 처리 가능
        //sessionStatus.setComplete();

        List<DomainObj> domainObjs = new ArrayList<>();
        domainObjs.add(domain);
        model.addAttribute("domainList", domainObjs);

        return "redirect:/domain/list"; // 요청처리를 GET "/domain/list"에 위임
    }



    /**
     * 멀티폼 서브밋 처리를 통한 session 사용
     */
    @GetMapping("/domain/form/name")
    public String objNameForm(Model model) {
        DomainObj domain = new DomainObj();
        model.addAttribute("domain", domain); // 세션에 해당 model 객체가 들어감
        return "/domain/form-name";
    }

    @GetMapping("/domain/form/limit")
    public String objLimitForm(@ModelAttribute("domain") DomainObj domain, Model model) {
        model.addAttribute("domain", domain); // 세션에 있는 domain객체를 @ModelAttribute를 통해서 꺼내고 model에 셋팅
        return "/domain/form-limit";
    }

    @PostMapping("/domain/form/name")
    public String objectFormNameSubmit(@Validated @ModelAttribute("domain") DomainObj domain,
                            BindingResult bindingResult,
                            Model model,
                            SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "/domain/form-name";
        }
        return "redirect:/domain/form/limit";
    }
    @PostMapping("/domain/form/limit")
    public String objectFormLimitSubmit(@Validated @ModelAttribute("domain") DomainObj domain,
                                        BindingResult bindingResult,
                                        SessionStatus sessionStatus,
                                        RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            return "/domain/form-limit";
        }
        // 메서드내애서 아규먼트로 SessionStatus를 받아서 특정시점에 세션만료 등 처리 가능
        sessionStatus.setComplete();

        //RedirectAttributes를 이용해서 redirect되는 요청 url에 쿼리 파라미터로 특정 primitive 값을 넘김
        // 쿼리파라미터로 넘겨야하기 때문에 문자열 변환이 가능해야하기 때문 (복합객체 불가)
        //redirectAttributes.addAttribute("name", domain.getName());
        //redirectAttributes.addAttribute("limit", domain.getLimitEntry());

        // Flash Attribute - 복합객체 저장가능
        /*
         redirect전에 HTTP session에 해당 값을 저장, redirect요청 이후 즉시 제거
          + 쿼리 파라미터가 아니기 때문에 urld에 값 노출 X
          + 복합객체 저장 가능

          => 사용하는 메서드의 Model객체에서 받을 수 있음
         */
        redirectAttributes.addFlashAttribute("newDomain", domain);

        return "redirect:/domain/list";
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

    /**
     * Multipart 관련 처리
     */
    @GetMapping("/file")
    public String fileUploadForm(Model model){
         return "files/index";
    }
    @PostMapping("file")
    public String fileUpload(@RequestParam MultipartFile file,
                             RedirectAttributes redirectAttributes){

        String msg = file.getOriginalFilename() + " is uploaded";

        // falshAttribute로 넣음으로써 msg가 세션에 담기고 redirect되는 페이지 렌더 이후 삭제
        redirectAttributes.addFlashAttribute("msg", msg);

        return "redirect:/file";
    }
    @GetMapping("/file/{filename}")
    //@ResponseBody -> responseEntity 자체가 응답본문이기 때문에 해당 어노테이션이 없어도 됌
    public ResponseEntity<Resource> fileDownload(@PathVariable String filename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:"+filename);
        File file = resource.getFile();

        String mediaType = Files.probeContentType(file.toPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mediaType)
                .header(HttpHeaders.CONTENT_LENGTH, file.length() + "")
                .body(resource);
    }

}