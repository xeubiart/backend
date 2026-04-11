package com.xeubiart.identity.providers.local.mapper;

import com.xeubiart.identity.providers.local.entity.IdentityLocal;
import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IdentityLocalMapper {
    IdentityLocal toEntity(IdentityLocalInputDTO inputDTO);
}
