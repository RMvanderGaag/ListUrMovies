package com.avans.listurmovies.businesslogic.validation;

public class ValidationTools {
    public boolean isUsernameFieldEmpty(String username) {
        return username.isEmpty();
    }

    public boolean isPasswordFieldEmpty(String password) {
        return password.isEmpty();
    }
}
