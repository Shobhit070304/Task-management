package com.taskapi.task_management.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 1L;

    // Constructor - kuch sample data add kar dete hain
    public TaskController() {
        tasks.add(new Task(nextId++, "Learn Spring Boot", "PENDING"));
        tasks.add(new Task(nextId++, "Build REST API", "IN_PROGRESS"));
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return tasks;
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Task createTask(@RequestBody TaskRequest request) {
        Task newTask = new Task(nextId++, request.getTitle(), "PENDING");
        tasks.add(newTask);
        return newTask;
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        Task task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (task != null) {
            task.setTitle(request.getTitle());
            if (request.getStatus() != null) {
                task.setStatus(request.getStatus());
            }
        }
        return task;
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        tasks.removeIf(task -> task.getId().equals(id));
        return "Task deleted successfully";
    }

    @GetMapping("/search")
    public List<Task> searchByStatus(@RequestParam String status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    @GetMapping("/count")
    public int getTaskCount() {
        return tasks.size();
    }
}

class Task {
    private Long id;
    private String title;
    private String status;

    public Task(Long id, String title, String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class TaskRequest {
    private String title;
    private String status;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
