package org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.jpa;

import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification.NotificationStatus;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification.PaymentNotificationDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentNotificationJpaRepository extends JpaRepository<PaymentNotificationDataModel, String> {
    // 契約IDで通知を検索
    List<PaymentNotificationDataModel> findByContractId(String contractId);

    // ステータスと作成日時で通知を検索
    // 例：失敗した通知の再送用
    List<PaymentNotificationDataModel> findByStatusAndCreatedAtBefore(
            NotificationStatus status,
            LocalDateTime createdAt
    );
}
