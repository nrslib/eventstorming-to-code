package org.example.eventstormingsamplecodes.modules.sns.app.application.services.document;

import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.Document;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentId;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentRepository;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.UserId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentApplicationService {
    private final DocumentRepository documentRepository;

    public DocumentApplicationService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public DocumentId createDocument(UserId userId, String content) {
        var documentId = new DocumentId(UUID.randomUUID().toString());

        var document = new Document(documentId, content, userId);
        documentRepository.save(document);

        return documentId;
    }

    public void updateDocument(DocumentId documentId, UserId userId, String content) {
        var document = documentRepository.findById(documentId);
        if (document == null) {
            throw new DocumentNotFoundException("Document not found");
        }

        document.update(content, userId);
        documentRepository.save(document);
    }

    public void markEffective(DocumentId documentId, UserId userId) {
        var document = documentRepository.findById(documentId);
        if (document == null) {
            throw new DocumentNotFoundException("Document not found");
        }
        if (document.isEffectiveFor(userId)) {
            return;
        }

        document.markEffective(userId);
        documentRepository.save(document);
    }

    public void unmarkEffective(DocumentId documentId, UserId userId) {
        var document = documentRepository.findById(documentId);
        if (document == null) {
            throw new DocumentNotFoundException("Document not found");
        }
        if (!document.isEffectiveFor(userId)) {
            return;
        }

        document.unmarkEffective(userId);
        documentRepository.save(document);
    }
}
