package xeubiart.com.domain.identity.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum IdentityProviders {
    LOCAL("local"),
    GOOGLE("google");

    private final String name;

    public static IdentityProviders fromCode(String code) {
        return Stream.of(IdentityProviders.values())
                .filter(t -> t.getName().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código inválido: " + code));
    }
}
