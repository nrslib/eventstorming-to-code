package org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.jpa;

import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.notificationtemplate.NotificationTemplateDataModel;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateJpaRepository extends JpaRepository<NotificationTemplateDataModel, String> {
    Optional<NotificationTemplateDataModel> findByType(NotificationType type);
    Optional<NotificationTemplateDataModel> findByTypeAndActiveTrue(NotificationType type);
}
