package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.services.user.AddressUserUseCase;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.AddressUserApiMapper;
import com.fiap.techchallenge.model.AddressResponse;
import com.fiap.techchallenge.model.CreateAddressRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AddressUserApiImplTest {

    @Mock
    private AddressUserUseCase addressUserUseCase;

    @InjectMocks
    private AddressUserApiImpl addressUserApi;

    private UUID userId;
    private CreateAddressRequest createAddressRequest;
    private Address domainAddress;
    private AddressResponse expectedResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        // Preenche o request DTO
        createAddressRequest = new CreateAddressRequest();
        createAddressRequest.setPublicPlace("Rua ABC");
        createAddressRequest.setNumber("123");
        createAddressRequest.setCity("São Paulo");
        createAddressRequest.setPostalCode("01234-567");

        // Preenche o objeto de domínio simulado
        domainAddress = new Address();
        domainAddress.setId(userId);
        domainAddress.setPublicPlace("Rua ABC");
        domainAddress.setNumber("123");
        domainAddress.setCity("São Paulo");
        domainAddress.setPostalCode("01234-567");

        // Gera a resposta esperada via mapper real
        expectedResponse = AddressUserApiMapper.INSTANCE.mapToAddressResponse(domainAddress);

        // Garante que não há autenticação prévia
        SecurityContextHolder.clearContext();
    }
}
