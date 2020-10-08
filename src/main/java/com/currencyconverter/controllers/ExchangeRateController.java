package com.currencyconverter.controllers;

import com.currencyconverter.services.DelegatorService;
import com.currencyconverter.services.ExchangeRateService;
import com.currencyconverter.services.UserServiceImpl;
import com.currencyconverter.viewModel.ViewCurrencies;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        exchangeRateService.isLoaded();
    }

    @RequestMapping
    public String mainPage(Model model) {
        model.addAttribute("standardDate", new Date());
        model.addAttribute("currencies", exchangeRateService.getAllValute());

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String home(Model model) {
        model.addAttribute("standardDate", new Date());
        model.addAttribute("currencies", exchangeRateService.getAllValute());

        return "index";
    }

    @GetMapping("/converter")
    public String amountForm(ViewCurrencies selectedCurrencies, ModelMap model) {
        model.addAttribute("currencies", exchangeRateService.getAllValute());
        model.addAttribute("selectedCurrencies", selectedCurrencies);
        return "converter";
    }

    @PostMapping("/converter")
    public String amountSubmit(@Valid ViewCurrencies selectedCurrencies, ModelMap model, Principal principal) {
        model.put("username", principal.getName());
        model.addAttribute("currencies", exchangeRateService.getAllValute());

        model.addAttribute("selectedCurrencies", selectedCurrencies);

        if (selectedCurrencies.getAmountToConvert() == null) {
            model.addAttribute("amountError", "Поле не может быть пустым. Введите значение!");
            return "converter";
        }
        delegatorService.performCurrencyConversion(selectedCurrencies);
        delegatorService.performAudit(selectedCurrencies, principal.getName());

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
