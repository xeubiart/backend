package xeubiart.com.domain.identity.providers.local.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import xeubiart.com.domain.identity.providers.local.dto.VerificationLocalOutputDTO;
import xeubiart.com.domain.identity.providers.local.entity.VerificationLocal;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface VerificationLocalMapper {
    VerificationLocal toEntity(VerificationLocalOutputDTO dto);
    VerificationLocalOutputDTO toDto(VerificationLocal entity);
}
