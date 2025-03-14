package com.task.service;

import com.task.dto.AttributeDto;
import com.task.dto.DocumentDto;
import com.task.dto.UserDto;
import com.task.entity.Document;
import com.task.entity.DocumentAttribute;
import com.task.entity.User;
import com.task.enumeration.Status;
import com.task.mapper.DocumentMapper;
import com.task.mapper.UserMapper;
import com.task.repository.DocumentAttributeRepository;
import com.task.repository.DocumentRepository;
import com.task.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DocumentAttributeRepository documentAttributeRepository;
    private final DocumentMapper documentMapper;

    public void createDocument(DocumentDto request) {
        documentRepository.save(documentMapper.toEntity(request));
    }

    public DocumentDto updateDocument(DocumentDto request) {
        Document documentToSave = documentMapper.toEntity(request);
        documentToSave.setVersion(request.getVersion()+1);
        Document saved = documentRepository.save(documentToSave);
        return DocumentMapper.INSTANCE.toDto(saved);
    }

    public DocumentDto updateDocumentAttributes(Long documentId, List<AttributeDto> attributesDto) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document version not found"));

        List<DocumentAttribute> attributes = attributesDto.stream()
                .map(dto -> {
                    DocumentAttribute attr = new DocumentAttribute();
                    attr.setDocument(document);
                    attr.setAttributeKey(dto.getKey());
                    attr.setAttributeValue(dto.getValue());
                    return attr;
                })
                .collect(Collectors.toList());

        documentAttributeRepository.saveAll(attributes);
        return DocumentMapper.INSTANCE.toDto(document);
    }

    public List<DocumentDto> getDocumentsAsOfDate(LocalDateTime date, Pageable pageable) {
        return documentRepository.findByCreatedAtBefore(date, pageable)
                .stream()
                .map(DocumentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public List<DocumentDto> getDocumentsByStatusAndAttribute(Status status, String attributeKey, String attributeValue, Pageable pageable) {
        return documentRepository.findByStatusAndAttribute(status, attributeKey, attributeValue, pageable)
                .stream()
                .map(DocumentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public DocumentDto updateDocumentOwners(Long documentId, List<UserDto> ownersDto) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        List<User> owners = ownersDto.stream()
                .map(dto -> userRepository.findById(dto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found")))
                .collect(Collectors.toList());

        document.setOwners(owners);
        Document savedDocument = documentRepository.save(document);
        return DocumentMapper.INSTANCE.toDto(savedDocument);
    }
}
