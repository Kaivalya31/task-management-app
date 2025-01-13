package com.kss.task_management_app.repository;

import com.kss.task_management_app.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    Optional<Approval> findByTaskIdAndApproverId(Long taskId, Long approverId);
}
