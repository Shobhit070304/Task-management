package com.taskapi.task_management.controllers;

import com.taskapi.task_management.dto.TaskRequest;
import com.taskapi.task_management.models.Task;
import com.taskapi.task_management.service.TaskService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        Task newTask = taskService.createTask(request.getTitle(), request.getDescription(), request.getStatus(), request.getPriority(), request.getDueDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        Task updatedTask = taskService.updateTask(id, request.getTitle(), request.getDescription(), request.getStatus(), request.getPriority(), request.getDueDate());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/status")
    public ResponseEntity<List<Task>> searchByStatus(@RequestParam String status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search/priority")
    public ResponseEntity<List<Task>> searchByPriority(@RequestParam String priority) {
        List<Task> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Task>> searchByTitle(@RequestParam String title) {
        List<Task> tasks = taskService.getTasksByTitle(title);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", taskService.getTaskCount());
        stats.put("pending", taskService.getTaskCountByStatus("PENDING"));
        stats.put("inProgress", taskService.getTaskCountByStatus("IN_PROGRESS"));
        stats.put("completed", taskService.getTaskCountByStatus("COMPLETED"));

        return ResponseEntity.ok(stats);
    }
}

