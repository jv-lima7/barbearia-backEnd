package com.barbearia.application.service;

import com.barbearia.application.model.Barbeiro;
import com.barbearia.application.repository.BarbeiroRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;

    public BarbeiroService(BarbeiroRepository barbeiroRepository) {
        this.barbeiroRepository = barbeiroRepository;
    }

    public List<Barbeiro> listarTodos() {
        return barbeiroRepository.findAll();
    }
}