package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto extends GenericResponseDto {

    private final String errorCode;
    private final String errorMessage;

    @Builder
    public ErrorResponseDto(String errorCode, String errorMessage) {
        super(false);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
