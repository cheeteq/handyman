package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import com.jakubcitko.handyman.adapters.inbound.web.dto.response.GenericResponseDto;
import lombok.Getter;
import java.util.UUID;

@Getter
public class CreateResourceResponseDto extends GenericResponseDto {
    private final UUID id;

    public CreateResourceResponseDto(UUID id) {
        super(true);
        this.id = id;
    }
}
