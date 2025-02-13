package org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.paymentnotification;

public enum NotificationStatus {
    CREATED,
    SCHEDULED,
    SENDING,
    SENT,
    FAILED,
    CANCELLED
}