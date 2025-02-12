package org.example.eventstormingsamplecodes.modules.sns.http.models.documents;

import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.datamodel.DocumentDataModel;

public record GetDocumentResponse(
        String documentId,
        String userId,
        String content
) {
    public static GetDocumentResponse from(DocumentDataModel document) {
        return new GetDocumentResponse(
                document.getId(),
                document.getUserId(),
                document.getContent()
        );
    }
}
