package com.zero.paymentprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "password cannot be empty")
    private String password;
}
