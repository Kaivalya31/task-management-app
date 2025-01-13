package com.kss.task_management_app.service;

import com.kss.task_management_app.model.Task;
import com.kss.task_management_app.model.User;
import com.kss.task_management_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void notifyTaskCreated(Task task) {
        User creator = task.getCreator();
        String subject = "Task Created: " + task.getTitle();
        String message = "Hello " + creator.getName() + ",\n\nYour task \"" + task.getTitle() + "\" has been created successfully.";

        sendEmail(creator.getEmail(), subject, message);
    }

    public void notifyApproverOfTask(User approver, Task task) {
        String subject = "Approval Needed for Task: " + task.getTitle();
        String message = "Hello " + approver.getName() + ",\n\nYou have been assigned to approve the task \"" + task.getTitle() + "\".";

        sendEmail(approver.getEmail(), subject, message);
    }

    public void notifyCreatorOfApproval(Task task, Long approverId) {
        User creator = task.getCreator();
        String subject = "Task Approved: " + task.getTitle();
        User approver = userRepository.findById(approverId).get();
        String message = "Hello " + creator.getName() + ",\n\nYour task \"" + task.getTitle() + "\" has been approved by: " + approver.getName() + ".";

        sendEmail(creator.getEmail(), subject, message);
    }

    public void notifyTaskApproved(User approver, Task task) {
        String subject = "Task Fully Approved: " + task.getTitle();
        String message = "Hello,\n\nThe task \"" + task.getTitle() + "\" has been approved by the required number of approvers and is now marked as APPROVED.";


        sendEmail(approver.getEmail(), subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
