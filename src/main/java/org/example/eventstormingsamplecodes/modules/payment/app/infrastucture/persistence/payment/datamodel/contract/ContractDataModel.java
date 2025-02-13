package org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Data
public class ContractDataModel {
    @Id
    private String id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;
}
