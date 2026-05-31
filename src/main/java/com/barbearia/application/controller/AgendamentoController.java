package com.barbearia.application.controller;

import com.barbearia.application.model.Agendamento;
import com.barbearia.application.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listar(
            @RequestParam Long barbeiroId,
            @RequestParam LocalDate data) {
        return ResponseEntity.ok(agendamentoService.listarPorBarbeiroEData(barbeiroId, data));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> body) {
        try {
            Long barbeiroId = Long.valueOf(body.get("barbeiroId").toString());
            Long servicoId = Long.valueOf(body.get("servicoId").toString());
            String nomeCliente = (String) body.get("nomeCliente");
            String telefoneCliente = (String) body.get("telefoneCliente");
            LocalDateTime dataHora = LocalDateTime.parse((String) body.get("dataHora"));

            Agendamento agendamento = agendamentoService.criar(
                    barbeiroId, servicoId, nomeCliente, telefoneCliente, dataHora);

            return ResponseEntity.ok(agendamento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            agendamentoService.cancelar(id);
            return ResponseEntity.ok("Agendamento cancelado.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/horarios-ocupados")
    public ResponseEntity<List<String>> horariosOcupados(
            @RequestParam Long barbeiroId,
            @RequestParam LocalDate data) {
        List<String> horarios = agendamentoService.listarHorariosOcupados(barbeiroId, data);
        return ResponseEntity.ok(horarios);
    }
}