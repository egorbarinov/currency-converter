package com.currencyconverter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotBlank()
    @Size(min = 4, message = "username length must be greater than 4 symbols")
    private String username;
    @NotBlank()
    @Size(min = 5, message = "password length must be greater than 5 symbols")
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String password;
//    @NotBlank()
//    @NotNull(message = "is required")
//    @Size(min = 5, message = "is required")
    private String passwordConfirm;
    @Email(message = "Email should be valid")
    private String email;
    private boolean activated;

}
