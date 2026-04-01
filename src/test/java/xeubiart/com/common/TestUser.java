package xeubiart.com.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.providers.local.entity.VerificationLocal;
import xeubiart.com.domain.user.model.User;

@Data
@AllArgsConstructor
public class TestUser{
    User user;
    Identity identity;
    VerificationLocal verification;
}