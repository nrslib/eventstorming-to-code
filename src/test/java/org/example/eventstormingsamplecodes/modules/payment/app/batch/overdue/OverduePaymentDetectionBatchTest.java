package org.example.eventstormingsamplecodes.modules.payment.app.batch.overdue;

import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract.ContractDataModel;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract.ContractStatus;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.jpa.ContractJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OverduePaymentDetectionBatchTest {
    @Mock
    private ContractJpaRepository contractRepository;

    @Mock
    private PaymentOverdueNotificationService notificationService;

    private OverduePaymentDetectionBatch process;

    @BeforeEach
    void setUp() {
        process = new OverduePaymentDetectionBatch(contractRepository, notificationService);
    }

    @Test
    void confirmPayment_WithOverdueContracts_ProcessesAllContracts() {
        // Arrange
        var now = LocalDateTime.now();
        var contract1 = createContract("1");
        var contract2 = createContract("2");

        var page1 = new PageImpl<>(Arrays.asList(contract1, contract2));
        Page<ContractDataModel> emptyPage = new PageImpl<>(Collections.emptyList());

        when(contractRepository.findOverdueContracts(
                eq(now),
                eq(ContractStatus.CONFIRMED),
                any(),
                any(PageRequest.class)))
                .thenReturn(page1)
                .thenReturn(emptyPage);

        // Act
        process.confirmPayment(now);

        // Assert
        verify(contractRepository, times(2)).findOverdueContracts(
                eq(now),
                eq(ContractStatus.CONFIRMED),
                any(),
                any(PageRequest.class));

        verify(notificationService, times(2)).sendOverdueNotification(any());
        verify(contractRepository, times(2)).save(any());
        verify(contractRepository, times(1)).flush();

        var contractCaptor = ArgumentCaptor.forClass(ContractDataModel.class);
        verify(contractRepository, times(2)).save(contractCaptor.capture());

        assertThat(contractCaptor.getAllValues())
                .hasSize(2)
                .allMatch(c -> c.getStatus() == ContractStatus.OVERDUE)
                .allMatch(c -> c.getLastCheckedAt() != null);
    }

    private ContractDataModel createContract(String id) {
        var contract = new ContractDataModel();
        contract.setId(id);
        contract.setStatus(ContractStatus.CONFIRMED);
        return contract;
    }
}
