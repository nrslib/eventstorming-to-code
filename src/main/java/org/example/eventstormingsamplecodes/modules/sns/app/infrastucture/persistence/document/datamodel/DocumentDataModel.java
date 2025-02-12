package org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
public class DocumentDataModel {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @ElementCollection
    @CollectionTable(
            name = "document_effectives",
            joinColumns = @JoinColumn(name = "document_id")
    )
    private Set<String> effectiveUserIds = new HashSet<>();
}