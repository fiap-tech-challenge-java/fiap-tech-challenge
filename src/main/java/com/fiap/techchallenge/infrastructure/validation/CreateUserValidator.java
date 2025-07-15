package com.fiap.techchallenge.infrastructure.validation;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.domain.exceptions.EmailAlreadyExistsException;
import com.fiap.techchallenge.domain.exceptions.InvalidPasswordPatternException;
import com.fiap.techchallenge.domain.exceptions.UsernameAlreadyExistsException;
import com.fiap.techchallenge.domain.exceptions.InvalidEmailPatternException;
import com.fiap.techchallenge.domain.utils.CpfValidator;
import com.fiap.techchallenge.domain.utils.PasswordValidator;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public final class CreateUserValidator {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE);

    private final UserJpaRepository userJpaRepository;

    public CreateUserValidator(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public void validate(CreateUser request) {

        if (request == null) {
            throw new IllegalArgumentException("Objeto de requisição não pode ser nulo.");
        }

        if (Strings.isBlank(request.getName()) || Strings.isBlank(request.getEmail())
                || Strings.isBlank(request.getCpf()) || Strings.isBlank(request.getLogin())
                || Strings.isBlank(request.getPassword())) {

            throw new IllegalArgumentException("Todos os campos obrigatórios devem ser preenchidos.");
        }

        if (!EMAIL_REGEX.matcher(request.getEmail()).matches()) {
            throw new InvalidEmailPatternException("E‑mail inválido.");
        }

        if (!PasswordValidator.isValid(request.getPassword())) {
            throw new InvalidPasswordPatternException();
        }

        CpfValidator.validate(request.getCpf());

        userJpaRepository.findByEmailAndActiveTrue(request.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException(request.getEmail());
        });

        userJpaRepository.findByLogin(request.getLogin()).ifPresent(u -> {
            throw new UsernameAlreadyExistsException(request.getLogin());
        });
    }

}
