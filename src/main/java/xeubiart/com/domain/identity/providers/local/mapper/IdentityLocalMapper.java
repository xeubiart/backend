package xeubiart.com.domain.identity.providers.local.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalInputDTO;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IdentityLocalMapper {
    IdentityLocal toEntity(IdentityLocalInputDTO dto);
}
