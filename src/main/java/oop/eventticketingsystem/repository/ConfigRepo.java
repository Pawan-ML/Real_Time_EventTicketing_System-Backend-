package oop.eventticketingsystem.repository;

import oop.eventticketingsystem.model.configurations.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepo extends JpaRepository<Configuration, Integer> {
}
