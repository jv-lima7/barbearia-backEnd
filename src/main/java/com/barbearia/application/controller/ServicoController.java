package com.barbearia.application.controller;

import com.barbearia.application.model.Servico;
import com.barbearia.application.service.ServicoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public List<Servico> listar() {
        return servicoService.listarTodos();
    }
}