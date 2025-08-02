package com.jakubcitko.handyman;

import com.jakubcitko.handyman.adapters.outbound.filestorage.S3FileStorageAdapter;
import com.jakubcitko.handyman.bootstrap.config.S3Config;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(
        properties = "spring.autoconfigure.exclude=io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration"
)
@ActiveProfiles("test")
@Transactional
public abstract class AbstractSpringBootTest {
    @MockitoBean
    S3Config s3Config;
    @MockitoBean
    S3FileStorageAdapter fileStorageAdapter;
}
