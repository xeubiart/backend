package xeubiart.com.domain.identity.dto;

import lombok.Getter;
import lombok.Setter;
import xeubiart.com.domain.identity.model.Identity;

@Getter
@Setter
public class IdentityOutputDTO {
    protected Identity identity;

    public IdentityOutputDTO() {
    }
}
