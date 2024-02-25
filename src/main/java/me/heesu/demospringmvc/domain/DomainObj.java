package me.heesu.demospringmvc.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.LocalDateTime;

@XmlRootElement()
@Data
public class DomainObj {

    public interface ValidateLimit {}
    public interface ValidateName {}

    private int id;

    @NotBlank // (groups = ValidateName.class)
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // yyyy-mm-dd 문자열을 자동으로 Date타입으로 변환
    private LocalDate startDate;

    @Min(value = 0)
    private int limitEntry;
}
