package com.barbearia.application.controller;

import com.barbearia.application.model.Barbeiro;
import com.barbearia.application.service.BarbeiroService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    public BarbeiroController(BarbeiroService barbeiroService) {
        this.barbeiroService = barbeiroService;
    }

    @GetMapping
    public List<Barbeiro> listar() {
        return barbeiroService.listarTodos();
    }
}