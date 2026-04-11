package com.xeubiart.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
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
}
