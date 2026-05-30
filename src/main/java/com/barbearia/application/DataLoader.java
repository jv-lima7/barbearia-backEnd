package com.barbearia.application;

import com.barbearia.application.model.Barbeiro;
import com.barbearia.application.model.Servico;
import com.barbearia.application.repository.BarbeiroRepository;
import com.barbearia.application.repository.ServicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;

    public DataLoader(BarbeiroRepository barbeiroRepository,
                      ServicoRepository servicoRepository) {
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
    }

    @Override
    public void run(String... args) {
        if (barbeiroRepository.count() == 0) {
            Barbeiro b1 = new Barbeiro();
            b1.setNome("Valter");
            barbeiroRepository.save(b1);

            Barbeiro b2 = new Barbeiro();
            b2.setNome("Lipe");
            barbeiroRepository.save(b2);
            System.out.println("✔ Barbeiros carregados!");
        }

        if (servicoRepository.count() == 0) {
            servicoRepository.save(new Servico("Corte", 30.00));
            servicoRepository.save(new Servico("Barba", 20.00));
            servicoRepository.save(new Servico("Corte + Barba", 50.00));
            System.out.println("✔ Serviços carregados!");
        }
    }
}