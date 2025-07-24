package com.fiap.techchallenge.infrastructure.validation;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public final class CreateAddressValidator {

    private static final Pattern CEP_REGEX = Pattern.compile("^\\d{5}-\\d{3}$");

    public void validate(CreateAddress request) {
        if (request == null) {
            throw new IllegalArgumentException("Objeto de requisição de endereço não pode ser nulo.");
        }

        if (Strings.isBlank(request.getPublicPlace()) || Strings.isBlank(request.getNumber())
                || Strings.isBlank(request.getCity()) || Strings.isBlank(request.getPostalCode())) {
            throw new IllegalArgumentException(
                    "Campos obrigatórios de endereço devem ser preenchidos: rua, número, cidade e CEP.");
        }

        if (!CEP_REGEX.matcher(request.getPostalCode()).matches()) {
            throw new IllegalArgumentException("CEP inválido. Formato esperado: NNNNN-NNN");
        }
    }
}
