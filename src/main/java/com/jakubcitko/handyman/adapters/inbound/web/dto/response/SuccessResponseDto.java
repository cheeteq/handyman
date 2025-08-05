package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import lombok.Getter;

@Getter
public class SuccessResponseDto extends GenericResponseDto {
    private final String message;
    public SuccessResponseDto(String message) {
        super(true);
        this.message = message;
    }
}
