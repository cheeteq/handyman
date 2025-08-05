package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import java.util.UUID;

public record AttachmentDetailsResponseDto(
        UUID fileUid,
        String originalFilename
) {
}
