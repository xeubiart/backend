package com.xeubiart.infra.seeder;

import com.xeubiart.account.entity.Account;
import com.xeubiart.account.model.AccountRole;
import com.xeubiart.account.repository.AccountRepository;
import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.providers.local.entity.IdentityLocal;
import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;
import com.xeubiart.identity.providers.local.repository.IdentityLocalRepository;
import com.xeubiart.identity.service.IdentityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
@Profile("!prod")
public class AdminSeeder {

    @Value("${adm.user.email}")
    private String adminEmail;

    @Value("${adm.user.password}")
    private String adminPassword;

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final IdentityLocalRepository identityLocalRepository;

    public AdminSeeder(PasswordEncoder passwordEncoder, AccountRepository accountRepository, IdentityLocalRepository identityLocalRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.identityLocalRepository = identityLocalRepository;
    }

    @Bean
    CommandLineRunner initDatabase(AccountRepository repository) {
        return args -> {
            if (repository.findByEmail(adminEmail).isEmpty()) {
                Account admin = new Account();
                admin.setEmail(adminEmail);
                admin.setRole(AccountRole.ADMIN);
                admin.setUsername("admin");

                Account savedAccount = this.accountRepository.saveAndFlush(admin);

                IdentityLocal identityLocal = IdentityLocal.builder()
                        .password(this.passwordEncoder.encode(adminPassword))
                        .accountId(savedAccount.getId())
                        .provider(IdentityType.LOCAL)
                        .active(true)
                        .build();

                this.identityLocalRepository.saveAndFlush(identityLocal);

                System.out.println("✅ Test Admin account created.");
            } else {
                System.out.println("ℹ️ Admin account already exists, skipping seed.");
            }
        };
    }
}