package com.hireready.repositories;

import com.hireready.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    public List<Response> findBySimulation_Id(Long simulationId);
    public boolean existsByIdAndSimulation_Id(Long id, Long simulationId);
}
