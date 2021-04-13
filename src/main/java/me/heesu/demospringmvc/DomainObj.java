package me.heesu.demospringmvc;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement()
@Data
public class DomainObj {

    public interface ValidateLimit {}
    public interface ValidateName {}

    private int id;

    @NotBlank // (groups = ValidateName.class)
    private String name;
    private LocalDateTime startTime;

    @Min(value = 0)
    private int limitEntry;
}
