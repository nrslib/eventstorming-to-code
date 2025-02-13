package org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.jpa;

import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract.ContractDataModel;
import org.example.eventstormingsamplecodes.modules.payment.app.infrastucture.persistence.payment.datamodel.contract.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ContractJpaRepository extends JpaRepository<ContractDataModel, Long> {
    @Query("SELECT c FROM ContractDataModel c " +
            "WHERE c.dueDate < :baseDate " +
            "AND c.status = :status " +
            "AND (c.lastCheckedAt IS NULL OR c.lastCheckedAt < :lastCheckBefore)")
    Page<ContractDataModel> findOverdueContracts(
            @Param("baseDate") LocalDateTime baseDate,
            @Param("status") ContractStatus status,
            @Param("lastCheckBefore") LocalDateTime lastCheckBefore,
            Pageable pageable
    );
}
