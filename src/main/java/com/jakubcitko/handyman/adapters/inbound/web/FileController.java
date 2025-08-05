package com.jakubcitko.handyman.adapters.inbound.web;

import com.jakubcitko.handyman.adapters.inbound.security.AuthService;
import com.jakubcitko.handyman.adapters.inbound.web.dto.request.InitiateUploadRequestDto;
import com.jakubcitko.handyman.core.application.port.in.InitiateFileUploadUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final AuthService authService;
    private final InitiateFileUploadUseCase initiateFileUploadUseCase;

    public FileController(AuthService authService, InitiateFileUploadUseCase initiateFileUploadUseCase) {
        this.authService = authService;
        this.initiateFileUploadUseCase = initiateFileUploadUseCase;
    }

    @PostMapping("/initiate-upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<InitiateFileUploadUseCase.FileUploadInfo>> initiateFileUpload(@RequestBody InitiateUploadRequestDto requestDto) {

        var command = new InitiateFileUploadUseCase.InitiateUploadCommand(
                authService.getLoggedUserId(),
                requestDto.files().stream()
                        .map(f -> new InitiateFileUploadUseCase.FileToUpload(f.filename(), f.contentType(), f.size()))
                        .toList());


        List<InitiateFileUploadUseCase.FileUploadInfo> uploadInfos = initiateFileUploadUseCase.initiateUpload(command);

        return ResponseEntity.ok(uploadInfos);
    }
}
