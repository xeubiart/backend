package xeubiart.com.domain.user.service;

import xeubiart.com.domain.identity.dto.IdentityInputDTO;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.user.dto.UserInputDTO;
import xeubiart.com.domain.user.model.User;

import java.util.Optional;

public interface UserService {
    Optional<IdentityOutputDTO> register(UserInputDTO userDTO, IdentityInputDTO identityDTO, IdentityProviders provider);
    Optional<User> findUserByEmail(String email);
//    Optional<User> findById(UUID id) throws UserNotFoundException;
//    boolean existsById(UUID id);
}
