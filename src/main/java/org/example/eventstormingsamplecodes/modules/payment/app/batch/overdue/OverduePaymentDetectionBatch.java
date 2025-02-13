package org.example.eventstormingsamplecodes.modules.payment.app.batch.overdue;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract.ContractDataModel;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract.ContractStatus;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.jpa.ContractJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OverduePaymentDetectionBatch {
    private final ContractJpaRepository contractRepository;
    private final PaymentOverdueNotificationService notificationService;

    private int checkIntervalHours = 24;
    private int chunkSize = 100;

    public OverduePaymentDetectionBatch(ContractJpaRepository contractRepository, PaymentOverdueNotificationService notificationService) {
        this.contractRepository = contractRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void confirmPayment(LocalDateTime dateTime) {
        var lastCheckBefore = dateTime.minusHours(checkIntervalHours);

        try {
            var processedCount = 0;

            while (true) {
                var page = contractRepository.findOverdueContracts(
                        dateTime,
                        ContractStatus.CONFIRMED,
                        lastCheckBefore,
                        PageRequest.of(0, chunkSize)
                );

                if (page.isEmpty()) {
                    break;
                }

                for (var contract : page.getContent()) {
                    try {
                        processOverdueContract(contract);
                    } catch (Exception e) {
                        log.error("Error processing contract {}: {}", contract.getId(), e.getMessage(), e);
                    }
                }

                contractRepository.flush();
                processedCount++;
            }
            log.info("Completed overdue contract processing. Processed {} contracts", processedCount);
        } catch (Exception e) {
            log.error("Error in batch processing: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void processOverdueContract(ContractDataModel contract) {
        log.debug("Processing overdue contract: {}", contract.getId());

        // ステータス更新
        contract.setStatus(ContractStatus.OVERDUE);
        contract.setLastCheckedAt(LocalDateTime.now());

        // 通知送信
        notificationService.sendOverdueNotification(contract);

        contractRepository.save(contract);
    }
}
