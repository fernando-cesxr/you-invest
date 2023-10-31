package br.com.fiap.YouInvest.Investimento;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import br.com.fiap.YouInvest.user.User;
import br.com.fiap.YouInvest.user.UserService;

@Service
public class InvestimentoService {

    @Autowired
    InvestimentoRepository repository;

    @Autowired
    UserService userService;

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

    public void decrement(Long id) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("investimento não encontrada");

        var investimento = optional.get();
        if (investimento.getValor_final() == 0)
            throw new RuntimeException("status não pode ser negativo");

        investimento.setValor_inicial(investimento.getValor_inicial() - 10);
        repository.save(investimento);
    }

    public void increment(Long id) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("investimento não encontrada");

        var investimento = optional.get();
        if (investimento.getValor_final() == 100)
            throw new RuntimeException("status não pode maior que 100%");

        investimento.setValor_inicial(investimento.getValor_inicial() + 10);

        if (investimento.getValor_inicial() == 100) {
            var user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.addScore(User.convert(user), investimento.getValor_final());
        }

        repository.save(investimento);
    }

    public void catchinvestimento(Long id, User user) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("investimento não encontrada");

        var investimento = optional.get();
        if (investimento.getUser() != null)
            throw new RuntimeException("investimento já atribuída");

        investimento.setUser(user);
        repository.save(investimento);
    }

    public void dropinvestimento(Long id, User user) {
        var optional = repository.findById(id);
        if (optional.isEmpty())
            throw new RuntimeException("investimento não encontrada");

        var investimento = optional.get();
        if (investimento.getUser() == null)
            throw new RuntimeException("investimento não atribuída");

        if (!investimento.getUser().equals(user))
            throw new RuntimeException("não pode largar investimento de outro usuário");

        investimento.setUser(null);
        repository.save(investimento);
    }
}
