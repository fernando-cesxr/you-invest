package br.com.fiap.YouInvest.Investimento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.config.Task;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/investimento")
public class InvestimentoController {

    @Autowired
    InvestimentoService service;

    @Autowired
    MessageSource messages;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user){
        model.addAttribute("username", user.getAttribute("name"));
        model.addAttribute("avatar_url", user.getAttribute("avatar_url"));
        model.addAttribute("investimento", service.findAll());
        return "investimento/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        if (service.delete(id)) {
            redirect.addFlashAttribute("success", getMessage("task.delete.success") );
        } else {
            redirect.addFlashAttribute("error", getMessage("task.notfound")) ;
        }
        return "redirect:/investimento";
    }

    
    @GetMapping("new")
    public String form(Investimento investimento){
        return "investimento/form";
    }

    @PostMapping
    public String create(@Valid Investimento investimento, BindingResult result, RedirectAttributes redirect){
        if (result.hasErrors()) return "investimento/form";

        service.save(investimento);
        redirect.addFlashAttribute("success", "Tarefa cadastrada com sucesso");
        return "redirect:/investimento";
    }


    private String getMessage(String code){
        return messages.getMessage(code, null, LocaleContextHolder.getLocale());
    }

}
