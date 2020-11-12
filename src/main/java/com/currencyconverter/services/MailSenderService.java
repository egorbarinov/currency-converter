package com.currencyconverter.services;

import com.currencyconverter.model.User;

public interface MailSenderService {
    void sendActivateCode(User user);
}
