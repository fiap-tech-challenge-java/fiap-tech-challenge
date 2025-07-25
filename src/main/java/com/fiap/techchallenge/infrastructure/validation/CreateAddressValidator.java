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
            throw new IllegalArgumentException("Address request object cannot be null.");
        }

        if (Strings.isBlank(request.getPublicPlace()) || Strings.isBlank(request.getNumber())
                || Strings.isBlank(request.getCity()) || Strings.isBlank(request.getPostalCode())) {
            throw new IllegalArgumentException(
                    "Required address fields must be filled: street, number, city and postal code.");
        }

        if (!CEP_REGEX.matcher(request.getPostalCode()).matches()) {
            throw new IllegalArgumentException("Invalid postal code. Expected format: NNNNN-NNN");
        }
    }
}
