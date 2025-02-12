package org.example.eventstormingsamplecodes.modules.sns.http.models.documents;

public record PutDocumentRequest(String documentId, String userId, String content) {
}
