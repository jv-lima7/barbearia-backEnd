package com.barbearia.application.repository;

import com.barbearia.application.model.Agendamento;
import com.barbearia.application.model.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByBarbeiroAndDataHoraBetween(Barbeiro barbeiro, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByBarbeiroAndDataHora(Barbeiro barbeiro, LocalDateTime dataHora);
}