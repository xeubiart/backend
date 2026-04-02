package xeubiart.com.domain.identity.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xeubiart.com.domain.user.model.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_identities")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "provider_type")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public abstract class Identity implements UserDetails, Serializable {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(unique = true, nullable = false)
    private String email;

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.user.getRole().name()));
    }

    @Override
    public @Nullable String getPassword() {
        return null;
    }

    @Override
    public @NullMarked String getUsername() {
        // Spring treats it internally as username, even though is the email...
        return this.email;
    }

    public String getUserName(){
        return this.user.getName();
    }

}
