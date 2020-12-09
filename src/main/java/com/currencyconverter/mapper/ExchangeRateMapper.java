package com.currencyconverter.mapper;

import com.currencyconverter.dto.ExchangeRateDto;
import com.currencyconverter.dto.ValuteDto;
import com.currencyconverter.model.ExchangeRate;
import com.currencyconverter.model.Valute;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
@Mapper
public interface ExchangeRateMapper {

     ExchangeRateMapper MAPPER = Mappers.getMapper(com.currencyconverter.mapper.ExchangeRateMapper.class);

     ExchangeRate toExchangeRate(ExchangeRateDto rateDto);

     @InheritInverseConfiguration
     ExchangeRateDto fromExchangeRate(ExchangeRate rate);

     List<ExchangeRate> toExchangeRateList(List<ExchangeRateDto> rateDtos);

     List<ExchangeRateDto> fromExchangeRateList(Collection<ExchangeRate> rates);

}
