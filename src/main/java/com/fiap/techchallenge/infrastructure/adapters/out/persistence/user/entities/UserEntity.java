package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities;

import com.fiap.techchallenge.domain.model.enums.UserEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    private LocalDateTime lastModifiedDate;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum userEnum;

}
