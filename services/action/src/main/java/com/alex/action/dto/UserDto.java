package com.alex.action.dto;

public record UserDto(
        Integer id,
        String firstName,
        String lastName,
        String username,
        Boolean isAccountPublic
) {
}
