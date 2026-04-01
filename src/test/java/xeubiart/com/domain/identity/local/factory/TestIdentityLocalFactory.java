package xeubiart.com.domain.identity.local.factory;

import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalInputDTO;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;
import xeubiart.com.domain.user.model.User;

import java.util.UUID;

public class TestIdentityLocalFactory {
    public static IdentityLocalInputDTO createDTO(){
        String unique = UUID.randomUUID().toString().substring(0, 8);

        return IdentityLocalInputDTO.builder()
                .user(null)
                .email(unique + "@gmail.com")
                .password("123123")
                .build();
    }

    public static IdentityLocal createEntity(User user, boolean active){
        String unique = UUID.randomUUID().toString().substring(0, 8);

        return IdentityLocal.builder()
                .password("123123")
                .active(active)
                .build();
    }

}
