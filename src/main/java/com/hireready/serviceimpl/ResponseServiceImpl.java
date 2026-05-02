package com.hireready.serviceimpl;

import com.hireready.entities.Response;
import com.hireready.repositories.ResponseRepository;
import com.hireready.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {
    @Autowired
    private ResponseRepository responseRepository;

    @Override
    public List<Response> findBySimulationId(Long id){
        return responseRepository.findBySimulationId(id);
    };
}
