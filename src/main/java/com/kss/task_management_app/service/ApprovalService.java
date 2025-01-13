package com.kss.task_management_app.service;

import com.kss.task_management_app.exception.DuplicateApproverException;
import com.kss.task_management_app.model.Approval;
import com.kss.task_management_app.repository.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    public Approval addApproval(Approval approval, Long taskId, Long approverId){
        //Check if the approval has been already sought for the current task from the current user.
        if(approvalRepository.findByTaskIdAndApproverId(taskId, approverId).isPresent()){
            throw new DuplicateApproverException("Approver already added");
        }

        return approvalRepository.save(approval);
    }

    public Approval updateApproval(Approval approval){
        return approvalRepository.save(approval);
    }

    public Optional<Approval> getApprovalByTaskIdAndApproverId(Long taskId, Long approverId){
        return approvalRepository.findByTaskIdAndApproverId(taskId, approverId);
    }
}
