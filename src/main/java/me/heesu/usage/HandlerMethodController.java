package me.heesu.usage;

import me.heesu.demospringmvc.DomainObj;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
                                @RequestParam(required = false) Integer limit) {
        DomainObj obj = new DomainObj();
        obj.setName(name);
        obj.setLimitEntry(limit);
        return obj;
    }
}