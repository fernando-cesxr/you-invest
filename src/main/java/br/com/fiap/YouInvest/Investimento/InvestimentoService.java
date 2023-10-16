package br.com.fiap.YouInvest.Investimento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

@Service
public class InvestimentoService {

    @Autowired
    InvestimentoRepository repository;

    public List<Investimento> findAll() {
        return repository.findAll();
    }

    public boolean delete(Long id) {
        var investimento = repository.findById(id);
        if (investimento.isEmpty())
            return false;
        repository.deleteById(id);
        return true;
    }

       public void save(Investimento investimento) {
        repository.save(investimento);
    }
}


