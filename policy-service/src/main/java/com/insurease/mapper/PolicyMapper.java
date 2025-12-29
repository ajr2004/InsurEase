package com.insurease.mapper;

import com.insurease.dto.PolicyUpdateDTO;
import com.insurease.model.Policy;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    // Only update fields that exist in DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePolicyFromDto(PolicyUpdateDTO dto, @MappingTarget Policy policy);
}
