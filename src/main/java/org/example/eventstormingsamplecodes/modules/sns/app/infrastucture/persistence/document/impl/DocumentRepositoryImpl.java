package org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.impl;

import lombok.RequiredArgsConstructor;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.Document;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentId;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentRepository;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.UserId;
import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.jpa.DocumentJpaRepository;
import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.mapper.DocumentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {
    private final DocumentJpaRepository jpaRepository;
    private final DocumentMapper mapper;

    @Override
    public Document save(Document document) {
        var dataModel = mapper.toDataModel(document);
        var savedModel = jpaRepository.save(dataModel);
        return mapper.toDomainModel(savedModel);
    }

    @Override
    public Document findById(DocumentId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomainModel)
                .orElse(null);
    }

    @Override
    public List<Document> findByEffectiveUserId(UserId userId) {
        return jpaRepository.findByEffectiveUserIdsContaining(userId.value())
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }
}
