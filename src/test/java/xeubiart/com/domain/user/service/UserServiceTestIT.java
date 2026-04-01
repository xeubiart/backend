package xeubiart.com.domain.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xeubiart.com.common.BaseIT;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.local.factory.TestIdentityLocalFactory;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalInputDTO;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalOutputDTO;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;
import xeubiart.com.domain.identity.providers.local.entity.VerificationLocal;
import xeubiart.com.domain.identity.providers.local.repository.IdentityLocalRepository;
import xeubiart.com.domain.identity.providers.local.repository.VerificationLocalRepository;
import xeubiart.com.domain.identity.providers.local.service.VerificationLocalService;
import xeubiart.com.domain.identity.repository.IdentityRepository;
import xeubiart.com.domain.user.dto.UserInputDTO;
import xeubiart.com.domain.user.factory.TestUserFactory;
import xeubiart.com.domain.user.model.User;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceTestIT extends BaseIT {
    @Autowired
    private UserService userService;

    // To assert
    @Autowired
    private IdentityLocalRepository identityLocalRepository;
    @Autowired
    private IdentityRepository identityRepository;
    @Autowired
    private VerificationLocalRepository verificationLocalRepository;

    @Test
    void registerUser_Success(){
        UserInputDTO userDTO = TestUserFactory.createDTO();
        IdentityLocalInputDTO identityDTO = TestIdentityLocalFactory.createDTO();

        IdentityLocalOutputDTO result = (IdentityLocalOutputDTO) this.userService.register(userDTO, identityDTO, IdentityProviders.LOCAL).orElseThrow();

        // Check if an entry was created in UserRepository
        User savedEntity = this.userService.findUserByEmail(identityDTO.getEmail()).orElseThrow();

        // Check if an entry was created in IdentityLocalRepository
        IdentityLocal identityLocal = this.identityLocalRepository.findByEmail(identityDTO.getEmail()).orElseThrow();
        assertThat(identityLocal.getUser()).isEqualTo(savedEntity);
        assertThat(identityLocal.isActive()).isFalse();
        assertThat(identityLocal.getPassword()).isNotEqualTo(identityDTO.getPassword());

        // Check if an entry was created in IdentityRepository
        Identity identity = this.identityRepository.findByEmail(identityDTO.getEmail()).orElseThrow();
        assertThat(identity.getUser()).isEqualTo(savedEntity);
    }
}
