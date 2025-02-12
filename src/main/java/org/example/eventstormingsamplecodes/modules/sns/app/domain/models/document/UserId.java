package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

public record UserId(String value) {
    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("UserId must not be blank");
        }
    }
}
