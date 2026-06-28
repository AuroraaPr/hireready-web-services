package com.hireready.serviceimpl;

import com.hireready.dtos.CareerRequestDTO;
import com.hireready.dtos.CareerResponseDTO;
import com.hireready.entities.Career;
import com.hireready.enums.AuthorityRole;
import com.hireready.exceptions.DuplicateResourceException;
import com.hireready.exceptions.ResourceNotFoundException;
import com.hireready.exceptions.ValidationException;
import com.hireready.repositories.CareerRepository;
import com.hireready.services.CareerService;
import com.hireready.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CareerServiceImpl implements CareerService {
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    UserService userService;

    // US01
    @Override
    public Career findById(Long id) {
        Career career = careerRepository.findById(id).orElse(null);
        if (career == null) {
            throw new ResourceNotFoundException("Career id: " + id + " not found");
        }
        return career;
    }

    // US16
    @Override
    public List<CareerResponseDTO> listAll(Long adminUserId) {
        List<CareerResponseDTO> result = new ArrayList<>();
        for (Career c : careerRepository.findAll()) {
            result.add(new CareerResponseDTO(c.getId(), c.getName()));
        }
        return result;
    }

    // US16
    @Override
    public CareerResponseDTO create(Long adminUserId, CareerRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Career name is required");
        }
        String name = request.getName().trim();
        if (careerRepository.findByName(name) != null) {
            throw new DuplicateResourceException("Career name already exists: " + name);
        }
        Career c =
                new Career(null, name, null, null);
        c = careerRepository.save(c);
        return new CareerResponseDTO(c.getId(), c.getName());
    }

    // US16
    @Override
    public CareerResponseDTO update(Long adminUserId, Long careerId, CareerRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Career name is required");
        }
        Career c = findById(careerId);
        String newName = request.getName().trim();
        Career other = careerRepository.findByName(newName);
        if (other != null && !other.getId().equals(c.getId())) {
            throw new DuplicateResourceException("Career name already exists: " + newName);
        }
        c.setName(newName);
        c = careerRepository.save(c);
        return new CareerResponseDTO(c.getId(), c.getName());
    }
}
