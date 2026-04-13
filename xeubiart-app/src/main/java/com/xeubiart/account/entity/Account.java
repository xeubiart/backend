package com.xeubiart.account.entity;

import com.xeubiart.account.model.AccountRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Email
    @NonNull
    private String email;

    @NotBlank
    private String username;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountRole role = AccountRole.USER;
}
