package me.heesu.demospringmvc.repository;

import me.heesu.demospringmvc.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
