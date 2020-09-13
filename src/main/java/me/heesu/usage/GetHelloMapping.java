package me.heesu.usage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * 메타 어노테이션 추가
 *  - Retention : 해당 어노테이션 정보를 어느 시점까지 유지하는가
 *  - Target : 해당 어노테이션을 어디에 사용가능한가
 *  - Documented : javadoc 생성 시, 해당 어노테이션을 명세에 포함하는가?
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented

@RequestMapping(method = RequestMethod.GET, value="/hello")
public @interface GetHelloMapping {
}
