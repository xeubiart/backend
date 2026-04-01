package xeubiart.com.domain.identity.providers.local.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.user.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_identity_local")
@PrimaryKeyJoinColumn(name = "id")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class IdentityLocal extends Identity {

    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = false;

    @OneToOne(mappedBy = "identity", cascade = CascadeType.ALL, orphanRemoval = true)
    private transient VerificationLocal verification;

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
