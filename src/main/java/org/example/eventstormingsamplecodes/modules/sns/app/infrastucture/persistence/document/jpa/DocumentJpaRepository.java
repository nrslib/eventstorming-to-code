package org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.jpa;

import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.datamodel.DocumentDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentJpaRepository extends JpaRepository<DocumentDataModel, String> {
    List<DocumentDataModel> findByEffectiveUserIdsContaining(String userId);
}
