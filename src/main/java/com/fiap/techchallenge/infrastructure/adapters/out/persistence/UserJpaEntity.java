package com.fiap.techchallenge.infrastructure.adapters.out.persistence;

import com.fiap.techchallenge.domain.model.User;
import com.fiap.techchallenge.domain.model.User.UserType;
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
public class UserJpaEntity {

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
    private UserType type;

    public static UserJpaEntity fromDomainModel(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .password(user.getPassword())
                .lastModifiedDate(user.getLastModifiedDate())
                .address(user.getAddress())
                .type(user.getType())
                .build();
    }

    public User toDomainModel() {
        return new User(
                this.id,
                this.name,
                this.email,
                this.login,
                this.password,
                this.lastModifiedDate,
                this.address,
                this.type
        );
    }
}
