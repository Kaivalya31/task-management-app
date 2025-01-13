package com.kss.task_management_app.controller;

import com.kss.task_management_app.model.Comment;
import com.kss.task_management_app.model.Task;
import com.kss.task_management_app.response.ApiResponse;
import com.kss.task_management_app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTask(@RequestBody Task task, @RequestParam List<Long> approverIds) {
        Task createdTask = taskService.createTask(task, approverIds);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Task created successfully", createdTask));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks fetched successfully", tasks));
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task deleted successfully"));
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task fetched successfully", task));
    }

    @PostMapping("/{taskId}/add-approver/{approverId}")
    public ResponseEntity<ApiResponse<Task>> addApprover(@PathVariable Long taskId, @PathVariable Long approverId) {
        Task updatedTask = taskService.addApprover(taskId, approverId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Approver added successfully", updatedTask));
    }

    @PostMapping("/{taskId}/approve/{approverId}")
    public ResponseEntity<ApiResponse<Void>> approveTask(@PathVariable Long taskId, @PathVariable Long approverId) {
        taskService.approveTask(taskId, approverId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task approved successfully"));
    }

    @PostMapping("/{taskId}/add-comment/{commenterId}")
    public ResponseEntity<ApiResponse<Comment>> addComment(@PathVariable Long taskId, @PathVariable Long commenterId,
                                                           @RequestBody Comment comment) {
        Comment savedComment = taskService.addComment(taskId, commenterId, comment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Comment added successfully", savedComment));
    }

}
