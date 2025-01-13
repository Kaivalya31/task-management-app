package com.kss.task_management_app.service;

import com.kss.task_management_app.exception.DuplicateApproverException;
import com.kss.task_management_app.exception.TaskApprovalException;
import com.kss.task_management_app.exception.TaskNotFoundException;
import com.kss.task_management_app.model.Approval;
import com.kss.task_management_app.model.Comment;
import com.kss.task_management_app.model.Task;
import com.kss.task_management_app.model.User;
import com.kss.task_management_app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private EmailService emailService;

    public Task createTask(Task task, List<Long> approverIds) {
        User creator = userService.getUserById(task.getCreator().getId());
        task.setCreator(creator);

        Task savedTask = taskRepository.save(task);

        emailService.notifyTaskCreated(savedTask);

        for(Long approverId : approverIds){
            this.addApprover(savedTask.getId(), approverId);
        }

        return savedTask;
    }

    //adds approver to the task and approval entity to approval
    public Task addApprover(Long taskId, Long approverId) {
        /*Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        User approver = userService.getUserById(approverId);

        Approval approval = new Approval();
        approval.setTask(task);
        approval.setApprover(approver);
        approval.setStatus(false);

        Approval savedApproval = approvalService.addApproval(approval, taskId, approverId);

        //update task iff a new approver has been added
        Task savedTask = null;
        if(savedApproval != null){
            List<Approval> currentTaskApprovals = task.getApprovals();
            if(currentTaskApprovals == null){
                currentTaskApprovals = new ArrayList<>();
            }
            currentTaskApprovals.add(savedApproval);
            task.setApprovals(currentTaskApprovals);
            savedTask = taskRepository.save(task);
            emailService.notifyApproverOfTask(approver, savedTask);
        }

        return savedTask;*/

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        User approver = userService.getUserById(approverId);

        Optional<Approval> existingApproval = approvalService.getApprovalByTaskIdAndApproverId(taskId, approverId);
        if (existingApproval.isPresent()) {
            throw new DuplicateApproverException("Approver has already been added to this task");
        }

        Approval approval = new Approval();
        approval.setTask(task);
        approval.setApprover(approver);
        approval.setStatus(false);

        Approval savedApproval = approvalService.addApproval(approval, taskId, approverId);

        List<Approval> currentApprovals = task.getApprovals();
        if (currentApprovals == null) {
            currentApprovals = new ArrayList<>();
        }
        currentApprovals.add(savedApproval);
        task.setApprovals(currentApprovals);

        Task updatedTask = taskRepository.save(task);

        emailService.notifyApproverOfTask(approver, updatedTask);

        return updatedTask;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with ID " + taskId + " not found"));
    }

    public void deleteTaskById(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task with ID " + taskId + " not found");
        }
        taskRepository.deleteById(taskId);
    }

    public String approveTask(Long taskId, Long approverId) {
        /*Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return "not found";
        }

        Task task = taskOptional.get();
        if (task.getCreator().getId().equals(approverId)) {
            return "creator";
        }

        Optional<Approval> existingApproval = approvalService.getApprovalByTaskIdAndApproverId(taskId, approverId);
        if(existingApproval.isEmpty()){
            return "unauthorized approver";
        }

        if (existingApproval.get().getStatus()) {
            return "approved";
        }

        existingApproval.get().setStatus(true);
        existingApproval.get().setApprovedAt(LocalDateTime.now());
        Approval updatedApproval = approvalService.updateApproval(existingApproval.get());

        //update approval count in the task
        if(updatedApproval != null){
            task.setApprovalCount(task.getApprovalCount() + 1);
        }

        if (task.getApprovalCount() >= 3) {
            task.setStatus("APPROVED");
            for(Approval approval : task.getApprovals()){
                User approver = approval.getApprover();
                emailService.notifyTaskApproved(approver, task);
            }
        }

        emailService.notifyCreatorOfApproval(task, approverId);

        taskRepository.save(task);

        return "Task approved successfully";*/

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (task.getCreator().getId().equals(approverId)) {
            throw new TaskApprovalException("Task creator cannot approve their own task");
        }

        Approval existingApproval = approvalService.getApprovalByTaskIdAndApproverId(taskId, approverId)
                .orElseThrow(() -> new TaskApprovalException("Unauthorized approver"));

        if (existingApproval.getStatus()) {
            throw new TaskApprovalException("User has already approved this task");
        }

        existingApproval.setStatus(true);
        existingApproval.setApprovedAt(LocalDateTime.now());
        approvalService.updateApproval(existingApproval);

        task.setApprovalCount(task.getApprovalCount() + 1);

        emailService.notifyCreatorOfApproval(task, approverId);

        if (task.getApprovalCount() == 3) {
            task.setStatus("APPROVED");
            for(Approval approval : task.getApprovals()){
                User approver = approval.getApprover();
                emailService.notifyTaskApproved(approver, task);
            }
            emailService.notifyTaskApproved(task.getCreator(), task);
        }

        taskRepository.save(task);

        return "Task approved successfully";
    }

    public Comment addComment(Long taskId, Long commenterId, Comment comment) {
        return commentService.addComment(taskId, commenterId, comment);
    }
}
