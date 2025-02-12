package org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.datamodel;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "document_effectives")
public class DocumentEffectiveDataModel {
    @EmbeddedId
    private DocumentEffectiveId id;

    @Column(name = "marked_at")
    private LocalDateTime markedAt;
}