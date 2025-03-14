package com.task.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.controller.DocumentController;
import com.task.dto.AttributeDto;
import com.task.dto.DocumentDto;
import com.task.entity.Document;
import com.task.entity.User;
import com.task.enumeration.Role;
import com.task.enumeration.Status;
import com.task.repository.DocumentRepository;
import com.task.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private UserRepository userRepository;
    @InjectMocks
    private DocumentController documentController;

    @MockBean
    private StorageService storageService;

    @Test
    void shouldCreateDocumentMetadataAndSaveFile() throws Exception {
        String mockFileUrl = "https://mock-storage.com/document/test.pdf";
        Mockito.when(storageService.uploadFileToBucket(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(mockFileUrl);

        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setPassword("securepass");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setTitle("Test Document");
        documentDto.setDescription("Test Description");
        documentDto.setStatus(Status.ACTIVE);

        documentDto.setAttributes(List.of(new AttributeDto("key", "value")));
        String metadataJson = objectMapper.writeValueAsString(documentDto);

        MockMultipartFile documentFile = new MockMultipartFile(
                "document",
                "test.pdf",
                "application/pdf",
                "Dummy file content".getBytes());

        MockMultipartFile metadata = new MockMultipartFile(
                "metadata",
                "",
                "application/json",
                metadataJson.getBytes());

        mockMvc.perform(multipart("/api/v1/documents/add")
                        .file(metadata)
                        .file(documentFile)
                        .with(user(user))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        List<Document> documents = documentRepository.findAll();
        assertThat(!documents.isEmpty());
        assertThat(documents.size()).isEqualTo(1);

        Document savedDocument = documents.get(0);
        assertThat(savedDocument.getTitle()).isEqualTo("Test Document");
        assertThat(savedDocument.getDescription()).isEqualTo("Test Description");
        assertThat(savedDocument.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(savedDocument.getUrl()).isEqualTo(mockFileUrl);
    }
}

