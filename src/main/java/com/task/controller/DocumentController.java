package com.task.controller;

import com.task.dto.AttributeDto;
import com.task.dto.DocumentDto;
import com.task.entity.User;
import com.task.enumeration.Status;
import com.task.exception.ServiceException;
import com.task.service.DocumentService;
import com.task.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Document")
public class DocumentController {
    private final DocumentService documentService;
    private final StorageService storageService;

    @Value("${bucket.document.name:task}")
    private String bucketName;

    @Value("${bucket.document.folder:document}")
    private String folderName;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add document", description = "Creates document metadata and saves document")
    public void createDocumentMetadata(@Valid @RequestPart("metadata") DocumentDto request,
                                       @RequestPart("document") MultipartFile document,
                                       @AuthenticationPrincipal User user) {
        try {
            String fileUrl = storageService.uploadFileToBucket(document, bucketName, folderName);
            request.setUrl(fileUrl);

            documentService.createDocument(request, user);
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update document", description = "Updates document metadata")
    public void updateDocument(@Valid @RequestBody DocumentDto request) {
        documentService.updateDocument(request);
    }

    @GetMapping("/versions")
    @Operation(summary = "Document Version", description = "Retrieves document of specific version")
    public ResponseEntity<List<DocumentDto>> getDocumentsAsOfDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(documentService.getDocumentsAsOfDate(date, pageable));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter document", description = "Retrieves recent versions of documents filtered by status and by some predefined attribute")
    public ResponseEntity<List<DocumentDto>> getDocumentsByStatusAndAttribute(
            @RequestParam Status status,
            @RequestParam String attributeKey,
            @RequestParam String attributeValue,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(documentService.getDocumentsByStatusAndAttribute(status, attributeKey, attributeValue, pageable));
    }

    @PutMapping("/{versionId}/attributes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update attributes", description = "Updates specific attributes")
    public ResponseEntity<DocumentDto> updateAttributes(
            @PathVariable Long versionId,
            @RequestBody List<AttributeDto> attributes) {
        return ResponseEntity.ok(documentService.updateDocumentAttributes(versionId, attributes));
    }
}
