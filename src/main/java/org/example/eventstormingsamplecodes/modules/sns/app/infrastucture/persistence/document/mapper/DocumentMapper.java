package org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.mapper;

import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.Document;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentId;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.UserId;
import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.datamodel.DocumentDataModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DocumentMapper {
    public Document toDomainModel(DocumentDataModel dataModel) {
        var document = Document.builder()
                .id(new DocumentId(dataModel.getId()))
                .content(dataModel.getContent())
                .createdBy(new UserId(dataModel.getCreatedBy()))
                .build();

        dataModel.getEffectiveUserIds()
                .forEach(userId -> document.markEffective(new UserId(userId)));

        return document;
    }

    public DocumentDataModel toDataModel(Document document) {
        var dataModel = new DocumentDataModel();
        dataModel.setId(document.getId().value());
        dataModel.setContent(document.getContent());
        dataModel.setCreatedBy(document.getCreatedBy().value());
        dataModel.setCreatedAt(document.getCreatedAt());
        dataModel.setLastModifiedBy(document.getLastModifiedBy().value());
        dataModel.setLastModifiedAt(document.getLastModifiedAt());
        dataModel.setEffectiveUserIds(
                document.getEffectiveUserIds().stream()
                        .map(UserId::value)
                        .collect(Collectors.toSet())
        );
        return dataModel;
    }
}
