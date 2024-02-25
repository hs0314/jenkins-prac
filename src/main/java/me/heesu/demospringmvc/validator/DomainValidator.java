package me.heesu.demospringmvc.validator;

import me.heesu.demospringmvc.domain.DomainObj;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DomainValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        // 어떠한 도메인 클래스에 대한 validation을 지원하는지 판단
        return DomainObj.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DomainObj domain = (DomainObj) o; // validation 대상 객체

        // case1
        if(domain.getName().equalsIgnoreCase("aaa")){
            errors.rejectValue("name", "wrongValue", "the value is not allowed.");
        }
    }
}
