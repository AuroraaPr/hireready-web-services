package com.hireready.services;

import com.hireready.dtos.CareerRequestDTO;
import com.hireready.dtos.CareerResponseDTO;
import com.hireready.entities.Career;

import java.util.List;

public interface CareerService {
    public Career findById(Long id);
    public List<CareerResponseDTO> listAll();
    public CareerResponseDTO create(CareerRequestDTO request);
    public CareerResponseDTO update(Long careerId, CareerRequestDTO req);
}
