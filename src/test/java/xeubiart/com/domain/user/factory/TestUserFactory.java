package xeubiart.com.domain.user.factory;

import xeubiart.com.domain.user.dto.UserInputDTO;
import xeubiart.com.domain.user.model.User;
import xeubiart.com.domain.user.model.UserRole;

import java.util.UUID;

public class TestUserFactory {

    public static UserInputDTO createDTO(){
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new UserInputDTO(
                unique + "@gmail.com"
        );
    }

    public static User createEntity(UserRole role){
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Persistent User");
        user.setRole(role);
        return user;
    }

}
