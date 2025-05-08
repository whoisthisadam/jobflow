package com.app.appUser.converter;

import com.app.appUser.AppUser;
import com.app.appUser.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<AppUser, UserDto> {

    @Override
    public UserDto convert(AppUser source) {
        return new UserDto(
                source.getId(),
                source.getUsername(),
                source.getRole().name(),

                source.getFio(),

                source.getImg(),

                source.getDate(),
                source.getDateFormatted(),

                source.getExp(),

                source.getCategoryId(),
                source.getCategoryName(),
                source.getCategorySum(),

                source.getTasksCount(),
                source.getTasksIntensity(),

                source.getIncome()
        );
    }
}
