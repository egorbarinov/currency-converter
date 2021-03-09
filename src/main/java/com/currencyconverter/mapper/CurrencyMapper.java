package com.currencyconverter.mapper;

import com.currencyconverter.dto.CurrencyDto;
import com.currencyconverter.model.Currency;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

//    ValuteMapper MAPPER = Mappers.getMapper(ValuteMapper.class);

    Currency toCurrency(CurrencyDto dto);

    @InheritInverseConfiguration
    CurrencyDto fromCurrency(Currency currency);

    List<Currency> toCurrencyList(List<CurrencyDto> currencyDtos);

    List<CurrencyDto> fromCurrencyList(Collection<Currency> currencies);

}
