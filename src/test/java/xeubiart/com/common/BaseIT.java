package xeubiart.com.common;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.local.factory.TestIdentityLocalFactory;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalInputDTO;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalOutputDTO;
import xeubiart.com.domain.identity.providers.local.entity.VerificationLocal;
import xeubiart.com.domain.identity.providers.local.repository.VerificationLocalRepository;
import xeubiart.com.domain.identity.repository.IdentityRepository;
import xeubiart.com.domain.identity.service.IdentityService;
import xeubiart.com.domain.user.dto.UserInputDTO;
import xeubiart.com.domain.user.factory.TestUserFactory;
import xeubiart.com.domain.user.mapper.UserMapper;
import xeubiart.com.domain.user.model.User;
import xeubiart.com.domain.user.model.UserRole;
import xeubiart.com.domain.user.repository.UserRepository;
import xeubiart.com.domain.user.service.UserService;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class BaseIT {
    @Autowired
    protected UserService userService;
    @Autowired
    protected IdentityRepository identityRepository;
    @Autowired
    protected VerificationLocalRepository verificationRepository;

    protected TestUser register(){
        IdentityLocalInputDTO identityDTO = TestIdentityLocalFactory.createDTO();

        IdentityLocalOutputDTO registerResult = (IdentityLocalOutputDTO) this.userService.register(TestUserFactory.createDTO(), identityDTO, IdentityProviders.LOCAL).orElseThrow();

        User savedUser = this.userService.findUserByEmail(identityDTO.getEmail()).orElseThrow(() -> new RuntimeException("Error setting up the user in test"));
        Identity savedIdentity = this.identityRepository.findByEmail(identityDTO.getEmail()).orElseThrow(() -> new RuntimeException("Error setting up the user in test"));

        VerificationLocal verificationLocal = this.verificationRepository.findById(registerResult.getSessionToken()).orElseThrow();
        return new TestUser(savedUser, savedIdentity, verificationLocal);
    }

    public void loginAnonymous(){
        SecurityContextHolder.clearContext();
    }

}
