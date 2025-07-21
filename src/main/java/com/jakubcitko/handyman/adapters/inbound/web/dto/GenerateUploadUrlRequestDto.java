package com.jakubcitko.handyman.adapters.inbound.web.dto;

public record GenerateUploadUrlRequestDto(
        String originalFilename,
        String contentType,
        long fileSize
) {
}
