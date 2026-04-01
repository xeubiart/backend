package xeubiart.com.domain.identity.providers.local.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_local_verification")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class VerificationLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String code;

    @NotNull
    private LocalDateTime expiry;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "identity_id")
    @NotNull
    private IdentityLocal identity;
}