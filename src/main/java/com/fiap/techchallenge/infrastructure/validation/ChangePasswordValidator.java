package com.fiap.techchallenge.infrastructure.validation;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class ChangePasswordValidator {
    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{5,}$");

    public static boolean isValid(String password) {
        return password != null && PASSWORD_REGEX.matcher(password).matches();
    }
}
