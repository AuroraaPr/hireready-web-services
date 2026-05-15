package com.hireready.services;

import com.hireready.dtos.CareerRequestDTO;
import com.hireready.dtos.CareerResponseDTO;
import com.hireready.entities.Career;

import java.util.List;

public interface CareerService {
    public Career findById(Long id);
    public List<CareerResponseDTO> listAll(Long adminUserId);
    public CareerResponseDTO create(Long adminUserId, CareerRequestDTO request);
    public CareerResponseDTO update(Long adminUserId, Long careerId, CareerRequestDTO req);
}
