package com.currencyconverter.controllers;

import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.services.DelegatorService;
import com.currencyconverter.services.ExchangeRateService;
import com.currencyconverter.services.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@Controller
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;
    private DelegatorService delegatorService;
    private UserServiceImpl userService;

    public ExchangeRateController(ExchangeRateService exchangeRateService, DelegatorService delegatorService, UserServiceImpl userService) throws IOException {
        this.exchangeRateService = exchangeRateService;
        this.delegatorService = delegatorService;
        this.userService = userService;
        exchangeRateService.processingHttpRequest();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @RequestMapping({"/", "/index"})
    @GetMapping({"/","/index"})
    public String index(Model model) {
        model.addAttribute("standardDate", new Date());
//        model.addAttribute("currencies", exchangeRateService.getAllValute(LocalDate.now()));
        model.addAttribute("currencies", exchangeRateService.getAll());

        return "index";
    }

    @GetMapping("/converter")
//    @ResponseBody
    public String amountForm(ValuteDto valuteDto, Model model, Principal principal) {
//        if (!userService.findByUsername(principal.getName()).isEnabled()) {
//            return "User Not Enabled!";
//        }
//        model.addAttribute("currencies", exchangeRateService.getAllValute(LocalDate.now()));
        model.addAttribute("currencies", exchangeRateService.getAll());
        model.addAttribute("selectedCurrencies", valuteDto);
        return "converter";
    }

    @PostMapping("/converter")
    public String amountSubmit(@Valid ValuteDto valuteDto, Model model, Principal principal) {

//        model.addAttribute("currencies", exchangeRateService.getAllValute(LocalDate.now()));
        model.addAttribute("currencies", exchangeRateService.getAll());
        model.addAttribute("selectedCurrencies", valuteDto);


        if (valuteDto.getAmountToConvert() == null) {
            model.addAttribute("amountError", "Поле не может быть пустым. Введите значение!");
            return "converter";
        }
        delegatorService.performCurrencyConversion(valuteDto);
        delegatorService.performAudit(valuteDto, principal);

        return "converter";
    }

    private String getFormattedQueryHistory(Principal principal) {
        return userService.getAuditHistoryForUser(principal.getName()).getFormattedString();
    }

    @GetMapping(value="/history")
    public String requestHistory(ModelMap modelMap, Model model, Principal principal) {
        modelMap.put("name", principal.getName());
        model.addAttribute("histories", getFormattedQueryHistory(principal));
        return "history";
    }

}
