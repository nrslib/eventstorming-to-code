package org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_notifications")
@Data
public class PaymentNotificationDataModel {
    @Id
    private String id;
    @Column(name = "contract_id", nullable = false)
    private String contractId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @OneToMany
    @CollectionTable(
            name = "notification_recipients",
            joinColumns = @JoinColumn(name = "notification_id")
    )
    private Set<NotificationRecipient> recipients = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
//
//
//
//@Entity
//@Table(name = "payment_notifications")
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class PaymentNotificationDataModel {
//    @Id
//    private String id;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private NotificationType type;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private NotificationStatus status;
//
//    @Column(name = "contract_id", nullable = false)
//    private String contractId;
//
//    @Column(name = "customer_id", nullable = false)
//    private String customerId;
//
//    @Column(name = "invoice_id")
//    private String invoiceId;
//
//    @Column(name = "template_id", nullable = false)
//    private String templateId;
//
//    @Column(name = "created_at", nullable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "scheduled_at")
//    private LocalDateTime scheduledAt;
//
//    @Column(name = "sent_at")
//    private LocalDateTime sentAt;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private NotificationPriority priority;
//
//    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<NotificationRecipient> recipients = new ArrayList<>();
//
//    @Version
//    private Long version;
//
//    public void addRecipient(NotificationRecipient recipient) {
//        recipients.add(recipient);
//        recipient.setNotification(this);
//    }
//
//    @PrePersist
//    protected void onCreate() {
//        if (createdAt == null) {
//            createdAt = LocalDateTime.now();
//        }
//        if (status == null) {
//            status = NotificationStatus.CREATED;
//        }
//    }
//}
