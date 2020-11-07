package com.currencyconverter.controllers;

import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.services.DelegatorService;
import com.currencyconverter.services.ExchangeRateService;
import com.currencyconverter.services.UserServiceImpl;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;
    private DelegatorService delegatorService;
    private UserServiceImpl userService;
    private LocalDate date = LocalDate.now();

    public ExchangeRateController(ExchangeRateService exchangeRateService, DelegatorService delegatorService, UserServiceImpl userService) throws IOException {
        this.exchangeRateService = exchangeRateService;
        this.delegatorService = delegatorService;
        this.userService = userService;
        exchangeRateService.processingHttpRequest();
    }

    @InitBinder   // для web обработки сообщений. WebDataBinder блокирует нулевые формы
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


//    @GetMapping({"/","/index"})
//    public String index(Model model) {
//
////        model.addAttribute("currencies", exchangeRateService.getAllValute(LocalDate.now()));
////        model.addAttribute("currencies", exchangeRateService.getAll());
//        model.addAttribute("standardDate", LocalDateTime.now());
//        model.addAttribute("currencies", exchangeRateService.getAll(LocalDate.now()));
//        return "index";
//    }

    // http://localhost:8189/index/daily/?date_req=2020-11-05
    // http://localhost:8189/index/daily/?date_req=06.11.2020  не работает потому как в базе хранится в виде 2020-11-06
    @GetMapping({"/","/index"})
    public String index(Model model, @RequestParam(required = false, name = "date_req") String date_req) {


        model.addAttribute("standardDate", LocalDateTime.now());
        if (date_req == null) {
            date_req = LocalDate.now().toString();
            model.addAttribute("requestDate", date_req);
            model.addAttribute("date", date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.forLanguageTag("ru"))));
            model.addAttribute("currencies", exchangeRateService.getAll(date));
        }
        else {
            LocalDate localDate = LocalDate.parse(date_req);
            model.addAttribute("date", localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.forLanguageTag("ru"))));
            model.addAttribute("requestDate", date_req);
            try {
                model.addAttribute("currencies", exchangeRateService.getAll(localDate));
            } catch (NullPointerException ex) {
                model.addAttribute("responseError", "Данные по курсам валют на запрашиваемую дату отсутствуют в базе данных.");
            }
        }
        return "index";
    }

    @GetMapping("/converter")
    public String amountForm(ValuteDto valuteDto, Model model, Principal principal) {
        model.addAttribute("name", principal.getName());
        if (!userService.findByUsername(principal.getName()).isEnabled()) {
            return "disabled";
        }
        model.addAttribute("currencies", exchangeRateService.getAll(date));
        model.addAttribute("selectedCurrencies", valuteDto);
        return "converter";
    }

    @PostMapping("/converter")
    public String amountSubmit(@Valid ValuteDto valuteDto, Model model, Principal principal) {
        model.addAttribute("currencies", exchangeRateService.getAll());
        model.addAttribute("selectedCurrencies", valuteDto);


        if (valuteDto.getAmountToConvert() == null) {
            model.addAttribute("amountError", "Поле не может быть пустым. Введите значение!");
            return "converter";
        }
        delegatorService.performCurrencyConversion(valuteDto, date);
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
