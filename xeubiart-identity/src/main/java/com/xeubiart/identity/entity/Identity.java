package com.xeubiart.identity.entity;

import com.xeubiart.identity.model.IdentityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
    name = "tb_identity",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_account_provider",
        columnNames = {"account_id", "provider"}
    )
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public abstract class Identity implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Column(nullable = false, updatable = false)
    protected UUID accountId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false, updatable = false)
    protected IdentityType provider;

    @Builder.Default
    protected boolean active = false;

    public abstract IdentityType getProvider();

    public void markAsVerified(){
        this.active = true;
    }
}

