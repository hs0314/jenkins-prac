package me.heesu.demospringmvc;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement()
@Data
public class DomainObj {
    private int id;
    private String name;
    private LocalDateTime startTime;
    private int limitEntry;
}
