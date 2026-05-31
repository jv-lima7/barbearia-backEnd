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

        if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        nomeCliente = nomeCliente.trim();

        if (nomeCliente.length() < 3 || nomeCliente.length() > 80) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 80 caracteres.");
        }

        if (!nomeCliente.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Nome deve conter apenas letras e espaços.");
        }

        if (telefoneCliente == null) {
            throw new IllegalArgumentException("Telefone é obrigatório.");
        }

        String telefoneLimpo = telefoneCliente.replaceAll("\\D", "");

        if (!telefoneLimpo.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("Telefone deve conter 11 números.");
        }

        if (dataHora == null) {
            throw new IllegalArgumentException("Data e horário são obrigatórios.");
        }

        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível agendar em data passada.");
        }

        if (dataHora.getMinute() != 0 && dataHora.getMinute() != 30) {
            throw new IllegalArgumentException("Horário inválido.");
        }

        LocalTime hora = dataHora.toLocalTime();

        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(17, 0))) {
            throw new IllegalArgumentException("Horário fora do expediente.");
        }

        if (dataHora.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Não é possível agendar aos domingos.");
        }

        Barbeiro barbeiro = barbeiroRepository.findById(barbeiroId)
                .orElseThrow(() -> new IllegalArgumentException("Barbeiro não encontrado."));

        var servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado."));

        if (agendamentoRepository.existsByBarbeiroAndDataHora(barbeiro, dataHora)) {
            throw new IllegalArgumentException("Horário já ocupado.");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
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