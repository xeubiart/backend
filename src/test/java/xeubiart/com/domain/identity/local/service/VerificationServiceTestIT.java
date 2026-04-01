package xeubiart.com.domain.identity.local.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xeubiart.com.common.BaseIT;
import xeubiart.com.common.TestUser;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;
import xeubiart.com.domain.identity.providers.local.repository.IdentityLocalRepository;
import xeubiart.com.domain.identity.providers.local.service.VerificationLocalService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class VerificationServiceTestIT extends BaseIT {
    @Autowired
    private VerificationLocalService verificationLocalService;
    @Autowired
    private IdentityLocalRepository identityLocalRepository;

    @Test
    public void should_Verify(){
//        TestUser testUser = this.register();
//
//        this.verificationLocalService.verify(String.valueOf(testUser.getVerification().getId()),testUser.getVerification().getCode());
//
//        IdentityLocal userIdentity = this.identityLocalRepository.findByEmail(testUser.getIdentity().getEmail()).orElseThrow();
//        assertThat(userIdentity.isActive()).isTrue();
    }
}
