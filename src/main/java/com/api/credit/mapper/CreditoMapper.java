package com.api.credit.mapper;


import com.api.credit.dto.CreditoDTO;
import com.api.credit.entity.Credito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditoMapper {

    @Mapping(target = "simplesNacional", source = "simplesNacional", qualifiedByName = "booleanToString")
    CreditoDTO toDTO(Credito credito);

    List<CreditoDTO> toDTOList(List<Credito> creditos);

    @Named("booleanToString")
    default String booleanToString(boolean value) {
        return value ? "Sim" : "Não";
    }
}