package me.heesu.demospringmvc.formatter;

import me.heesu.demospringmvc.domain.Person;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

//@Component
public class PersonFormatter implements Formatter<Person> {

    @Override
    public Person parse(String s, Locale locale) throws ParseException {
        // 문자열 -> 객체 매핑
        Person p = new Person();
        p.setName(s);
        return p;
    }

    @Override
    public String print(Person person, Locale locale) {
        // 객체 -> 문자열 매핑
        return person.getName();
    }
}
