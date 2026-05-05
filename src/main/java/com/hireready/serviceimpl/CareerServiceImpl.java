package com.hireready.serviceimpl;

import com.hireready.entities.Career;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.repositories.CareerRepository;
import com.hireready.services.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CareerServiceImpl implements CareerService {
    @Autowired
    CareerRepository careerRepository;

    // US01
    @Override
    public Career findById(Long id) {
        Career career = careerRepository.findById(id).orElse(null);
        if (career == null) {
            throw new ResourceNotFoundException("Career id: " + id + " not found");
        }
        return career;
    }
}
