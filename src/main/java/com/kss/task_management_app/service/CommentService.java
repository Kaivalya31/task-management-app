package com.kss.task_management_app.service;

import com.kss.task_management_app.exception.TaskNotFoundException;
import com.kss.task_management_app.exception.UserNotFoundException;
import com.kss.task_management_app.model.Comment;
import com.kss.task_management_app.model.Task;
import com.kss.task_management_app.model.User;
import com.kss.task_management_app.repository.CommentRepository;
import com.kss.task_management_app.repository.TaskRepository;
import com.kss.task_management_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Long taskId, Long commenterId, Comment comment){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        User commenter = userRepository.findById(commenterId).orElseThrow(() ->
                new UserNotFoundException("User not found"));
        comment.setTask(task);
        comment.setCommenter(commenter);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }
}
