package org.example.eventstormingsamplecodes.modules.payment.app.batch.overdue;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract.ContractDataModel;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.notificationtemplate.NotificationTemplateDataModel;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification.NotificationStatus;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification.NotificationType;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification.PaymentNotificationDataModel;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.jpa.PaymentNotificationJpaRepository;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.jpa.TemplateJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class PaymentOverdueNotificationService {
    private PaymentNotificationJpaRepository notificationRepository;
    private TemplateJpaRepository templateRepository;

    @Transactional
    public void sendOverdueNotification(ContractDataModel contract) {
        var template = templateRepository
                .findByType(NotificationType.PAYMENT_OVERDUE)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        var notification = new PaymentNotificationDataModel();
        notification.setId(UUID.randomUUID().toString());
        notification.setContractId(contract.getId());
        notification.setType(NotificationType.PAYMENT_OVERDUE);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus(NotificationStatus.CREATED);

        notificationRepository.save(notification);

        try {
            // メール送信の実装（省略）
            sendEmail(contract, template);

            notification.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            log.error("Failed to send notification for contract {}",
                    contract.getId(), e);
            notification.setStatus(NotificationStatus.FAILED);
        }

        notificationRepository.save(notification);
    }

    private void sendEmail(ContractDataModel contract, NotificationTemplateDataModel template) {
        // メール送信の実装（省略）
    }
}
