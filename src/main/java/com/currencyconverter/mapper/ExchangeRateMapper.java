package com.currencyconverter.mapper;

import com.currencyconverter.dto.ExchangeRateDto;
import com.currencyconverter.model.ExchangeRate;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    ExchangeRate toExchangeRate(ExchangeRateDto exchangeRateDto);

    @InheritInverseConfiguration
    ExchangeRateDto fromExchangeRate(ExchangeRate exchangeRate);

    List<ExchangeRate> toExchangeRateList(List<ExchangeRateDto> exchangeRateDtos);

    List<ExchangeRateDto> fromExchangeRateList(List<ExchangeRate> exchangeRates);



}
