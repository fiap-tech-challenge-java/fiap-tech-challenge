package com.fiap.techchallenge.domain.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Valida senhas de acordo com a regra: - mínimo de 5 caracteres - ao menos 1 letra maiúscula - ao menos 1 dígito
 */

@Component
public class PasswordValidator {

    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{5,}$");

    public static boolean isValid(String password) {
        return password != null && PASSWORD_REGEX.matcher(password).matches();
    }
}
