package com.barbearia.application.service;

import com.barbearia.application.model.Agendamento;
import com.barbearia.application.model.Barbeiro;
import com.barbearia.application.repository.AgendamentoRepository;
import com.barbearia.application.repository.BarbeiroRepository;
import com.barbearia.application.repository.ServicoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, BarbeiroRepository barbeiroRepository, ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
    }

    public List<Agendamento> listarPorBarbeiroEData(Long barbeiroId, LocalDate data) {
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado."));

        LocalDateTime inicio = data.atTime(LocalTime.MIN);
        LocalDateTime fim = data.atTime(LocalTime.MAX);

        return agendamentoRepository.findByBarbeiroAndDataHoraBetween(barbeiro, inicio, fim);
    }

    public Agendamento criar(Long barbeiroId, Long servicoId,
                             String nomeCliente, String telefoneCliente,
                             LocalDateTime dataHora) {

        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado."));

        servicoRepository.findById(servicoId)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado."));

        if (agendamentoRepository.existsByBarbeiroAndDataHora(barbeiro, dataHora))
            throw new IllegalArgumentException("Horário já ocupado.");

        Agendamento agendamento = new Agendamento();
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servicoRepository.findById(servicoId).get());
        agendamento.setNomeCliente(nomeCliente);
        agendamento.setTelefoneCliente(telefoneCliente);
        agendamento.setDataHora(dataHora);

        return agendamentoRepository.save(agendamento);
    }

    public void cancelar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado."));
        agendamentoRepository.delete(agendamento);
    }

    public List<String> listarHorariosOcupados(Long barbeiroId, LocalDate data) {
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado."));

        LocalDateTime inicio = data.atTime(LocalTime.MIN);
        LocalDateTime fim = data.atTime(LocalTime.MAX);

        return agendamentoRepository.findByBarbeiroAndDataHoraBetween(barbeiro, inicio, fim)
                .stream()
                .map(a -> a.getDataHora().toLocalTime().toString().substring(0, 5))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Agendamento> listarPorBarbeiroEMes(Long barbeiroId, int ano, int mes) {
        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado."));

        LocalDateTime inicio = LocalDateTime.of(ano, mes, 1, 0, 0);
        LocalDateTime fim = inicio.plusMonths(1).minusSeconds(1);

        return agendamentoRepository.findByBarbeiroAndDataHoraBetween(barbeiro, inicio, fim);
    }
}