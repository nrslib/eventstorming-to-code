package org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.notificationtemplate;

import jakarta.persistence.*;
import lombok.Data;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification.NotificationType;

@Entity
@Table(name = "notification_templates")
@Data
public class NotificationTemplateDataModel {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean active = true;
}