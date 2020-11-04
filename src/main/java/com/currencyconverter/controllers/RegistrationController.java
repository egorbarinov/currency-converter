package com.currencyconverter.controllers;

import com.currencyconverter.dto.UserDto;
import com.currencyconverter.model.User;
import com.currencyconverter.services.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder   // для web обработки сообщений. Блокирует нулевые формы
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDto());

        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@Valid @ModelAttribute("userForm") UserDto userForm,
                                          BindingResult bindingResult, Model model) {

        model.addAttribute("userForm", userForm);

        //проверка содержимого полей формы пользователя
        if (bindingResult.hasErrors()) {
            return "register";
        }
        //если нового пользователя нет в базе то заводим его
        User existing = userService.findByUsername(userForm.getUsername());
        if (existing != null) {
            userForm.setUsername(null);
            model.addAttribute("registrationError", "Пользователь с таким логином уже существует");

            return "register";
        }

        User existingByEmail = userService.findByUserEmail(userForm.getEmail());
        if (existingByEmail != null) {
            userForm.setEmail(null);
            model.addAttribute("mailError", "Пользователь с таким email уже существует");
            return "register";
        }

        if (userForm.getUsername() == null || userForm.getPassword() == null || userForm.getPasswordConfirm() == null || userForm.getEmail() == null ) {
            model.addAttribute("errorForm", "Все поля должны быть заполнены");
            return "register";
        }

        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            userForm.setPassword(null);
            userForm.setPasswordConfirm(null);
            model.addAttribute("passwordError", "Пароли не совпадают!");
            return "register";
        }

        userService.saveUser(userForm);
        return "solution";

    }

}
