package com.fiap.techchallenge.infrastructure.validation;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.domain.exceptions.InvalidPasswordPatternException;
import com.fiap.techchallenge.domain.exceptions.MissingRequiredFieldsException;
import com.fiap.techchallenge.domain.exceptions.PasswordsDoNotMatchException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class ChangePasswordValidator {
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{5,}$");

    public static boolean isValid(ChangePassword changePassword) {
        UUID uuid = changePassword.getIdUser();
        String newPassword = changePassword.getNewPassword();
        String confirmPassword = changePassword.getConfirmPassword();

        if (uuid == null || Strings.isBlank(newPassword) || Strings.isBlank(confirmPassword)) {
            throw new MissingRequiredFieldsException();
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (!PASSWORD_REGEX.matcher(newPassword).matches()) {
            throw new InvalidPasswordPatternException();
        }

        return true;
    }

}
