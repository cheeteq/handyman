package com.jakubcitko.handyman.adapters.inbound.web;

import com.jakubcitko.handyman.adapters.inbound.web.dto.GenerateUploadUrlRequestDto;
import com.jakubcitko.handyman.core.application.port.in.GenerateUploadUrlUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final GenerateUploadUrlUseCase generateUploadUrlUseCase;

    public FileController(GenerateUploadUrlUseCase generateUploadUrlUseCase) {
        this.generateUploadUrlUseCase = generateUploadUrlUseCase;
    }

    @PostMapping("/generate-upload-url")
    @PreAuthorize("isAuthenticated()") // Każdy zalogowany użytkownik może generować URL
    public ResponseEntity<GenerateUploadUrlUseCase.UploadUrlResponse> generateUploadUrl(@RequestBody GenerateUploadUrlRequestDto requestDto) {

        var command = new GenerateUploadUrlUseCase.GenerateUploadUrlCommand(
                requestDto.originalFilename(),
                requestDto.contentType(),
                requestDto.fileSize()
        );

        GenerateUploadUrlUseCase.UploadUrlResponse response = generateUploadUrlUseCase.generateUploadUrl(command);

        return ResponseEntity.ok(response);
    }
}
