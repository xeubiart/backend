package com.xeubiart.identity.providers.local.mapper;

import com.xeubiart.identity.providers.local.entity.IdentityLocal;
import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T15:26:46+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Oracle Corporation)"
)
@Component
public class IdentityLocalMapperImpl implements IdentityLocalMapper {

    @Override
    public IdentityLocal toEntity(IdentityLocalInputDTO inputDTO) {
        if ( inputDTO == null ) {
            return null;
        }

        IdentityLocal.IdentityLocalBuilder<?, ?> identityLocal = IdentityLocal.builder();

        if ( inputDTO.getProvider() != null ) {
            identityLocal.provider( inputDTO.getProvider() );
        }
        if ( inputDTO.getPassword() != null ) {
            identityLocal.password( inputDTO.getPassword() );
        }

        return identityLocal.build();
    }
}
