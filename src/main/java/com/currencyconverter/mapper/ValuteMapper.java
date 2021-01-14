package com.currencyconverter.mapper;

import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.model.Valute;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ValuteMapper {

//    ValuteMapper MAPPER = Mappers.getMapper(ValuteMapper.class);

    Valute toValute(ValuteDto dto);

    @InheritInverseConfiguration
    ValuteDto fromValute(Valute valute);

    List<Valute> toValuteList(List<ValuteDto> valuteDtos);

    List<ValuteDto> fromValuteList(Collection<Valute> valutes);

}
