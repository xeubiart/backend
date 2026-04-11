package com.xeubiart.identity.providers.local.entity;

import com.xeubiart.identity.entity.Identity;
import com.xeubiart.identity.model.IdentityType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="tb_identity_local")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public class IdentityLocal extends Identity implements UserDetails {
    @NotBlank
    private String password;

    @Override
    public IdentityType getProvider(){
        return IdentityType.LOCAL;
    }

    // User Details specific methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        // We did this to keep identity completely decoupled from the base application
        // It relies on AccountService doing his job right and providing the correct account id
        return this.accountId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}

