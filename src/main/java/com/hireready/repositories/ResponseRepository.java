package com.hireready.repositories;

import com.hireready.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findBySimulationId(Long simulationId);
}
