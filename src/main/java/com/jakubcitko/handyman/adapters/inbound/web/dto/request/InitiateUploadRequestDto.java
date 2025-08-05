package com.jakubcitko.handyman.adapters.inbound.web.dto.request;


import java.util.List;

public record InitiateUploadRequestDto(
        List<FileMetadataDto> files
) {

    public record FileMetadataDto(
            String filename,
            String contentType,
            long size
    ) {}
}