package org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.datamodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class DocumentEffectiveId implements Serializable {
    @Column(name = "document_id")
    private String documentId;

    @Column(name = "user_id")
    private String userId;
}