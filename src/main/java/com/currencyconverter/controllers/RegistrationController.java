package com.currencyconverter.controllers;

import com.currencyconverter.dto.UserDto;
import com.currencyconverter.services.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder   // для web обработки сообщений. Блокирует нулевые формы
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDto());
        return "register";
    }

    @PostMapping
    public String processRegistrationForm(@Valid @ModelAttribute("userForm") UserDto userForm,
                                          BindingResult bindingResult, Model model) {
        model.addAttribute("userForm", userForm);

        //проверка содержимого полей формы пользователя
        if (bindingResult.hasErrors()) {
            return "register";
        }
        //если нового пользователя нет в базе то заводим его
        boolean existing = userService.existByName(userForm.getUsername());
        if (existing) {
            userForm.setUsername(null);
            model.addAttribute("registrationError", "Пользователь с таким логином уже существует");

            return "register";
        }

        existing = userService.existByEmail(userForm.getEmail());
        if (existing) {
            userForm.setEmail(null);
            model.addAttribute("mailError", "Пользователь с таким email уже существует");
            return "register";
        }
        userService.saveUser(userForm);
        return "solution";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable("code") String activateCode) {
        boolean activated = userService.activateUser(activateCode);
        model.addAttribute("activated", activated);
        return "activate-user";
    }


}

