package com.task.service;

import com.task.dto.AttributeDto;
import com.task.dto.DocumentDto;
import com.task.entity.Document;
import com.task.enumeration.Status;
import com.task.mapper.DocumentMapper;
import com.task.repository.DocumentAttributeRepository;
import com.task.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentAttributeRepository documentAttributeRepository;

    @InjectMocks
    private DocumentService documentService;

    @Mock
    private DocumentMapper documentMapper;

    @Test
    void shouldUpdateDocument() {
        DocumentDto request = new DocumentDto();
        request.setId(1L);
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setVersion(1);

        Document document = new Document();
        document.setId(1L);
        document.setTitle("Updated Title");
        document.setDescription("Updated Description");

        when(documentMapper.toEntity(request)).thenReturn(document);
        when(documentRepository.save(any())).thenReturn(document);

        documentService.updateDocument(request);

        verify(documentRepository, times(1)).save(any());
    }

    @Test
    void shouldUpdateDocumentAttributes() {
        Long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);

        List<AttributeDto> attributesDto = List.of(
                new AttributeDto("priority", "high"),
                new AttributeDto("category", "finance")
        );

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        documentService.updateDocumentAttributes(documentId, attributesDto);

        verify(documentAttributeRepository, times(1)).saveAll(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingAttributesForNonExistentDocument() {
        Long documentId = 1L;
        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                documentService.updateDocumentAttributes(documentId, List.of(new AttributeDto("key", "value")))
        );
    }

    @Test
    void shouldGetDocumentsAsOfDate() {
        LocalDateTime date = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);
        Document document = new Document();
        document.setId(1L);
        document.setTitle("Test Document");

        List<Document> documentList = List.of(document);
        Page<Document> documents = new PageImpl<>(documentList, pageable, documentList.size());
        when(documentRepository.findByCreatedAtBefore(date, pageable)).thenReturn(documents);

        List<DocumentDto> result = documentService.getDocumentsAsOfDate(date, pageable);

        assertNotNull(result);
        verify(documentRepository, times(1)).findByCreatedAtBefore(date, pageable);
    }

    @Test
    void shouldGetDocumentsByStatusAndAttribute() {
        Status status = Status.ACTIVE;
        String attributeKey = "priority";
        String attributeValue = "high";
        Pageable pageable = PageRequest.of(0, 10);
        Document document = new Document();
        document.setId(1L);
        document.setTitle("Test Document");

        List<Document> documentList = List.of(document);
        Page<Document> documents = new PageImpl<>(documentList, pageable, documentList.size());
        when(documentRepository.findByStatusAndAttribute(status, attributeKey, attributeValue, pageable))
                .thenReturn(documents);

        List<DocumentDto> result = documentService.getDocumentsByStatusAndAttribute(status, attributeKey, attributeValue, pageable);

        assertNotNull(result);
        verify(documentRepository, times(1)).findByStatusAndAttribute(status, attributeKey, attributeValue, pageable);
    }
}
