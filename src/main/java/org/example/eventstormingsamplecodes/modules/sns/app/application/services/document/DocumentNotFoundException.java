package org.example.eventstormingsamplecodes.modules.sns.app.application.services.document;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
