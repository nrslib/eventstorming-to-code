package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

import java.util.List;

public interface DocumentRepository {
    Document save(Document document);

    Document findById(DocumentId id);

    List<Document> findByEffectiveUserId(UserId userId);
}
