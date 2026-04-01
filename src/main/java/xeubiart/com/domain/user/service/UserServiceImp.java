package xeubiart.com.domain.user.service;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xeubiart.com.domain.identity.dto.IdentityInputDTO;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.identity.service.IdentityService;
import xeubiart.com.domain.user.dto.UserInputDTO;
import xeubiart.com.domain.user.mapper.UserMapper;
import xeubiart.com.domain.user.model.User;
import xeubiart.com.domain.user.repository.UserRepository;
import xeubiart.com.infra.emailSender.EmailSenderService;
import xeubiart.com.infra.emailSender.dto.EmailDuplicatedRegisterDTO;
import xeubiart.com.infra.emailSender.dto.EmailVerificationCodeDTO;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final IdentityService identityService;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional
    public Optional<IdentityOutputDTO> register(UserInputDTO userDTO, IdentityInputDTO identityDTO, IdentityProviders provider){
        if(this.identityService.existsByEmail(identityDTO.getEmail())){
            handleDuplicatedRegister(identityDTO.getEmail(), userDTO.getName());
            return Optional.empty();
        }

        User user = this.userMapper.toEntity(userDTO);

        User savedUser = this.userRepository.save(user);

        identityDTO.setUser(savedUser);

        return Optional.of(this.identityService.setupIdentity(savedUser, identityDTO, provider));
    }
    private void handleDuplicatedRegister(String email, String name){
        try {
            this.emailSenderService.sendDuplicatedRegisterEmail(
                    new EmailDuplicatedRegisterDTO(
                        email,
                        name
                    )
            );
        } catch (MessagingException e) {
            // TODO re-try send email again
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email){
        return this.identityService.findByEmail(email);
    }
//
//    @Override
//    public Optional<User> findById(UUID id) throws UserNotFoundException {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(UUID id) {
//        return false;
//    }
}
