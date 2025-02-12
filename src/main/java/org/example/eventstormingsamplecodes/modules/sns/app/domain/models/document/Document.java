package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Document {
    private final DocumentId id;
    private String content;
    private final UserId createdBy;
    private final LocalDateTime createdAt;
    private UserId lastModifiedBy;
    private LocalDateTime lastModifiedAt;
    private Set<UserId> effectiveUserIds;

    @Builder
    public Document(DocumentId id, String content, UserId createdBy) {
        this.id = id;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedBy = createdBy;
        this.lastModifiedAt = this.createdAt;
        this.effectiveUserIds = new HashSet<>();
    }

    public void update(String content, UserId modifiedBy) {
        this.content = content;
        this.lastModifiedBy = modifiedBy;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void markEffective(UserId userId) {
        effectiveUserIds.add(userId);
    }

    public void unmarkEffective(UserId userId) {
        effectiveUserIds.remove(userId);
    }

    public boolean isEffectiveFor(UserId userId) {
        return effectiveUserIds.contains(userId);
    }

    public Set<UserId> getEffectiveUserIds() {
        return Collections.unmodifiableSet(effectiveUserIds);
    }
}