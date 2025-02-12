package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

public record DocumentId(String value) {
    public DocumentId {
        if (value == null) {
            throw new IllegalArgumentException("DocumentId must not be null");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("DocumentId must not be blank");
        }
    }
}
