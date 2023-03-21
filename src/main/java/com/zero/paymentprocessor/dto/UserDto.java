package com.zero.paymentprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @Size(min = 8, max = 32, message = "password must be minimum 8 maximum 32 characters long")
    private String password;
    @Pattern(regexp = "^\\+998.*", message = "Wrong country code")
    @Size(min = 13, max = 13, message = "Wrong number")
    private String phoneNumber;
    @NotEmpty(message = "firstName, cannot be empty")
    private String firstName;
    @NotEmpty(message = "lastName, cannot be empty")
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
    }
}
