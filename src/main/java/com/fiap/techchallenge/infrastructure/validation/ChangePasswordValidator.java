package com.fiap.techchallenge.infrastructure.validation;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.domain.exceptions.InvalidPasswordPatternException;
import com.fiap.techchallenge.domain.exceptions.InvalidPreviousPasswordException;
import com.fiap.techchallenge.domain.exceptions.MissingRequiredFieldsException;
import com.fiap.techchallenge.domain.exceptions.PasswordsDoNotMatchException;
import com.fiap.techchallenge.domain.utils.PasswordValidator;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChangePasswordValidator {

    private final PasswordEncoder passwordEncoder;

    public ChangePasswordValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void isValid(ChangePassword changePassword, String storedEncodedPassword) {
        UUID uuid = changePassword.getId();
        String lastPassword = changePassword.getLastPassword();
        String newPassword = changePassword.getNewPassword();
        String confirmPassword = changePassword.getConfirmPassword();

        if (uuid == null) {
            throw new InvalidPreviousPasswordException();
        }
        if (Strings.isBlank(newPassword) || Strings.isBlank(lastPassword)) {
            throw new MissingRequiredFieldsException();
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (!passwordEncoder.matches(lastPassword, storedEncodedPassword)) {
            throw new InvalidPreviousPasswordException();
        }

        if (!PasswordValidator.isValid(newPassword)) {
            throw new InvalidPasswordPatternException();
        }
    }
}
