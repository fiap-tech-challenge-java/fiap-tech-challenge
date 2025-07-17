package com.fiap.techchallenge.domain.utils;

import com.fiap.techchallenge.domain.exceptions.InvalidCpfException;

/**
 * Valida CPF de acordo com os padrões
 */
public class CpfValidator {

    public static void validate(String cpf) {
        if (cpf == null)
            throw new InvalidCpfException();

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}"))
            throw new InvalidCpfException();

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += (cpf.charAt(i) - '0') * (10 - i);
            }

            int firstCheckDigit = 11 - (sum % 11);
            if (firstCheckDigit >= 10)
                firstCheckDigit = 0;

            if (firstCheckDigit != (cpf.charAt(9) - '0'))
                throw new InvalidCpfException();

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += (cpf.charAt(i) - '0') * (11 - i);
            }

            int secondCheckDigit = 11 - (sum % 11);
            if (secondCheckDigit >= 10)
                secondCheckDigit = 0;

            if (secondCheckDigit != (cpf.charAt(10) - '0'))
                throw new InvalidCpfException();

        } catch (NumberFormatException e) {
            throw new InvalidCpfException();
        }
    }

    private CpfValidator() {
    }
}
