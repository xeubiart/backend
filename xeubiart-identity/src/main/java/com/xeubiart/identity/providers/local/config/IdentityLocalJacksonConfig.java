package com.xeubiart.identity.providers.local.config;

import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.jsontype.NamedType;

@Configuration
public class IdentityLocalJacksonConfig {
    @Bean
    public JsonMapperBuilderCustomizer registerLocalSubtype() {
        return builder -> builder.registerSubtypes(
                new NamedType(IdentityLocalInputDTO.class, "LOCAL")
        );
    }
}
