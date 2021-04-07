package me.heesu.usage;

import me.heesu.demospringmvc.DomainObj;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HandlerMethodController {
    /**
     * uri 패턴에서 핸들러에 값 매핑
     * - @PathVariable, @MatrixVariable (추가적으로 pathMatch 관련 config 변경이 필요
     */
    @GetMapping(value = "/objs/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DomainObj getObj(@PathVariable Integer id) { // pathVariable 이 필수로 들어와야 함
        DomainObj obj = new DomainObj();
        obj.setId(id);

        return obj;
    }

    /**
     * 요청 매개변수 (?쿼리스트링 혹은 요청 본문에 form으로 받는 파라미터)
     * - @RequestParams
     */
    @PostMapping(value = "/objs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public DomainObj getObjPost(@RequestParam String name,
                                @RequestParam(required = false) Integer limitEntry) {
        DomainObj obj = new DomainObj();
        obj.setName(name);
        obj.setLimitEntry(limitEntry);
        return obj;
    }

    /**
     * form submit 핸들링
     * -> html form 서브밋 된 데이터를 @RequestParam으로 핸들러에서 받을 수 있음
     * -> 추가적으로 @ModelAttribute를 통해서 특정 객체에 전달받은 파라미터를 바인딩 시킬 수 있음
     *     -바인딩 시, 타입 문제등은 400 바인딩에러를 내림 , BindingResult로 익셉션 핸들링 가능
     */
    @GetMapping("/objs/form")
    public String objForm(Model model) {
        model.addAttribute("domain", new DomainObj());

        return "/domain/form";
    }
}