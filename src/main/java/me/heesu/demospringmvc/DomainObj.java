package me.heesu.demospringmvc;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement()
public class DomainObj {
    private String name;
    private LocalDateTime startTime;
    private int limitEntry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getLimitEntry() {
        return limitEntry;
    }

    public void setLimitEntry(int limitEntry) {
        this.limitEntry = limitEntry;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", limitEntry=" + limitEntry +
                '}';
    }
}
