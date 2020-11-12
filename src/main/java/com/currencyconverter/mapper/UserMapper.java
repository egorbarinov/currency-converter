package com.currencyconverter.mapper;

import com.currencyconverter.dto.UserDto;
import com.currencyconverter.model.User;
import com.currencyconverter.model.Valute;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    User toUser(UserDto dto);

    @InheritInverseConfiguration
    UserDto fromValute(Valute valute);

    List<Valute> toUserList(List<UserDto> userDtos);

    List<UserDto> fromUserList(Collection<Valute> users);
}
