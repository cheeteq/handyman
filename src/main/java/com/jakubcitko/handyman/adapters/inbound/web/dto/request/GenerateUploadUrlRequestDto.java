package com.jakubcitko.handyman.adapters.inbound.web.dto.request;

public record GenerateUploadUrlRequestDto(
        String originalFilename,
        String contentType,
        long fileSize
) {
}
